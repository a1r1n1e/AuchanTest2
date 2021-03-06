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

public class NewGroupAdderTask extends AsyncTask<Object, Void, UserGroup> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public UserGroup doInBackground(Object... loginPair) {
        UserGroup result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        String groupName = (String) loginPair[0];
        result = activeActivityProvider.dataExchanger.newGroupAdding(groupName);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(result != null){
            activeActivityProvider.showNewGroupAddedGood(result);
        }
        else{
            activeActivityProvider.showNewGroupAddedBad(null);
        }
        activeActivityProvider = null;
    }
}
