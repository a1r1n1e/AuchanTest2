package vovch.auchan_test.auchantest.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;

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


public class GroupsUpdaterTask implements Runnable {
    private ActiveActivityProvider provider;
    private static final String DEFAULT_CHECK_NEEDED_STRING = "Something new";

    public GroupsUpdaterTask(ActiveActivityProvider provider){
        this.provider = provider;
    }

    @Override
    public void run() {
        try {
            UserGroup[] groups;
            UserGroup[] webGroups;
            DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
            groups = dataBaseTask2.getGroups();

            ListInformersCreatorTask runnable = new ListInformersCreatorTask(groups, provider, false);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);

            UserGroup[] result = null;

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();

            String resultJsonString = webCall.callServer( provider.userSessionData.getId(), DataExchanger.BLANK_WEBCALL_FIELD,
                                                                    DataExchanger.BLANK_WEBCALL_FIELD, "update_groups",
                                                                    jsonString, provider.userSessionData);

            if(resultJsonString != null && resultJsonString.length() > 2){
                if(resultJsonString.substring(0, 3).equals("200")) {
                    webGroups = WebCall.getGroupsFromJsonString(resultJsonString.substring(3));

                    if(!UserGroup.arraysEqualByLastUpdateTime(groups, webGroups)) {
                        result = dataBaseTask2.resetGroups(webGroups);

                        NotificationsTask notificationsTask = new NotificationsTask(provider, DEFAULT_CHECK_NEEDED_STRING, false);
                        handler.post(notificationsTask);

                    } else{
                        result = groups;
                    }
                } else if(resultJsonString.substring(0, 3).equals("502")) {
                    provider.userSessionData.setNotLoggedIn();
                } else {
                    result = groups;
                }
            }

            ListInformersCreatorTask task = new ListInformersCreatorTask(result, provider, true);
            handler.post(task);

        } catch (Exception e){
            Log.d("WhoBuys", "GroupsUpdaterTask");
        }
    }
}
