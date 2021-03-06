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

public class AddUserTask extends AsyncTask <Object, Void, AddingUser>{
    private ActiveActivityProvider activeActivityProvider;
    private String activityType;
    @Override
    public AddingUser doInBackground(Object... loginPair) {
        AddingUser result = null;
        String checkingUserId = (String) loginPair[0];
        activityType = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.dataExchanger.addUser(checkingUserId);
        return result;
    }
    @Override
    public void onPostExecute(AddingUser result){
        if(activityType.equals("NewGroup")) {
            if (result != null) {
                activeActivityProvider.showCheckUserNewGroupGood(result);
            } else {
                activeActivityProvider.showCheckUserNewGroupBad(null);
            }
        }
        else if(activityType.equals("GroupSettingsActivity")){
            if (result != null) {
                activeActivityProvider.showCheckUserSettingsGood(result);
            } else {
                activeActivityProvider.showCheckUserSettingsBad(null);
            }
        }
    }
}
