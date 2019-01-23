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
 * Created by vovch on 07.01.2018.
 */

public class RemoveAddedUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        if(loginPair[0] != null && loginPair[1] != null && loginPair[2] != null) {
            result = (AddingUser) loginPair[0];
            activityType = (Integer) loginPair[1];
            activeActivityProvider = (ActiveActivityProvider) loginPair[2];
            result = activeActivityProvider.dataExchanger.removeUser(result);
        }
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if (activityType == 5) {
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserNewGroupGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserNewGroupBad(null);
            }
        } else if (activityType == 7) {
            if (result != null) {
                activeActivityProvider.showRemoveAddedUserSettingsGood(result);
            } else {
                activeActivityProvider.showRemoveAddedUserSettingsBad(null);
            }
        }
        activeActivityProvider = null;
    }
}
