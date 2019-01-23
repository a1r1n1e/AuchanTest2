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


public class NetWorkUpdateOneGroupDETask implements Runnable {
    public String groupId;
    public UserGroup group;
    public ActiveActivityProvider provider;
    public boolean forceWatched;

    public NetWorkUpdateOneGroupDETask(UserGroup group, String groupId, ActiveActivityProvider provider, boolean forceWatched){
        this.group = group;
        this.groupId = groupId;
        this.provider = provider;
        this.forceWatched = forceWatched;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public void setProvider(ActiveActivityProvider provider) {
        this.provider = provider;
    }

    @Override
    public void run() {
        provider.dataExchanger.updateOneGroupDataNew(groupId, group, forceWatched);
    }
}