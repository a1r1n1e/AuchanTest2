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
 * Created by vovch on 05.01.2018.
 */

public class OnlineDisactivateTask extends AsyncTask <Object, Void, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private SList taskList;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        taskList = (SList) loginPair[0];
        result = activeActivityProvider.dataExchanger.disactivateOnlineList(taskList);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup group){
        if (group != null) {
            //activeActivityProvider.showOnlineDisactivateListGood(group);
            activeActivityProvider.getOfflineHistoryData();
            activeActivityProvider.getOfflineActiveData();
        } else {
            //activeActivityProvider.showOnlineDisactivateListBad(taskList);
            activeActivityProvider.getOfflineHistoryData();
            activeActivityProvider.getOfflineActiveData();
        }
        taskList = null;
        activeActivityProvider = null;
    }
}
