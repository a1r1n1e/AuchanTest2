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


public class OfflineDisactivateTask implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;

    public OfflineDisactivateTask(SList list, ActiveActivityProvider provider) {
        this.provider = provider;
        this.list = list;
    }


    @Override
    public void run() {
        try {
            if(list != null) {
                DataBaseTask2 memoryTask = new DataBaseTask2(provider);
                memoryTask.disactivateOfflineList(String.valueOf(list.getId()));

                Handler handler = new Handler(Looper.getMainLooper());
                OfflineDisactivateTaskDE runnable = new OfflineDisactivateTaskDE(list, provider);
                handler.post(runnable);
            }
        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineListogramCreatorTask");
        }
    }
}
