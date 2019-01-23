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


public class OfflineListogramCreatorTask implements Runnable {
    private ActiveActivityProvider provider;
    private Item[] items;
    private int incomingActivityType;
    private String listName;

    public OfflineListogramCreatorTask(Item[] items, int incomingActivityType, ActiveActivityProvider provider, String listName) {
        this.provider = provider;
        this.items = items;
        this.incomingActivityType = incomingActivityType;
        this.listName = listName;
    }


    @Override
    public void run() {
        try {

            SList list = null;
            DataBaseTask2 addTask = new DataBaseTask2(provider);
            list = addTask.addList(items, "t", listName);

            Handler handler = new Handler(Looper.getMainLooper());
            OfflineListogramCreatorTaskDE runnable = new OfflineListogramCreatorTaskDE(list, incomingActivityType, provider);
            handler.post(runnable);

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineListogramCreatorTask");
        }
    }
}
