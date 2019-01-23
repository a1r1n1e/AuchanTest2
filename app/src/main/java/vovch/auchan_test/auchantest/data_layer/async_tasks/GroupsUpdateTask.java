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


public class GroupsUpdateTask extends AsyncTask<Object, ListInformer[], ListInformer[]> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public ListInformer[] doInBackground(Object... loginPair) {
        UserGroup[] result;
        ListInformer[] informers;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];

        result = activeActivityProvider.dataExchanger.getGroupsFromHardMemory();
        informers = activeActivityProvider.dataExchanger.createListinformers();

        publishProgress(informers);

        result = activeActivityProvider.dataExchanger.updateGroups();
        informers = activeActivityProvider.dataExchanger.createListinformers();

        return informers;
    }

    @Override
    protected void onProgressUpdate(ListInformer[]... progress) {
        super.onProgressUpdate();
        presenter(progress[0], false);
    }

    @Override
    public void onPostExecute(ListInformer[] result) {
        presenter(result, true);
    }

    private void presenter(ListInformer[] result, boolean isUpdateComplete){
        if (result == null) {
            if (activeActivityProvider.userSessionData.isLoginned()) {
                activeActivityProvider.showListInformersGottenBad();
            } else {
                activeActivityProvider.badLoginTry(activeActivityProvider.getString(R.string.log_yourself_in));
            }
        } else {
            activeActivityProvider.showListInformersGottenGood(result, isUpdateComplete);
        }
    }
}
