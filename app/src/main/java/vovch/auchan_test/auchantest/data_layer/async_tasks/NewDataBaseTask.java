package vovch.auchan_test.auchantest.data_layer.async_tasks;

import android.os.AsyncTask;

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
 * Created by vovch on 24.12.2017.
 */

public class NewDataBaseTask extends AsyncTask<Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private boolean type = false;

    @Override
    public SList[] doInBackground(Object... loginPair){
        type = (Boolean) loginPair[0];
        SList[] result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        if(type) {
            result = activeActivityProvider.dataExchanger.getOfflineActiveData();
        }
        else {
            result = activeActivityProvider.dataExchanger.getOfflineHistoryData();
        }

        return result;
    }
    @Override
    public void onPostExecute(SList[] result){
            if (result == null || result.length == 0) {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsBad(result);
                } else {
                    activeActivityProvider.showOfflineHistoryListsBad(result);
                }
            } else {
                if (type) {
                    activeActivityProvider.showOfflineActiveListsGood(result);
                } else {
                    activeActivityProvider.showOfflineHistoryListsGood(result);
                }
            }
            activeActivityProvider = null;
    }
}
