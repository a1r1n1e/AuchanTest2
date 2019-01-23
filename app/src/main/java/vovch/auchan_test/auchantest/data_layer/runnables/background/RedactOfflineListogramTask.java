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


public class RedactOfflineListogramTask implements Runnable {
    private ActiveActivityProvider provider;
    private int activityType;
    private Item[] items;
    private SList list;
    private String listName;

    public RedactOfflineListogramTask(SList list, int activityType, Item[] items, ActiveActivityProvider provider, String listName) {
        this.provider = provider;
        this.list = list;
        this.items = items;
        this.activityType = activityType;
        this.listName = listName;
    }


    @Override
    public void run() {
        try {
            SList resultList = null;
            if (list != null && items != null) {
                DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
                resultList = dataBaseTask2.redactOfflineList(list, items, listName);

                Handler handler = new Handler(Looper.getMainLooper());
                RedactOfflineListogramTaskDE runnable = new RedactOfflineListogramTaskDE(resultList, activityType, items, provider);
                handler.post(runnable);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "RedactOfflineListogramTask");
        }
    }
}
