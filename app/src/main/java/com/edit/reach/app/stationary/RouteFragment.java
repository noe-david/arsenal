package com.edit.reach.app.stationary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.edit.reach.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RouteFragment.OnRouteInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RouteFragment extends Fragment {

    private static final String ARG_ID = "Route";
    private String mId;

    private EditText etFrom;
    private EditText etTo;
    private List<EditText> etListOfVia;

    private OnRouteInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Id of the fragment
     * @return A new instance of fragment EditRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteFragment newInstance(String id) {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
        }
    }

    private View.OnClickListener addDestinationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            editText();

        }
    };

    private View.OnClickListener getNearestRouteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String strFrom = etFrom.getText().toString();
            String strTo = etTo.getText().toString();
            List<String> strListOfVia = new ArrayList<String>();
            for(EditText et: etListOfVia){
                strListOfVia.add(et.getText().toString());
            }


            //Send : strFrom, strTo, listOfVia to map-Activity

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_route, container, false);

        etFrom = (EditText) view.findViewById(R.id.etFrom);
        etTo = (EditText) view.findViewById(R.id.etTo);


        etListOfVia = new ArrayList<EditText>();

        Button btGetNearestRoute = (Button) view.findViewById(R.id.btSubmitNearestRoute);
        btGetNearestRoute.setOnClickListener(getNearestRouteListener);
        Button btAddDestination = (Button) view.findViewById(R.id.bt_add_destination);
        btAddDestination.setOnClickListener(addDestinationListener);


		return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRouteInteraction(uri);


        }
    }

    //Kan behövas för att dynamiskt lägga till fler textfält för del-destinationer
    private EditText editText(){
        EditText editText = new EditText(getActivity());
        editText.setHint("By");
        editText.setWidth(180);
        etListOfVia.add(editText);
        return editText;

    }

    //Kan behövas för att dynamiskt lägga till fler textfält för del-destinationer
    private LinearLayout linearLayout (int _intID){
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setId(_intID);


        return ll;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRouteInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRouteInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRouteInteractionListener {
        // TODO: Update argument type and name
        public void onRouteInteraction(Uri uri);
    }




}
