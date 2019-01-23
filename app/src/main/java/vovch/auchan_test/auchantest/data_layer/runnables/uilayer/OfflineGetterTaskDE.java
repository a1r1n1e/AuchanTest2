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


public class OfflineGetterTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList[] activeLists;
    private SList[] historyLists;

    public OfflineGetterTaskDE(SList[] activeLists, SList[] historyLists, ActiveActivityProvider provider) {
        this.activeLists = activeLists;
        this.historyLists = historyLists;
        this.provider = provider;
    }


    @Override
    public void run() {
        try {

            provider.dataExchanger.setOfflineData(activeLists, historyLists);

            if (activeLists == null || activeLists.length == 0) {
                provider.showOfflineActiveListsBad(activeLists);
            } else {
                provider.showOfflineActiveListsGood(activeLists);
            }

            if (historyLists == null || historyLists.length == 0) {
                provider.showOfflineHistoryListsBad(historyLists);
            } else {
                provider.showOfflineHistoryListsGood(historyLists);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");
        }
    }
}

