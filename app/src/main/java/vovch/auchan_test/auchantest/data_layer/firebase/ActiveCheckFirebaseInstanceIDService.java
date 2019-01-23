package vovch.auchan_test.auchantest.data_layer.firebase;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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
 * Created by vovch on 30.10.2017.
 */


public class ActiveCheckFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ActiveActivityProvider provider = (ActiveActivityProvider) getApplicationContext();
        provider.userSessionData.setToken(refreshedToken);

        try {
            TelephonyManager tMgr = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
            if(tMgr != null) {
                String mPhoneNumber = tMgr.getDeviceId();

                String result = provider.userSessionData.startSession(mPhoneNumber, mPhoneNumber);
                if (result != null && result.length() >= 3) {
                    if (!result.substring(0, 3).equals("200")) {
                        provider.userSessionData.register(mPhoneNumber);
                    }
                    provider.updateAllGroups();
                }
            }
        } catch (SecurityException e){
            Log.d("auchan_test", "phone permission");
        }
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        //provider.userSessionData.registerForPushes();
    }
}
