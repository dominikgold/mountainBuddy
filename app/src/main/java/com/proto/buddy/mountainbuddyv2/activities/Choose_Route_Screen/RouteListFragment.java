package com.proto.buddy.mountainbuddyv2.activities.Choose_Route_Screen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TabHost;


import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.AppLogic.RouteManager;
import com.proto.buddy.mountainbuddyv2.activities.Choose_Route_Screen.ListAdapter.RouteListAdapterAllRoutes;
import com.proto.buddy.mountainbuddyv2.activities.Route_Item_Screen.RouteItemFragment;
import com.proto.buddy.mountainbuddyv2.activities.MainActivity;
import com.proto.buddy.mountainbuddyv2.database.DatabaseHelper;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RouteListFragment extends Fragment{

    private DatabaseHelper db;

    private MainActivity mainActivity;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView_all;

    private AbsListView mListView_my;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter_all;

    private ListAdapter mAdapter_my;

    private RouteManager routeManager;

    private FragmentManager fragmentManager;

    // TODO: Rename and change types of parameters
    public static RouteListFragment newInstance() {
        RouteListFragment fragment = new RouteListFragment();
        Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = this.getActivity().getSupportFragmentManager();

        DatabaseHelper db = new DatabaseHelper(this.getActivity().getApplicationContext());

        ArrayList<Route> allRoutes = db.getAllRoutes();

        for(Route r: allRoutes){
            System.out.println(r.toString());
            System.out.println("---------");
        }

        // initiate the listadapter
        mAdapter_all = new RouteListAdapterAllRoutes<String>(this.getActivity().getApplicationContext(), allRoutes);

       /* // assign the list adapter
        mAdapter_my = new RouteListAdapterMyRoutes<String>(this.getActivity().getApplicationContext(), routeManager.getMyRoutes());
*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_route, container, false);

        // Set the adapter
        mListView_all = (AbsListView) view.findViewById(R.id.list_all_routes);
        mListView_all.setAdapter(mAdapter_all);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Route currentRoute = (Route) mAdapter_all.getItem(position);
                routeManager.setCurrent(currentRoute);
                mainActivity.setRouteManager(routeManager);
                RouteItemFragment newFragment = new RouteItemFragment();
                Bundle args = new Bundle();
                args.putSerializable("route", currentRoute);
                newFragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment)
                        .addToBackStack("List_to_Item")
                        .commit();
            }
        });
        // Set the adapter
        mListView_my = (AbsListView) view.findViewById(R.id.list_my_routes);
        mListView_my.setAdapter(mAdapter_my);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView_my.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeManager.setCurrent((Route) mAdapter_my.getItem(position));
                fragmentManager.beginTransaction()
                        .replace(R.id.container, RouteItemFragment.newInstance())
                        .addToBackStack("List_to_Item")
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = ((MainActivity) activity);
        mainActivity.onSectionAttached(3);
        routeManager = mainActivity.getRouteManager();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mainActivity.setRouteManager(this.routeManager);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TabHost tabhost = (TabHost) getActivity().findViewById(R.id.myTabHost);
        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Alle Routen");
        tabhost.addTab(ts);
        ts = tabhost.newTabSpec("tag2");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Meine Routen");
        tabhost.addTab(ts);

    }
}
