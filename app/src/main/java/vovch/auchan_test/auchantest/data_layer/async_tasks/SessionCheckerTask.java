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
 * Created by vovch on 21.02.2018.
 */

public class SessionCheckerTask extends AsyncTask<Object, Void, Integer> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public Integer doInBackground(Object... loginPair) {
        int result = 0;
        activeActivityProvider = (ActiveActivityProvider) loginPair[0];
        if(activeActivityProvider.userSessionData.isSession()){
            result = activeActivityProvider.userSessionData.checkSession();
        }
        return result;
    }

    @Override
    public void onPostExecute(Integer result) {
        if(result == 1){                                                            //if there is bad connection to internet we allow user to log in
            activeActivityProvider.goodSessionCheck();                              //in case he won't be able to do smth through web server with wrong session key
        } else if(result == 0){
            activeActivityProvider.badSessionCheck();
        } else if(result == 5){
            activeActivityProvider.goodSessionCheck();
            activeActivityProvider.activeListsNoInternet();
        }
        activeActivityProvider = null;
    }
}
