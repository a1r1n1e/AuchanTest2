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


public class NetWorkUpdateOneGroupTask implements Runnable {
    private String groupId;
    public ActiveActivityProvider provider;
    public boolean forceWatched;

    public NetWorkUpdateOneGroupTask(String groupId, ActiveActivityProvider provider, boolean forceWatched){
        this.groupId = groupId;
        this.provider = provider;
        this.forceWatched = forceWatched;
    }

    public void setProvider(ActiveActivityProvider provider) {
        this.provider = provider;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void run() {
        try {
            UserGroup result = null;

            WebCall webCall = new WebCall();
            JSONArray jsonArray = new JSONArray();
            String jsonString = jsonArray.toString();
            String resultJsonString = webCall.callServer(groupId, DataExchanger.BLANK_WEBCALL_FIELD,
                    DataExchanger.BLANK_WEBCALL_FIELD, "check_group_updates",
                    jsonString, provider.userSessionData);
            if (resultJsonString != null && resultJsonString.length() > 2) {
                if (resultJsonString.substring(0, 3).equals("200")) {
                    result = WebCall.getGroupFromJSONString(resultJsonString.substring(3));

                    if(result != null) {
                        NetWorkUpdateOneGroupDETask runnable = new NetWorkUpdateOneGroupDETask(result, groupId, provider, forceWatched);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(runnable);
                    } else{
                        ErrorUpdateGroupPublisherRunnable runnable = new ErrorUpdateGroupPublisherRunnable(groupId);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(runnable);
                    }
                } else{
                    ErrorUpdateGroupPublisherRunnable runnable = new ErrorUpdateGroupPublisherRunnable(groupId);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(runnable);
                }
            } else{
                ErrorUpdateGroupPublisherRunnable runnable = new ErrorUpdateGroupPublisherRunnable(groupId);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(runnable);
            }

        } catch (Exception e){
            Log.d("WhoBuys", "NetWorkUpdateOneGroupTask");
        }
    }
    public class ErrorUpdateGroupPublisherRunnable implements Runnable{
        private String groupId;

        ErrorUpdateGroupPublisherRunnable(String groupd){
            this.groupId = groupId;
        }

        @Override
        public void run() {
            provider.unsetGroupRefresher(groupId);
        }
    }
}