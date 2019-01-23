package vovch.auchan_test.auchantest.data_layer.runnables.uilayer;

import android.util.Log;

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


public class ListInformersCreatorTask implements Runnable {
    private UserGroup[] groups;
    private ActiveActivityProvider provider;
    private boolean isUpdateComplete;

    public ListInformersCreatorTask(UserGroup[] groups, ActiveActivityProvider provider, boolean isUpdateComplete){
        this.groups = groups;
        this.provider = provider;
        this. isUpdateComplete = isUpdateComplete;
    }

    @Override
    public void run() {
        try {
            provider.dataExchanger.setGroups(groups);
            if(groups != null) {
                provider.setActiveGroup(groups[0]);
                provider.getOfflineActiveData();
                provider.getOfflineHistoryData();
            }

            //ListInformer[] informers = provider.dataExchanger.createListinformers();
            //presenter(informers, isUpdateComplete);
        } catch (Exception e){
            Log.d("WhoBuys","ListinformersCreatorTask");
        }
    }
    private void presenter(ListInformer[] result, boolean isUpdateComplete){
        if (result == null) {
            if (provider.userSessionData.isLoginned()) {
                provider.showListInformersGottenBad();
            } else {
                provider.badLoginTry(provider.getString(R.string.log_yourself_in));
            }
        } else {
            provider.showListInformersGottenGood(result, isUpdateComplete);
        }
    }
}
