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


public class GroupActiveGetterTask extends AsyncTask<Object, Void, SList[]> {
    private ActiveActivityProvider activeActivityProvider;
    private UserGroup group;

    @Override
    public SList[] doInBackground(Object... loginPair) {
        SList[] result;
        group = (UserGroup) loginPair[0];
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.getGroupActiveData(group);
        return result;
    }

    @Override
    public void onPostExecute(SList[] result) {
        if (result == null || result.length == 0) {
            activeActivityProvider.showGroupActiveListsBad(group.getId());
        } else {
            activeActivityProvider.showGroupActiveListsGood(result, group.getId());
        }
        activeActivityProvider = null;
    }
}
