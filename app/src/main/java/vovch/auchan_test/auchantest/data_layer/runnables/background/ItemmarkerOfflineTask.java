package vovch.auchan_test.auchantest.data_layer.runnables.background;

import android.os.Handler;
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


public class ItemmarkerOfflineTask implements Runnable {
    private ActiveActivityProvider provider;
    private Item item;

    public ItemmarkerOfflineTask(Item item, ActiveActivityProvider provider){
        this.provider = provider;
        this.item = item;
    }


    @Override
    public void run() {
        try {

            Handler handler = new Handler(Looper.getMainLooper());
            ItemmarkerOfflineTask.ProgressPublisherRunnable showerRunnable = new ItemmarkerOfflineTask.ProgressPublisherRunnable(item);
            handler.post(showerRunnable);

            DataBaseTask2 memoryTask = new DataBaseTask2(provider);
            memoryTask.itemMarkOffline(String.valueOf(item.getId()));

            ItemmarkerOfflineTaskDE runnable = new ItemmarkerOfflineTaskDE(item, provider);
            handler.post(runnable);

        } catch (Exception e){

            Log.d("WhoBuys", "ItemmarkerOnlineTask");

            Handler handler = new Handler(Looper.getMainLooper());
            ItemmarkerOfflineTask.ErrorItemmarkOfflinePublisherRunnable errorRunnable = new ItemmarkerOfflineTask.ErrorItemmarkOfflinePublisherRunnable(item);
            handler.post(errorRunnable);
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
    public class ErrorItemmarkOfflinePublisherRunnable implements Runnable{
        private Item item;

        public ErrorItemmarkOfflinePublisherRunnable(Item item){
            this.item = item;
        }

        @Override
        public void run() {
            provider.showOfflineActiveListsItemmarkedBad(item);
        }
    }
}
