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


public class ItemmarkerOfflineTaskDE implements Runnable {
    public Item item;
    public ActiveActivityProvider provider;

    public ItemmarkerOfflineTaskDE(Item item, ActiveActivityProvider provider){
        this.item = item;
        this.provider = provider;
    }

    @Override
    public void run() {
        try{
            if (item != null) {
                provider.dataExchanger.itemmarkOfflineNew(item);
                provider.showOfflineActiveListsItemmarkedGood(item);
            }
        } catch (Exception e){
            Log.d("WhoBuys", "ItemmarkerOnlineTaskDE");
        }
    }
}
