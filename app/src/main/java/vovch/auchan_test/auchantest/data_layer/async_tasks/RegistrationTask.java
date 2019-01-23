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
 * Created by vovch on 09.01.2018.
 */

public class RegistrationTask extends AsyncTask<Object, Void, String> {
    private ActiveActivityProvider activeActivityProvider;

    @Override
    public String doInBackground(Object... loginPair) {
        String result;
        String userLogin = (String) loginPair[0];
        String userPassword = (String) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        result = activeActivityProvider.userSessionData.registration(userLogin, userPassword);
        return result;
    }

    @Override
    public void onPostExecute(String result) {
        if ( result != null && result.substring(0, 3).equals("200")) {
            activeActivityProvider.goodRegistrationTry(result.substring(3));
        } else {
            if ( result != null && result.substring(0, 3).equals("403")) {
                result = "Username Is Already Used";
            } else {
                result = "Something Went Wrong";
            }
            activeActivityProvider.badRegistrationTry(result);
        }
        activeActivityProvider = null;
    }
}
