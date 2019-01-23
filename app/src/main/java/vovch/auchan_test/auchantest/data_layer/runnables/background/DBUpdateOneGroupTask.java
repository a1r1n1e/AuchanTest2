package vovch.auchan_test.auchantest.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;

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


public class DBUpdateOneGroupTask implements Runnable {
    private UserGroup group;
    public ActiveActivityProvider provider;

    public DBUpdateOneGroupTask(UserGroup group, ActiveActivityProvider provider){
        this.provider = provider;
        this.group = group;
    }

    @Override
    public void run() {
        UserGroup result = null;
        DataBaseTask2 dataBaseTask2 = new DataBaseTask2(provider);
        result = dataBaseTask2.addGroup(group);

        DBUpdateOneGroupOnUITask runnable = new DBUpdateOneGroupOnUITask(result, provider);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
