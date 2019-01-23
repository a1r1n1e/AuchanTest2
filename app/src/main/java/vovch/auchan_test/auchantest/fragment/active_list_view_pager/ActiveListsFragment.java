package vovch.auchan_test.auchantest.fragment.active_list_view_pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import vovch.auchan_test.auchantest.activities.WithLoginActivity;
import vovch.auchan_test.auchantest.data_layer.*;
import vovch.auchan_test.auchantest.data_layer.async_tasks.*;
import vovch.auchan_test.auchantest.data_layer.firebase.*;
import vovch.auchan_test.auchantest.data_layer.runnables.background.*;
import vovch.auchan_test.auchantest.data_layer.runnables.uilayer.*;
import vovch.auchan_test.auchantest.activities.complex.*;
import vovch.auchan_test.auchantest.activities.simple.*;
import vovch.auchan_test.auchantest.data_types.*;
import vovch.auchan_test.auchantest.fragment.group_activity.*;
import vovch.auchan_test.auchantest.fragment.active_list_view_pager.*;
import vovch.auchan_test.auchantest.fragment.active_list_view_pager.active_lists_fragment_content.*;
import vovch.auchan_test.auchantest.recievers.*;
import vovch.auchan_test.auchantest.*;


/**
 * Created by vovch on 14.12.2017.
 */

public class ActiveListsFragment extends Fragment {
    private View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    public void checkRootView(ViewGroup container, LayoutInflater inflater){
        if(rootView == null){
            rootView = inflater.inflate(R.layout.active_lists_page_one_container, container, false);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.active_lists_page_one_container, container, false);
        unsetRefresher();
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        if(activity != null) {
            activity.loginToActiveFragmentChange();
        }
        return rootView;
    }
    public void fragmentShowGood(ListInformer[] result){}
    public void fragmentShowBad(ListInformer[] result){}
    public void cleaner(){
        FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.active_lists_page_one);
        layout.removeAllViewsInLayout();
    }
    public void noInternet(){
        setRefreresher();
        Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG)
                .show();
    }

    public void setRefreresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setEnabled(true);
        refreshLayout.setFocusable(true);
        refreshLayout.setRefreshing(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    public  void unsetRefresher(){
        SwipeRefreshLayout refreshLayout = getRefresher();
        refreshLayout.setEnabled(false);
        refreshLayout.setFocusable(false);
        refreshLayout.setRefreshing(false);
    }

    public void refresh(){
        ActiveListsActivity activity = (ActiveListsActivity) getActivity();
        activity.tryToLoginFromPrefs();
    }

    public SwipeRefreshLayout getRefresher() {
        return (SwipeRefreshLayout) rootView.findViewById(R.id.active_lists_container_refresher);
    }
}
