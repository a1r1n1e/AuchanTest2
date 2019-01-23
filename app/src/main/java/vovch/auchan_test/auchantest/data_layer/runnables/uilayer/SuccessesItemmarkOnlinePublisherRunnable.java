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


public class SuccessesItemmarkOnlinePublisherRunnable implements Runnable{
    private UserGroup group;
    private  UserGroup result;
    private ActiveActivityProvider provider;
    private Item item;

    public SuccessesItemmarkOnlinePublisherRunnable(UserGroup group, UserGroup result, ActiveActivityProvider provider, Item item){
        this.group = group;
        this.result = result;
        this.provider = provider;
        this.item = item;
    }

    @Override
    public void run() {
        if(result != null){
            if(result.equals(group)){
                provider.showOfflineActiveListsItemmarkedGood(item);
            }
        }
        else{
            provider.showOfflineActiveListsItemmarkedBad(item);
        }
    }
}