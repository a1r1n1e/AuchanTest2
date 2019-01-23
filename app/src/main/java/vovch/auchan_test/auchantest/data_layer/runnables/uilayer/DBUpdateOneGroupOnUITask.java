package vovch.auchan_test.auchantest.data_layer.runnables.uilayer;

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


public class DBUpdateOneGroupOnUITask implements Runnable {
    private UserGroup result;
    private ActiveActivityProvider provider;
    private static final String DEFAULT_NOTIFICATION_MESSAGE = "Something new";

    public DBUpdateOneGroupOnUITask(UserGroup group, ActiveActivityProvider provider){
        this.result = group;
        this.provider = provider;
    }

    @Override
    public void run() {
        if(result != null){
            provider.showGroupChangeOutside(result);

            NotificationsTask notificationsTask = new NotificationsTask(provider, DEFAULT_NOTIFICATION_MESSAGE, false);
            provider.executor.execute(notificationsTask);

        }
    }
}