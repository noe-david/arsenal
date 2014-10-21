package com.edit.reach.model;

import android.util.Log;
import com.edit.reach.constants.GetStatus;
import com.edit.reach.model.interfaces.IMilestone;
import com.edit.reach.model.interfaces.MilestonesReceiver;
import com.edit.reach.system.Remote;
import com.edit.reach.system.ResponseHandler;
import com.edit.reach.system.WorldTruckerEndpoints;
import com.edit.reach.utils.RankingDistanceUtil;
import com.edit.reach.utils.RankingUtil;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static com.edit.reach.model.interfaces.IMilestone.Category;

public class Ranking {

    private static GetStatus status;

    private Ranking() {
        status = GetStatus.PENDING;
    }

    public static void getMilestonesByRank(LatLng driverPoint, LatLng maxPoint, Category category, final MilestonesReceiver milestonesReceiver) {
        BoundingBox bbox = new BoundingBox(driverPoint, maxPoint);
        RankingUtil rankingUtil = new RankingUtil(category);
        performGet(bbox, rankingUtil, milestonesReceiver);
    }

    public static void getMilestonesByDistance(LatLng driverPoint, LatLng maxPoint, Category category, final MilestonesReceiver milestonesReceiver) {
        BoundingBox bbox = new BoundingBox(driverPoint, maxPoint);
        RankingUtil rankingUtil = new RankingDistanceUtil(category, driverPoint);
        performGet(bbox, rankingUtil, milestonesReceiver);
    }

    public static void getMilestones(final MilestonesReceiver milestonesReceiver, LatLng centralPoint, double sideLength) {
        BoundingBox bbox = new BoundingBox(centralPoint, sideLength);
        performGet(bbox, null, milestonesReceiver);
    }

    private static void performGet(BoundingBox bbox, final RankingUtil rankingUtil, final MilestonesReceiver milestonesReceiver) {
        status = GetStatus.RUNNING;

        try {
            URL url = WorldTruckerEndpoints.getMilestonesURL(bbox);
            Remote.get(url, new ResponseHandler() {

                @Override
                public void onGetSuccess(JSONObject json) {
                    ArrayList<IMilestone> milestones = new ArrayList<IMilestone>();

                    try {
                        JSONObject paging = json.getJSONObject("paging");
                        int milestonesCount = paging.getInt("objectcount");


                        if (milestonesCount > 0) {
                            JSONArray features = json.getJSONArray("features");

                            for (int i = 0; i < milestonesCount; ++i) {
                                IMilestone milestone = new Milestone(features.getJSONObject(i));
                                milestones.add(milestone);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("RankingTest", e.getMessage());
                    }

                    if (rankingUtil != null) {
                        rankingUtil.removeUnwanted(milestones);
                        Collections.sort(milestones, rankingUtil);
                    }

                    milestonesReceiver.onMilestonesRecieved(milestones);

                    status = GetStatus.SUCCEEDED;
                }

                @Override
                public void onGetFail() {
                    milestonesReceiver.onMilestonesGetFailed();
                    status = GetStatus.FAILED;
                }
            });
        } catch (MalformedURLException e) {
            Log.d("RankingTest", e.getMessage());
        }
    }

    public static GetStatus getStatus() {
        return status;
    }

    public static void reset() {
        status = GetStatus.PENDING;
    }

}
