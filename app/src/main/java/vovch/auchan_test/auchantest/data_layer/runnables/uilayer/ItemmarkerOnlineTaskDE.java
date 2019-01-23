package vovch.auchan_test.auchantest.data_layer.runnables.uilayer;

import android.os.Looper;
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


public class ItemmarkerOnlineTaskDE implements Runnable {
    public Item item;
    public UserGroup group;
    public ActiveActivityProvider provider;
    private String resultString;

    public ItemmarkerOnlineTaskDE(UserGroup group, Item item, ActiveActivityProvider provider, String resultString){
        this.group = group;
        this.item = item;
        this.provider = provider;
        this.resultString = resultString;
    }

    @Override
    public void run() {
        try{
            if (resultString.substring(0, 3).equals("205")) {
                provider.dataExchanger.itemmarkOnlineOff(item, resultString, group);
            } else if (resultString.substring(0, 3).equals("200")) {
                provider.dataExchanger.itemmarkOnlineOn(item, resultString, group);
            } else{

                android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                ErrorItemmarkOnlinePublisherRunnable showerRunnable = new ErrorItemmarkOnlinePublisherRunnable(group);
                handler.post(showerRunnable);

            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTaskDE");
        }
    }
    public class ErrorItemmarkOnlinePublisherRunnable implements Runnable{
        private  UserGroup group;

        ErrorItemmarkOnlinePublisherRunnable(UserGroup group){
            this.group = group;
        }

        @Override
        public void run() {
            provider.showOnlineItemmarkedBad(group);
        }
    }
}
