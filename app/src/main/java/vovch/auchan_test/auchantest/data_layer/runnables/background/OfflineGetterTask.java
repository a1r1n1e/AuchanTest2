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


public class OfflineGetterTask implements Runnable {
    private ActiveActivityProvider provider;

    public OfflineGetterTask(ActiveActivityProvider provider) {
        this.provider = provider;
    }


    @Override
    public void run() {
        try {

            SList[] activeLists = null;
            DataBaseTask2 task1 = new DataBaseTask2(provider);
            activeLists = task1.getOffline(1);

            SList[] historyLists = null;
            DataBaseTask2 task2 = new DataBaseTask2(provider);
            historyLists = task2.getOffline(0);

            Handler handler = new Handler(Looper.getMainLooper());
            OfflineGetterTaskDE runnable = new OfflineGetterTaskDE(activeLists, historyLists, provider);
            handler.post(runnable);

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTask");
        }
    }
}
