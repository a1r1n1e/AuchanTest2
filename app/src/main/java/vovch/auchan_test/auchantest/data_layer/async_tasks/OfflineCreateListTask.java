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
 * Created by vovch on 14.12.2017.
 */

public class OfflineCreateListTask extends AsyncTask<Object, Void, SList> {
    private ActiveActivityProvider activeActivityProvider;
    private int incomingActivityType;

    @Override
    public SList doInBackground(Object... loginPair){
        Item[] items = (Item[]) loginPair[0];
        incomingActivityType = (Integer) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        SList list = null;

        //list = activeActivityProvider.dataExchanger.addOfflineList(items);

        return list;
    }
    @Override
    public void onPostExecute(SList result){
        if(result != null){
            activeActivityProvider.showOfflineListCreatedGood(result, incomingActivityType);
        }
        else{
            activeActivityProvider.showOfflineListCreatedBad(null, incomingActivityType);
        }
        activeActivityProvider = null;
    }
}
