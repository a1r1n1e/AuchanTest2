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

public class DisactivateOfflineTask extends AsyncTask<Object, Void, SList>{
    private ActiveActivityProvider activeActivityProvider;
    private SList tempList;
    @Override
    public SList doInBackground(Object... loginPair){
        SList result;
        tempList = (SList) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.disactivateOfflineList(tempList);
        return result;
    }
    @Override
    public void onPostExecute(SList list){
        if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }if (list != null) {
            activeActivityProvider.showOfflineActiveListsDisactivatedGood(list);
        } else {
            activeActivityProvider.showOfflineActiveListsDisactivatedBad(tempList);
        }
        tempList = null;
        activeActivityProvider = null;
    }
}
