package vovch.auchan_test.auchantest.data_layer.runnables.uilayer;

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


public class RedactOfflineListogramTaskDE implements Runnable {
    private ActiveActivityProvider provider;
    private SList list;
    protected Item[] items;
    private int activityType;

    public RedactOfflineListogramTaskDE(SList list, int activityType, Item[] items, ActiveActivityProvider provider) {
        this.provider = provider;
        this.list = list;
        this.items = items;
        this.activityType = activityType;
    }


    @Override
    public void run() {
        try {

            if(list != null && items != null) {
                provider.dataExchanger.redactOfflineList(list);
            }

            if (list != null) {
                provider.showOfflineListRedactedGood(activityType);
            } else {
                provider.showOfflineListRedactedBad(activityType);
            }

        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");

            provider.showOfflineListRedactedBad(activityType);

        }
    }
}
