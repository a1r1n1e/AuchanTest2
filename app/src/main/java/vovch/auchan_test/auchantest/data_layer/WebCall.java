package vovch.auchan_test.auchantest.data_layer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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


/**
 * Created by vovch on 03.01.2018.
 */

public class WebCall {

    private ActiveActivityProvider provider;

    private static final String DATA_1 = "uname";
    private static final String DATA_2 = "upassword";
    private static final String DATA_3 = "third";
    private static final String ACTION = "action";
    private static final String DATA_JSON = "data_json";
    private static final String SESSION_ID = "session_id";
    private static final String TOKEN = "token";
    private static final String CLIENT_TYPE = "client_type";
    private static final String VERSION = "version";

    private static final String NO_INTERNET = "400";
    private static final String ONLINE_ACTIONS_DENIED_INFORMER = "000";

    public WebCall() {
    }

    public void setActiveActivityProvider(ActiveActivityProvider newProvider){
        provider = newProvider;
    }

    public String callServer(Object... loginPair) {

        UserSessionData userSessionData;
        String response = "";
        if (loginPair[5] != null && loginPair[5] instanceof UserSessionData) {
            userSessionData = (UserSessionData) loginPair[5];
            if (userSessionData.isSession() || ((String) loginPair[3]).equals("registration")) {
                HttpURLConnection conn = null;
                try {
                    URL url;
                    ActiveActivityProvider activeActivityProvider = (ActiveActivityProvider) userSessionData.getContext();
                    if (activeActivityProvider != null) {
                        if (!((String) loginPair[3]).equals("registration")) {
                            url = new URL(activeActivityProvider.getString(R.string.controller_page));
                        } else {
                            url = new URL(activeActivityProvider.getString(R.string.session_page));
                        }

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);
                        conn.setDoOutput(true);                                                 // Enable POST stream
                        conn.setDoInput(true);
                        conn.setRequestMethod("POST");

                        HashMap<String, String> postDataParams = new HashMap<String, String>();

                        postDataParams.put(DATA_1, (String) loginPair[0]);
                        postDataParams.put(DATA_2, (String) loginPair[1]);
                        postDataParams.put(DATA_3, (String) loginPair[2]);
                        postDataParams.put(ACTION, (String) loginPair[3]);
                        postDataParams.put(DATA_JSON, (String) loginPair[4]);

                        postDataParams.put(SESSION_ID, userSessionData.getSession());
                        postDataParams.put(CLIENT_TYPE, userSessionData.getClientType());
                        postDataParams.put(VERSION, userSessionData.getClientVersion());
                        postDataParams.put(TOKEN, userSessionData.getToken());

                        InputStream is = null;
                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();


                        int responseCode = conn.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {
                            response += String.valueOf(responseCode);
                            String line;
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            while ((line = br.readLine()) != null) {
                                response += line;
                            }
                        } else {
                            response += String.valueOf(responseCode);
                        }
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    if(e instanceof IOException) {
                        if (loginPair[3].equals("itemmark")) {
                            StringBuilder tempString = new StringBuilder("");
                            tempString.append("400");
                            tempString.append(loginPair[1]);
                            response = tempString.toString();
                        }
                    } else{
                        response = "700";
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } else {
                response =  ONLINE_ACTIONS_DENIED_INFORMER;
            }
        } else {
            response = ONLINE_ACTIONS_DENIED_INFORMER;
        }
        if(response.equals("")){
            response = ONLINE_ACTIONS_DENIED_INFORMER;
        }
        return response;
    }

    public static UserGroup getNewGroupFromJsonString(String result){
        UserGroup group = null;
        try {
            JSONArray groupsArray = new JSONArray(result);
            int length = groupsArray.length();
            UserGroup tempGroup;
            JSONObject tempJsonObject;
            JSONArray tempListsArray;
            JSONArray tempMembersArray;
            String name;
            String id;
            AddingUser[] members;
            String owner;
            String ownerName;
            String lastUpdateTime;
            tempJsonObject = groupsArray.getJSONObject(0);
            name = tempJsonObject.getString("group_name");
            id = tempJsonObject.getString("group_id");
            tempMembersArray = tempJsonObject.getJSONArray("group_members");
            int membersNumber = tempMembersArray.length();
            members = new AddingUser[membersNumber];
            JSONObject tempMember;
            for (int j = 0; j < membersNumber; j++) {
                tempMember = tempMembersArray.getJSONObject(j);
                members[j] = new AddingUser();
                members[j].setData(tempMember.getString("name"), tempMember.getString("id"));
            }
            owner = tempJsonObject.getString("group_owner_id");
            ownerName = tempJsonObject.getString("group_owner_name");
            lastUpdateTime = tempJsonObject.getString("group_last_update_time");
            tempGroup = new UserGroup(name, id, members);
            tempGroup.setHistoryLists(new SList[0]);
            tempGroup.setActiveLists(new SList[0]);
            tempGroup.setOwner(owner);
            tempGroup.setOwnerName(ownerName);
            tempGroup.setLastUpdateTime(lastUpdateTime);
            group = tempGroup;
        } catch (Exception e) {
            return null;
        }
        return group;
    }

    public static UserGroup[] getGroupsFromJsonString(String result) {
        UserGroup[] groups = null;
        try {
            JSONArray groupsArray = new JSONArray(result);
            int length = groupsArray.length();
            groups = new UserGroup[length];
            UserGroup tempGroup;
            JSONObject tempJsonObject;
            JSONArray tempListsArray;
            JSONArray tempMembersArray;
            String name;
            String id;
            AddingUser[] members;
            String owner;
            String ownerName;
            String lastUpdateTime;
            for (int i = 0; i < length; i++) {
                tempJsonObject = groupsArray.getJSONObject(i);
                name = tempJsonObject.getString("group_name");
                id = tempJsonObject.getString("group_id");
                tempMembersArray = tempJsonObject.getJSONArray("group_members");
                int membersNumber = tempMembersArray.length();
                members = new AddingUser[membersNumber];
                JSONObject tempMember;
                for (int j = 0; j < membersNumber; j++) {
                    tempMember = tempMembersArray.getJSONObject(j);
                    members[j] = new AddingUser();
                    members[j].setData(tempMember.getString("user_name"), tempMember.getString("user_id"));
                }
                owner = tempJsonObject.getString("group_owner_id");
                ownerName = tempJsonObject.getString("group_owner_name");
                lastUpdateTime = tempJsonObject.getString("group_last_update_time");
                tempGroup = new UserGroup(name, id, members);
                tempGroup.setOwner(owner);
                tempGroup.setOwnerName(ownerName);
                tempGroup.setLastUpdateTime(lastUpdateTime);

                tempListsArray = tempJsonObject.getJSONArray("group_lists");
                int listsLength = tempListsArray.length();
                JSONObject tempListObject;
                for (int k = 0; k < listsLength; k++) {
                    tempListObject = tempListsArray.getJSONObject(k);
                    JSONArray encodedItems = tempListObject.getJSONArray("items_array");
                    int itemsLength = encodedItems.length();
                    Item[] items = new Item[itemsLength];
                    for (int j = 0; j < itemsLength; j++) {
                        String itemName = encodedItems.getJSONObject(j).getString("item_name");
                        int itemId = Integer.parseInt(encodedItems.getJSONObject(j).getString("item_id"));
                        String itemStateString = encodedItems.getJSONObject(j).getString("item_state");
                        String itemOwner = encodedItems.getJSONObject(j).getString("item_owner_id");
                        String itemOwnerName = encodedItems.getJSONObject(j).getString("item_owner_name");
                        boolean itemState = false;
                        if (itemStateString.equals("t")) {
                            itemState = true;
                        }
                        Item tempItem = new Item(itemId, itemName, null, itemState);
                        if (!itemOwner.equals("null") && !itemOwnerName.equals("null")) {
                            tempItem.setOwner(itemOwner);
                            tempItem.setOwnerName(itemOwnerName);
                        }
                        String itemStore = encodedItems.getJSONObject(j).getString("item_store");
                        String itemSpecId = encodedItems.getJSONObject(j).getString("item_special_id");
                        String imageLink = encodedItems.getJSONObject(j).getString("item_image_link");
                        String description = encodedItems.getJSONObject(j).getString("item_description");
                        String compound = encodedItems.getJSONObject(j).getString("item_compound");
                        String price = encodedItems.getJSONObject(j).getString("item_price");
                        if(itemStore.equals("null")){                                               //different null types, check to be sure
                            itemStore = "";
                            itemSpecId = "";
                            imageLink = "";
                            description = "";
                            compound = "";
                            price = "";
                            tempItem.setImage(null);
                        } else{
                            tempItem.setImage(WebCall.getBitmapFromURL(imageLink));
                        }
                        tempItem.setAuchanId(itemSpecId);                                           //TODO store
                        tempItem.setDescription(description);
                        tempItem.setCompound(compound);
                        tempItem.setPrice(price);
                        items[j] = tempItem;
                    }
                    int listId = Integer.parseInt(tempListObject.getString("list_id"));
                    String listStateString = tempListObject.getString("list_state");
                    boolean listState = false;
                    if (listStateString.equals("t")) {
                        listState = true;
                    }
                    int listOwner = tempListObject.getInt("list_owner_id");
                    String listOwnerName = tempListObject.getString("list_owner_name");

                    String creation_time = tempListObject.getString("list_creation_time");
                    String listName = tempListObject.getString("list_name");
                    SList tempList = new SList(items, listId, tempGroup, true, listState, listOwner, listOwnerName, creation_time);
                    tempList.setName(listName);
                    if(tempList.getState()){
                        tempGroup.addActiveList(tempList);
                    } else {
                        tempGroup.addHistoryList(tempList);
                    }
                    for (int m = 0; m < items.length; m++) {
                        items[m].setList(tempList);
                    }
                }
                /*if(tempGroup.getActiveLists() == null || tempGroup.getActiveLists().length == 0){
                    tempGroup.setState("f");
                } else{
                    tempGroup.setState("t");
                }*/
                groups[i] = tempGroup;
            }
        } catch (Exception e) {
            return null;
        }
        return groups;
    }

    public static UserGroup getGroupFromJSONString(String result){
        try {
            UserGroup[] groups = getGroupsFromJsonString(result);
            if(groups != null && groups.length > 0) {
                return groups[0];
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }

    public static String prepareItemsJSONString(Item[] items) {
        String result = null;
        if (items != null) {
            int length = items.length;
            JSONArray itemsArray = new JSONArray();
            try {
                for (int i = 0; i < length; i++) {
                    JSONObject item = new JSONObject();
                    item.put("item_name", items[i].getName());
                    String ownerId;
                    if (items[i].getOwner() != null) {
                        ownerId = items[i].getOwner();
                    } else {
                        ownerId = "0";
                    }
                    item.put("item_owner", ownerId);
                    if (items[i].getId() != 0) {
                        item.put("item_id", items[i].getId());
                    }
                    if (items[i].getState()) {
                        item.put("item_state", "TRUE");
                    } else {
                        item.put("item_state", "FALSE");
                    }
                    if ("auchan".equals("auchan")){                             //TODO
                        item.put("item_store", "auchan");
                    }
                    if (items[i].getAuchanId() != null){
                        item.put("item_special_id", items[i].getAuchanId());
                    }
                    itemsArray.put(item);
                }
                result = itemsArray.toString();
            } catch (Exception e) {
                return null;
            }
            return result;
        } else {
            return null;
        }
    }

    public static String getStringFromJsonString(String jsonString, String value) {
        String result = null;
        if (jsonString != null && value != null) {
            try {
                JSONObject dataHolder = new JSONObject(jsonString);
                result = dataHolder.getString(value);
            } catch (Exception e) {                                                                         //TODO

            }
            return result;
        } else {
            return null;
        }
    }

    public static  SList[] getGroupListsFromJsonString(String result, UserGroup group) {
        SList[] lists = null;
        if (result != null && group != null) {
            try {
                JSONArray fromJsonLists = new JSONArray(result);
                int length = fromJsonLists.length();
                SList tempList;
                Item[] items;
                Item tempItem;
                JSONObject tempListObject;
                lists = new SList[length];
                for (int i = 0; i < length; i++) {
                    tempListObject = fromJsonLists.getJSONObject(i);
                    JSONArray encodedItems = tempListObject.getJSONArray("items_array");
                    int itemsLength = encodedItems.length();
                    items = new Item[itemsLength];
                    for (int j = 0; j < itemsLength; j++) {
                        String itemName = encodedItems.getJSONObject(j).getString("item_name");
                        String itemComment = encodedItems.getJSONObject(j).getString("item_comment");
                        int itemId = encodedItems.getJSONObject(j).getInt("item_id");
                        String itemStateString = encodedItems.getJSONObject(j).getString("item_state");
                        String itemOwner = encodedItems.getJSONObject(j).getString("item_owner_id");
                        String itemOwnerName = encodedItems.getJSONObject(j).getString("item_owner_name");
                        boolean itemState = false;
                        if (itemStateString.equals("t")) {
                            itemState = true;
                        }
                        tempItem = new Item(itemId, itemName, itemComment, itemState);
                        if (!itemOwner.equals("null") && !itemOwnerName.equals("null")) {
                            tempItem.setOwner(itemOwner);
                            tempItem.setOwnerName(itemOwnerName);
                        }
                        items[j] = tempItem;
                    }
                    int listId = tempListObject.getInt("list_id");
                    String listStateString = tempListObject.getString("list_state");
                    boolean listState = false;
                    if (listStateString.equals("t")) {
                        listState = true;
                    }
                    int listOwner = tempListObject.getInt("list_owner");
                    String listOwnerName = tempListObject.getString("list_owner_name");
                    int listGroupId = tempListObject.getInt("list_group");

                    if(group != null) {
                        if (Integer.parseInt(group.getId()) != listGroupId) {
                            group = null;
                        }
                    }


                    String creation_time = tempListObject.getString("list_creation_time");
                    String listName = tempListObject.getString("list_name");
                    tempList = new SList(items, listId, group, false, listState, listOwner, listOwnerName, creation_time);
                    tempList.setName(listName);
                    for (int k = 0; k < items.length; k++) {
                        items[k].setList(tempList);
                    }
                    lists[i] = tempList;
                }
            } catch (Exception e) {
                return null;
            }
            return lists;
        } else {
            return null;
        }
    }

    /*public  static ListInformer[] getListInformersFromJsonString(String result) {
        if (result != null) {
            ListInformer[] informers = null;
            try {
                JSONArray informersArray = new JSONArray(result);
                JSONObject tempObject = null;
                ListInformer tempInformer = null;
                UserGroup tempGroup;
                int i = 0;
                int length = informersArray.length();
                informers = new ListInformer[length];
                String groupName;
                String groupId;
                AddingUser[] tempMembers;
                JSONArray tempMembersArray;
                String owner;
                String active;
                int tempMembersLength;
                for (i = 0; i < length; i++) {
                    tempObject = informersArray.getJSONObject(i);
                    groupId = tempObject.getString("group_id");
                    groupName = tempObject.getString("group_name");
                    owner = tempObject.getString("group_owner");
                    active = tempObject.getString("group_active");
                    tempMembersArray = tempObject.getJSONArray("group_members");
                    tempMembersLength = tempMembersArray.length();
                    tempMembers = new AddingUser[tempMembersLength];
                    for (int j = 0; j < tempMembersLength; j++) {
                        tempObject = tempMembersArray.getJSONObject(j);
                        tempMembers[j] = new AddingUser();
                        tempMembers[j].setData(tempObject.getString("name"), tempObject.getString("id"));
                    }
                    tempInformer = new ListInformer(groupId, groupName, active);
                    tempGroup = new UserGroup(groupName, groupId, tempMembers);
                    tempGroup.setOwner(owner);
                    tempInformer.setGroup(tempGroup);
                    informers[i] = tempInformer;
                }
            } catch (Exception e) {
                return null;
            }
            return informers;
        } else {
            return null;
        }
    }*/



    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.d("AuchanTest2", "getting bitmap");
            return null;
        }
    }



    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
