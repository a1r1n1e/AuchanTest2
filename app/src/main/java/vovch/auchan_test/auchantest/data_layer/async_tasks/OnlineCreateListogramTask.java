package vovch.auchan_test.auchantest.data_layer.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

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
 * Created by vovch on 03.01.2018.
 */

public class OnlineCreateListogramTask extends AsyncTask <Object, Void, UserGroup>{
    private ActiveActivityProvider activeActivityProvider;
    private int activityType;

    @Override
    public UserGroup doInBackground(Object... loginPair){
        UserGroup result;
        Item[] items = (Item[]) loginPair[0];
        UserGroup group = (UserGroup) loginPair[1];
        activeActivityProvider = (ActiveActivityProvider) loginPair[2];
        activityType = (Integer) loginPair[3];
        String listName = (String) loginPair[4];

        try {
            TelephonyManager tMgr = (TelephonyManager) activeActivityProvider.getSystemService(Context.TELEPHONY_SERVICE);
            if(tMgr != null) {
                String mPhoneNumber = tMgr.getDeviceId();
                if (!activeActivityProvider.userSessionData.isSession()) {
                    activeActivityProvider.userSessionData.register(mPhoneNumber);
                    UserGroup[] temp = activeActivityProvider.dataExchanger.updateGroups();
                    activeActivityProvider.setActiveGroup(temp[0]);
                }
            }
        } catch (SecurityException e){
            Log.d("auchan_test", "phone permission");
        }

        result = activeActivityProvider.dataExchanger.addOnlineList(items, activeActivityProvider.getActiveGroup(), listName);
        return result;
    }
    @Override
    public void onPostExecute(UserGroup result){
        if(activityType == 6) {
            if (result != null) {
                activeActivityProvider.showOnlineListogramCreatedGood(result);
                activeActivityProvider.dataExchanger.clearTempItems();
            } else {
                activeActivityProvider.showOnlineListogramCreatedBad();
            }
        } else if(activityType == 4){
            if(result != null){
                activeActivityProvider.resendListToGroupGood(result);
            }
            else{
                activeActivityProvider.resendListToGroupBad(null);
            }
        }
        activeActivityProvider = null;
    }
}
