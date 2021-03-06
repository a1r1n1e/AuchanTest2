package vovch.auchan_test.auchantest;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.transition.Slide;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import vovch.auchan_test.auchantest.activities.complex.ActiveListsActivity;
import vovch.auchan_test.auchantest.activities.complex.Group2Activity;
import vovch.auchan_test.auchantest.activities.simple.CreateListogramActivity;
import vovch.auchan_test.auchantest.activities.simple.GroupList2Activity;
import vovch.auchan_test.auchantest.activities.simple.GroupSettingsActivity;
import vovch.auchan_test.auchantest.activities.simple.NewGroup;
import vovch.auchan_test.auchantest.activities.simple.SendBugActivity;
import vovch.auchan_test.auchantest.data_layer.DataExchanger;
import vovch.auchan_test.auchantest.data_layer.UserSessionData;
import vovch.auchan_test.auchantest.data_layer.async_tasks.AddUserTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.DropHistoryTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.GroupChangeConfirmTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.GroupHistoryGetterTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.GroupLeaverTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.GroupStateSetWatchedTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.GroupsGetterTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.LoginnerTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.NewGroupAdderTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.OnlineCreateListogramTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.OnlineDisactivateTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.RedactOnlineListTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.RegistrationTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.RemoveAddedUserTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.ResendListToGroupTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.SendBugTask;
import vovch.auchan_test.auchantest.data_layer.async_tasks.SessionCheckerTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.GroupsUpdaterTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.ItemmarkerOfflineTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.ItemmarkerOnlineTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.NetWorkUpdateOneGroupTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.OfflineDisactivateTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.OfflineGetterTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.OfflineListogramCreatorTask;
import vovch.auchan_test.auchantest.data_layer.runnables.background.RedactOfflineListogramTask;
import vovch.auchan_test.auchantest.data_types.AddingUser;
import vovch.auchan_test.auchantest.data_types.Item;
import vovch.auchan_test.auchantest.data_types.ListInformer;
import vovch.auchan_test.auchantest.data_types.SList;
import vovch.auchan_test.auchantest.data_types.TempItem;
import vovch.auchan_test.auchantest.data_types.UserGroup;

/**
 * Created by vovch on 09.11.2017.
 */

public class ActiveActivityProvider extends Application {

    private Context activeActivity;
    private int activeActivityNumber;
    private int activeListsActivityLoadType;
    private UserGroup activeGroup;
    private SList list;
    public DataExchanger dataExchanger;
    public UserSessionData userSessionData;
    public int debugger;
    public Executor executor;

    @Override
    public void onCreate() {
        super.onCreate();
        debugger = 0;
        activeListsActivityLoadType = 1;
        Log.w("MY", "onCreate WhoBuys ActiveActivityProvider");
        dataExchanger = DataExchanger.getInstance(ActiveActivityProvider.this);
        userSessionData = UserSessionData.getInstance(ActiveActivityProvider.this);
        nullActiveActivity();

        executor = Executors.newFixedThreadPool(4);

        updateAllGroups();

        startOfflineGetterDatabaseTask();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("HelveticaNeueCyr-Roman.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public Context getActiveActivity() {
        return activeActivity;
    }

    public void setActiveActivity(int whichOne, Context context) {
        activeActivity = context;
        activeActivityNumber = whichOne;
    }

    public int getActiveListsActivityLoadType(){
        return activeListsActivityLoadType;
    }

    public void setActiveListsActivityLoadType(int newType){
        activeListsActivityLoadType = newType;
    }

    public void setActiveGroup(UserGroup newActiveGroup) {
        activeGroup = newActiveGroup;
    }

    public UserGroup getActiveGroup() {
        return activeGroup;
    }

    public void nullActiveActivity() {
        activeActivity = null;
        activeActivityNumber = -1;
    }

    public int getActiveActivityNumber() {
        return activeActivityNumber;
    }

    public void setResendingList(SList newList){
        list = newList;
    }

    public SList getResendingList(){
        return list;
    }

    public void nullResendingList(){
        list = null;
    }

    public boolean isAnyResendingList(){
        boolean result = false;
        if(list != null){
            result = true;
        }
        return result;
    }

    public void showResultInAuchanBase(TempItem[] result, TempItem addTo){
        if(getActiveActivityNumber() == 6 && getActiveActivity() != null){
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showVariants(result, addTo);
         }
    }

    public void updateAllGroups(){

        if(getActiveActivityNumber() == 2 && getActiveActivity() != null){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.setActiveListsOnlineRefresher();
        }

        GroupsUpdaterTask worker = new GroupsUpdaterTask(ActiveActivityProvider.this);
        executor.execute(worker);
    }

    public void tryToLoginFromPrefs() {
        boolean prefCheck = false;
        boolean internetCheck = false;
        if (userSessionData.isAnyPrefsData()) {
            prefCheck = true;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                internetCheck = true;
            }
            if (prefCheck && internetCheck) {
                checkSessionWeb();
            } else if (internetCheck) {
                badLoginTry(getResources().getString(R.string.log_yourself_in));
            } else {
                activeListsNoInternet();
            }
        }
    }

    public void showExitGood(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.activeToLoginFragmentChange();
        }
    }

    public void showExitBad(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();                   //TODO
        }
    }

    public void activeListsNoInternet() {
        /*if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.noInternet();
        }*/
    }

    public void tryToLoginFromForms(String login, String password) {
        boolean internetCheck = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                internetCheck = true;
            }
            if (internetCheck) {
                tryToLoginWeb(login, password);
            } else {
                activeListsNoInternet();
            }
        }
    }

    public void tryToLoginWeb(String login, String password) {
        LoginnerTask loginnerTask = new LoginnerTask();
        loginnerTask.execute(login, password, ActiveActivityProvider.this);
    }

    public void  checkSessionWeb(){
        SessionCheckerTask sessionCheckerTask = new SessionCheckerTask();
        sessionCheckerTask.execute(ActiveActivityProvider.this);
    }

    public void goodSessionCheck(){
        goodLoginTry(null);
    }

    public void badSessionCheck(){
        badLoginTry(getString(R.string.some_error));
    }

    public void badLoginTry(String result) {
        /*if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.onLoginFailed(result);
        }*/
    }

    public void goodLoginTry(String result) {
        if (getActiveActivityNumber() == 2) {

            updateAllGroups();

            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.loginToActiveFragmentChange();
        }
    }

    public void registrationTry(String login, String password) {
        RegistrationTask registrationTask = new RegistrationTask();
        registrationTask.execute(login, password, ActiveActivityProvider.this);
    }

    public void goodRegistrationTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.registrationToActiveListsOnlineFragmentChange();
        }
    }

    public void badRegistrationTry(String result) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.badRegistrationTry(result);
        }
    }

    public void dropHistory(){
        DropHistoryTask dropHistoryTask = new DropHistoryTask();
        dropHistoryTask.execute(ActiveActivityProvider.this);
    }

    public void showDropHistoryGood(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDropHistoryGood();
        }
    }

    public void showDropHistoryBad(){
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDropHistoryBad();
        }
    }

    public void sendBug(String text){
        SendBugTask sendBugTask = new SendBugTask();
        sendBugTask.execute(text, ActiveActivityProvider.this);
    }

    public void showSendBugGood(){
        if (getActiveActivityNumber() == 8) {
            SendBugActivity activity = (SendBugActivity) getActiveActivity();
            activity.showGood();
        }
    }

    public void showSendBugBad(){
        if (getActiveActivityNumber() == 8) {
            SendBugActivity activity = (SendBugActivity) getActiveActivity();
            activity.showBad();
        }
    }

    public void startOfflineGetterDatabaseTask() {

        OfflineGetterTask runnable = new OfflineGetterTask(ActiveActivityProvider.this);
        executor.execute(runnable);

        //NewDataBaseTask newDataBaseTask = new NewDataBaseTask();
        //newDataBaseTask.execute(type, ActiveActivityProvider.this);
    }

    public void getOfflineActiveData(){
        //SList[] activeLists = dataExchanger.getOfflineActiveData();
        if(getActiveGroup() != null) {
            SList[] activeLists = dataExchanger.getGroupActiveData(getActiveGroup());
            if (activeLists == null || activeLists.length == 0) {
                showOfflineActiveListsBad(activeLists);
            } else {
                showOfflineActiveListsGood(activeLists);
            }
        }
    }

    public void getOfflineHistoryData(){
        if(getActiveGroup() != null) {
            SList[] historyLists = dataExchanger.getGroupHistoryData(getActiveGroup());
            if (historyLists == null || historyLists.length == 0) {
                showOfflineHistoryListsBad(historyLists);
            } else {
                showOfflineHistoryListsGood(historyLists);
            }
        }
    }

    public void activeActivityDisactivateList(SList list) {

        OfflineDisactivateTask runnable = new OfflineDisactivateTask(list, ActiveActivityProvider.this);
        executor.execute(runnable);

        //DisactivateOfflineTask disactivateOfflineTask = new DisactivateOfflineTask();
        //disactivateOfflineTask.execute(list, ActiveActivityProvider.this);
    }

    public void activeListsItemmark(Item item) {

        ItemmarkerOnlineTask runnable = new ItemmarkerOnlineTask(ActiveActivityProvider.this, item);
        executor.execute(runnable);

        //ItemmarkerOfflineTask runnable = new ItemmarkerOfflineTask(item, ActiveActivityProvider.this);
        //executor.execute(runnable);

        //OfflineItemmarkTask offlineItemmarkTask = new OfflineItemmarkTask();
        //offlineItemmarkTask.execute(item, ActiveActivityProvider.this);
    }

    public void createListogramOffline(Item[] items, String name) {

        OfflineListogramCreatorTask runnable = new OfflineListogramCreatorTask(items, getActiveActivityNumber(), ActiveActivityProvider.this, name);
        executor.execute(runnable);

        //OfflineCreateListTask offlineCreateListTask = new OfflineCreateListTask();
        //offlineCreateListTask.execute(items, getActiveActivityNumber(), ActiveActivityProvider.this);
    }

    public void createListogram(Item[] items, String listName){
        if(getActiveGroup() != null) {
            OnlineCreateListogramTask onlineCreateListogramTask = new OnlineCreateListogramTask();
            onlineCreateListogramTask.execute(items, getActiveGroup(), ActiveActivityProvider.this, getActiveActivityNumber(), listName);
        }
    }

    public void getActiveActivityActiveLists() {
        if(userSessionData.isLoginned()) {
            //GroupsUpdateTask groupsUpdateTask = new GroupsUpdateTask();
            //groupsUpdateTask.execute(ActiveActivityProvider.this);

            //GroupsUpdaterTask worker = new GroupsUpdaterTask(ActiveActivityProvider.this);
            //executor.execute(worker);

            UserGroup[] groupsInDataStorage = dataExchanger.getGroupsFromRAM();
            if(groupsInDataStorage != null && groupsInDataStorage.length > 0){                          //TODO ZERO listinformers
                ListInformer[] informers = dataExchanger.createListinformers();
                if (informers == null) {
                    if (userSessionData.isLoginned()) {
                        showListInformersGottenBad();
                    } else {
                        badLoginTry(getString(R.string.log_yourself_in));
                    }
                } else {
                    showListInformersGottenGood(informers, false);
                }
            }
        } else {
            if(getActiveActivityNumber() == 2){
                ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
                activity.activeToLoginFragmentChange();
            }
        }
    }

    public void showListInformersGottenGood(ListInformer[] result, boolean isUpdateComplete) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showGood(result, isUpdateComplete);
        }
    }

    public void showListInformersGottenBad() {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showBad(dataExchanger.getListInformersRAM());
            //activeListsNoInternet();                                                                  //TODO can be different reasons
        }
    }


    public void confirmGroupSettingsChange(UserGroup changedGroup, String newGroupName) {
        GroupChangeConfirmTask groupChangeConfirmTask = new GroupChangeConfirmTask();
        groupChangeConfirmTask.execute(changedGroup, newGroupName, ActiveActivityProvider.this);
    }

    public void showGroupSettingsChangeGood(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmGood(result);
        }
    }

    public void showGroupSettingsChangeBad(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.confirmBad(result);
        }
    }

    public void leaveGroup() {
        UserGroup group = getActiveGroup();
        GroupLeaverTask groupLeaverTask = new GroupLeaverTask();
        groupLeaverTask.execute(group, ActiveActivityProvider.this);

    }

    public void leaveGroupGood(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupGood(result);
        }
    }

    public void leaveGroupBad(UserGroup result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.leaveGroupBad(result);
        }
    }


    public void addNewGroup(String groupName) {
        NewGroupAdderTask newGroupAdderTask = new NewGroupAdderTask();
        newGroupAdderTask.execute(groupName,ActiveActivityProvider.this);
    }

    public AddingUser[] getPossibleMembers() {
        return dataExchanger.getDeletableUsers();
    }

    public void makeAllMembersPossible(UserGroup group) {
        clearNewGroupPossibleMembers();
        AddingUser[] users = null;
        if (getActiveGroup() != null) {
            users = dataExchanger.makeAllUsersPossible(group);
        }
    }

    public void clearNewGroupPossibleMembers() {
        dataExchanger.clearDeletableUsers();
    }

    public void clearAddedUsers(){
        dataExchanger.clearAddedUsers();
    }

    public void showNewGroupAddedGood(UserGroup result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showGood(result);
        }
    }

    public void showNewGroupAddedBad(UserGroup result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showBad(result);
        }
    }

    public boolean checkUser(String id) {
        boolean result = false;
        result = dataExchanger.checkUserRAM(id);
        return  result;
    }

    public void addYourselfToGroup(){
        dataExchanger.addYourselfLocal();
    }

    public void addUserToGroup(String userId, String activityType) {
        AddUserTask addUserTask = new AddUserTask();
        addUserTask.execute(userId, activityType, ActiveActivityProvider.this);
    }

    public void removeAddedUser(AddingUser user) {
        RemoveAddedUserTask removeAddedUserTask = new RemoveAddedUserTask();
        removeAddedUserTask.execute(user, getActiveActivityNumber(), ActiveActivityProvider.this);
    }

    public void showRemoveAddedUserNewGroupGood(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }

    public void showRemoveAddedUserNewGroupBad(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }

    public void showCheckUserNewGroupGood(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }

    public void showCheckUserNewGroupBad(AddingUser result) {
        if (getActiveActivityNumber() == 5) {
            NewGroup activity = (NewGroup) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }

    public void showCheckUserSettingsGood(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckGood(result);
        }
    }

    public void showCheckUserSettingsBad(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showUserCheckBad(result);
        }
    }

    public void showRemoveAddedUserSettingsGood(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserGood(result);
        }
    }

    public void showRemoveAddedUserSettingsBad(AddingUser result) {
        if (getActiveActivityNumber() == 7) {
            GroupSettingsActivity activity = (GroupSettingsActivity) getActiveActivity();
            activity.showRemoveUserBad(result);
        }
    }


    public void getGroupHistoryLists(UserGroup group) {
        GroupHistoryGetterTask groupHistoryGetterTask = new GroupHistoryGetterTask();
        groupHistoryGetterTask.execute(group, ActiveActivityProvider.this);
    }

    public void getGroupActiveLists(UserGroup group) {
        SList[] lists = dataExchanger.getGroupActiveData(group);
        if (lists == null || lists.length == 0) {
            showGroupActiveListsBad(group.getId());
        } else {
            showGroupActiveListsGood(lists, group.getId());
        }
    }

    public void unsetGroupRefresher(String groupId){                                                    //shitcode
       if(getActiveActivityNumber() == 3 && getActiveGroup().getId().equals(groupId)){
           Group2Activity activity = (Group2Activity) getActiveActivity();
           activity.unsetActiveListsRefresher();
       }
    }

    public void disactivateGroupList(SList list) {
        OnlineDisactivateTask onlineDisactivateTask = new OnlineDisactivateTask();
        onlineDisactivateTask.execute(list, ActiveActivityProvider.this);
    }

    public void itemmark(Item item) {
        //OnlineItemmarkTask onlineItemmarkTask = new OnlineItemmarkTask();
        //onlineItemmarkTask.execute(item, ActiveActivityProvider.this);

        ItemmarkerOnlineTask runnable = new ItemmarkerOnlineTask(ActiveActivityProvider.this, item);
        executor.execute(runnable);
    }


    public void createOnlineListogram(UserGroup group, Item[] items, String listName) {
        OnlineCreateListogramTask onlineCreateListogramTask = new OnlineCreateListogramTask();
        onlineCreateListogramTask.execute(items, group, ActiveActivityProvider.this, getActiveActivityNumber(), listName);
    }


    public void redactListogram(Item[] items, SList redactingList, String listName){
        RedactOnlineListTask redactOnlineListTask = new RedactOnlineListTask();
        redactOnlineListTask.execute(redactingList, items, getActiveActivityNumber(), ActiveActivityProvider.this, listName);
    }

    public void redactOnlineListogram(Item[] items, SList redactingList, String listName){
        RedactOnlineListTask redactOnlineListTask = new RedactOnlineListTask();
        redactOnlineListTask.execute(redactingList, items, getActiveActivityNumber(), ActiveActivityProvider.this, listName);
    }

    public void showOnlineListRedactedGood(int incomingActivity, UserGroup group){
        if (getActiveActivityNumber() == 6 && incomingActivity == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOnlineGood(group);
        }
    }

    public void showOnlineListRedactedBad(SList resultList, int activity){
        if(getActiveActivity() != null) {
            Toast.makeText(getActiveActivity(), getString(R.string.some_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void setGroupStateToWatched(UserGroup group){
        GroupStateSetWatchedTask groupStateChangerTask = new GroupStateSetWatchedTask();
        groupStateChangerTask.execute(group, ActiveActivityProvider.this);
    }

    public void redactOfflineListogram(Item[] items, SList redactingList, String listName){

        RedactOfflineListogramTask runnable = new RedactOfflineListogramTask(list, getActiveActivityNumber(), items, ActiveActivityProvider.this, listName);
        executor.execute(runnable);

        //RedactOfflineListTask redactOfflineListTask = new RedactOfflineListTask();
        //redactOfflineListTask.execute(redactingList, items, getActiveActivityNumber(), ActiveActivityProvider.this);
    }

    public void showOfflineListRedactedGood(int incomingActivity){
        if (getActiveActivityNumber() == 6 && incomingActivity == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineGood();
        }
    }

    public void showOfflineListRedactedBad(int activity){
        if(getActiveActivity() != null) {
            Toast.makeText(getActiveActivity(), getString(R.string.some_error), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void saveTempItems(TempItem[] tempItems) {
        dataExchanger.saveTempItems(tempItems);
    }

    public TempItem[] getTempItems() {
        TempItem[] result;
        result = dataExchanger.getTempItems();
        return result;
    }


    public void getGroups() {
        GroupsGetterTask groupsGetterTask = new GroupsGetterTask();
        groupsGetterTask.execute(ActiveActivityProvider.this);
    }

    public void showGroupsGottenGood(UserGroup[] result) {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showGood(result);
        }
    }

    public void showGroupsGottenBad() {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.showBad(dataExchanger.getGroupsFromRAM());
        }
    }

    public void resendListToGroup(SList resendingList, UserGroup group){


        ResendListToGroupTask resendListToGroupTask = new ResendListToGroupTask();
        resendListToGroupTask.execute(resendingList, group, ActiveActivityProvider.this);
    }

    public void resendListToGroupGood(UserGroup result){
        if(getActiveActivityNumber() == 4){
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
            nullResendingList();
        }
    }
    public void resendListToGroupBad(UserGroup result){

    }                                           //TODO


    public void showTouchedGroupGoingGood(UserGroup result) {
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.goToGroup(result);
        }
    }

    public void showTouchedGroupGoingBad(UserGroup result) {                                          //TODO
        if (getActiveActivityNumber() == 4) {
            GroupList2Activity activity = (GroupList2Activity) getActiveActivity();
            activity.update();
        }
    }


    public void showOnlineItemmarkedGood(UserGroup group) {
        if (getActiveActivityNumber() == 3 && group != null) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(group.getId())) {
                setActiveGroup(group);
                activity.showGood(group.getActiveLists());
                //activity.historyLoadOnGood(group.getHistoryLists());
            }
        }
    }

    public void showOnlineItemmarkedGoodLight(Item item) {
        if (getActiveActivityNumber() == 3 && item != null) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(item.getList().getGroup().getId())) {
                activity.showThirdGood(item);
            }
        }
    }

    public void showOnlineItemmarkedBad(UserGroup group) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(group.getId())) {
                //setActiveGroup(group);
                activity.showGood(group.getActiveLists());
                //activity.historyLoadOnGood(group.getHistoryLists());
            }
        }
    }

    public void showItemmarkProcessingToUser(Item item) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(item.getList().getGroup().getId())) {
                activity.showItemmarkProcessing(item);
            }
        } else if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkProcessing(item);
        }
    }


    public void showOnlineDisactivateListGood(UserGroup result) {
        if (getActiveActivityNumber() == 3 && result != null) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(result.getId())) {
                activity.showGood(result.getActiveLists());
                activity.historyLoadOnGood(result.getHistoryLists());
                //if (!dataExchanger.checkGroupActiveData(getActiveGroup())) {
                //    activity.showBad(new SList[0]);
                //}
            }
        }
    }

    public void showOnlineDisactivateListBad(SList result) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(result.getGroup().getId())) {
                activity.showSecondBad(result);
            }
        }
    }


    public void showOnlineListogramCreatedGood(UserGroup group) {
        if (getActiveActivityNumber() == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineGood();
        }
    }

    public void showOnlineListogramCreatedBad() {
        if (getActiveActivityNumber() == 6) {
            CreateListogramActivity activity = (CreateListogramActivity) getActiveActivity();
            activity.showAddListOfflineBad();
        }
    }

    public void updateOneGroup(String groupId){

        NetWorkUpdateOneGroupTask worker = new NetWorkUpdateOneGroupTask(groupId, ActiveActivityProvider.this, false);
        executor.execute(worker);

        //UpdateOneGroupTask updateOneGroupTask = new UpdateOneGroupTask();
        //updateOneGroupTask.execute(ActiveActivityProvider.this, groupId);
    }

    public void showGroupChangeInside(UserGroup group){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(group.getId())){
                setActiveGroup(group);
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.refreshWholeGroup();
            }
        }
    }

    public void showGroupChangeOutside(UserGroup group){
        if(getActiveActivityNumber() == 3){
            if(getActiveGroup().getId().equals(group.getId())){
                setActiveGroup(group);
                Group2Activity activity = (Group2Activity) getActiveActivity();
                activity.refreshWholeGroup();
            }
        } else if(getActiveActivityNumber() == 2){
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.update();
        }
    }

    public void showGroupHistoryListsGood(SList[] lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnGood(lists);
            }
        }
    }

    public void showGroupHistoryListsBad(String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.historyLoadOnBad(dataExchanger.getGroupHistoryDataRAM(getActiveGroup()));
            }
        }
    }

    public void showGroupActiveListsGood(SList[] lists, String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.showGood(lists);
                if(!getActiveGroup().getUpdateNeededFlag()) {
                    getActiveGroup().changeUpdateNeededFlag();
                }
            }
        }
    }

    public void showGroupActiveListsBad(String groupId) {
        if (getActiveActivityNumber() == 3) {
            Group2Activity activity = (Group2Activity) getActiveActivity();
            if (getActiveGroup().getId().equals(groupId)) {
                activity.showBad(dataExchanger.getGroupActiveDataRAM(getActiveGroup()));
                if(!getActiveGroup().getUpdateNeededFlag()) {
                    getActiveGroup().changeUpdateNeededFlag();
                }
            }
        }
    }


    public void showOfflineListCreatedGood(SList list, int activity) {
        if (getActiveActivityNumber() == 6 && activity == 6) {
            CreateListogramActivity createListogramActivity = (CreateListogramActivity) getActiveActivity();
            createListogramActivity.showAddListOfflineGood();
        }
        else if(getActiveActivityNumber() == 2 && activity == 2){
            ActiveListsActivity activeListsActivity = (ActiveListsActivity) getActiveActivity();
            activeListsActivity.refreshOfflineLists();
        }
    }

    public void showOfflineListCreatedBad(SList list, int activity) {
        if (getActiveActivityNumber() == 6 && activity == 6) {
            CreateListogramActivity createListogramActivity = (CreateListogramActivity) getActiveActivity();
            createListogramActivity.showAddListOfflineBad();
        }
        else if(getActiveActivityNumber() == 2 && activity == 2){
            ActiveListsActivity activeListsActivity = (ActiveListsActivity) getActiveActivity();
            activeListsActivity.refreshOfflineLists();
        }
    }

    public void showOfflineActiveListsItemmarkedGood(Item item) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineGood(item);
        }
    }

    public void showOfflineActiveListsItemmarkedBad(Item item) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showItemmarkOfflineBad(item);
        }
    }

    public void showOfflineActiveListsDisactivatedGood(SList list) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineGood(list);
            if (!dataExchanger.checkOfflineActiveLists()) {
                activity.showActiveOfflineBad(new SList[0]);
            }
        }
    }

    public void showOfflineActiveListsDisactivatedBad(SList list) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showDisactivateOfflineBad(list);
        }
    }

    public void showOfflineActiveListsGood(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineGood(lists);
        }
    }

    public void showOfflineActiveListsBad(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showActiveOfflineBad(lists);
        }
    }

    public void showOfflineHistoryListsGood(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineGood(lists);
        }
    }

    public void showOfflineHistoryListsBad(SList[] lists) {
        if (getActiveActivityNumber() == 2) {
            ActiveListsActivity activity = (ActiveListsActivity) getActiveActivity();
            activity.showHistoryOfflineBad(lists);
        }
    }
}
