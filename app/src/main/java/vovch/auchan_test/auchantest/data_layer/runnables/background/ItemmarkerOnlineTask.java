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


public class ItemmarkerOnlineTask implements Runnable {
    private UserGroup group;
    private ActiveActivityProvider provider;
    private Item item;

    public ItemmarkerOnlineTask(ActiveActivityProvider provider, Item item){
        this.provider = provider;
        this.item = item;
    }


    @Override
    public void run() {
        try {

            Handler handler = new Handler(Looper.getMainLooper());

            group = provider.getActiveGroup();
            if (item != null){

                ProgressPublisherRunnable showerRunnable = new ProgressPublisherRunnable(item);
                handler.post(showerRunnable);

                if (item != null) {

                    WebCall webCall = new WebCall();
                    String userId = String.valueOf(provider.userSessionData.getId());
                    String itemId = String.valueOf(item.getId());
                    String listId = String.valueOf(item.getList().getId());
                    String groupId = group.getId();
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(0, userId);
                    jsonArray.put(1, groupId);
                    jsonArray.put(2, listId);
                    jsonArray.put(3, itemId);
                    String jsonString = jsonArray.toString();

                    String resultString = webCall.callServer( userId, DataExchanger.BLANK_WEBCALL_FIELD,
                                                                        DataExchanger.BLANK_WEBCALL_FIELD, "itemmark",
                                                                        jsonString, provider.userSessionData);

                    if (resultString != null && resultString.length() > 2) {

                        ItemmarkerOnlineTaskDE checkerRunnuble = new ItemmarkerOnlineTaskDE(group, item, provider, resultString);
                        handler.post(checkerRunnuble);

                    }
                }
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTask");
        }
    }

    private class ProgressPublisherRunnable implements Runnable{
        private  Item item;

        ProgressPublisherRunnable(Item item){
            this.item = item;
        }

        @Override
        public void run() {
            provider.showItemmarkProcessingToUser(item);
        }
    }
}
