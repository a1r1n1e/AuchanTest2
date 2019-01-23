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

public class OfflineItemmarkTask extends AsyncTask <Object, Void, Item>{
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Item doInBackground(Object... loginPair){
        Item result;
        activeActivityProvider = (ActiveActivityProvider) loginPair[1];
        result = activeActivityProvider.dataExchanger.itemmarkOffline((Item) loginPair[0]);
        return result;
    }
    @Override
    public void onPostExecute(Item item){
        if (item != null) {
            activeActivityProvider.showOfflineActiveListsItemmarkedGood(item);
        } else {
            activeActivityProvider.showOfflineActiveListsItemmarkedBad(null);
        }
        activeActivityProvider = null;
    }
}

