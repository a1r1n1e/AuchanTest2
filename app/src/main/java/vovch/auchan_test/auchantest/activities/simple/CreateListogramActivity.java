package vovch.auchan_test.auchantest.activities.simple;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

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


public class CreateListogramActivity extends WithLoginActivity {

    private static final String INTENT_LOAD_TYPE = "loadtype";
    private static final String FRAGMENT_TRANSACTION_DIALOG = "dialog";

    protected ArrayList<TempItem> TempItems = new ArrayList<>();
    private String groupId;
    private boolean flag;
    private ActiveActivityProvider provider;

    private int loadType;

    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId ==EditorInfo.IME_ACTION_UNSPECIFIED) {
                CreateListEditText currentEditText = (CreateListEditText) v;
                CreateListEditText nextEditText = currentEditText.getTempItem().getItemCommentEditText();
                Selection.setSelection(nextEditText.getText(), nextEditText.getSelectionStart());
                nextEditText.requestFocus();
                return true;
            }
            else {
                return true;
            }
        }
    };
    TextView.OnEditorActionListener editorListenerTwo = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                /*CreateListEditText createListEditText = (CreateListEditText) v;
                TempItem item = createListEditText.getTempItem();
                int number = TempItems.indexOf(item);
                if(number >= TempItems.size() - 1) {
                    createPunct(null);
                }
                CreateListEditText nextEditText =  TempItems.get(number + 1).getNameEditText();
                Selection.setSelection(nextEditText.getText(), nextEditText.getSelectionStart());
                nextEditText.requestFocus();
                return true;*/
                CreateListEditText editText = (CreateListEditText) v;
                FindInBaseRunnable runnable = new FindInBaseRunnable(v.getText().toString(), provider, editText.getTempItem());
                provider.executor.execute(runnable);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flag = false;

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);

        setContentView(R.layout.activity_create_listogram);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_listogram_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        if(getIntent().getExtras() != null) {
            loadType = getIntent().getExtras().getInt(INTENT_LOAD_TYPE);                                      //getting information what type of action is it
        }

        if((loadType == 1 || loadType == 3) && provider != null ){
            UserGroup currentGroup = provider.getActiveGroup();
            if(currentGroup != null){
                groupId = currentGroup.getId();
            }
        }

        if(loadType == 2 || loadType == 3){
            EditText listNameEditText = (EditText) findViewById(R.id.create_listogram_toolbar_edittext);
            listNameEditText.setText(provider.getResendingList().getName());
        }

        View.OnClickListener sendListagramListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendListogram();
            }
        };
        ImageButton sendListogramButton = (ImageButton) findViewById(R.id.create_listogram_send_list_button);
        sendListogramButton.setOnClickListener(sendListagramListener);
        scrollToEnd();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.create_listogram_add_punct_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPunct(null);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(6, CreateListogramActivity.this);

        clearer();
        getAndShowTempItemsState();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onStop(){
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        saveTempItemsState();
        if(flag){
            provider.dataExchanger.clearTempItems();
        }
        super.onStop();
    }
    @Override
    public void onBackPressed(){
        if(provider.getActiveActivityNumber() == 6) {
            provider.nullActiveActivity();
        }
        Intent intent = null;
        if(loadType == 1 || loadType == 3) {                                                            //Possibly refactor to odds and evens
            intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        } else if ((loadType == 0 || loadType == 2) && provider != null) {
            provider.setActiveListsActivityLoadType(1);
            intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        }
        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            flag = true;
            startActivity(intent);
        }
        if(loadType == 0 || loadType == 2) {
            provider.setActiveListsActivityLoadType(1);
        }
        super.onBackPressed();
    }
    private void clearer(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        layout.removeAllViewsInLayout();
        if(TempItems != null) {
            TempItems.clear();
        }
    }

    public void showVariants(final TempItem[] result, TempItem addTo){
        LinearLayout layout = (LinearLayout) findViewById(R.id.temp_items_result_viewer);
        layout.removeAllViews();
        for(TempItem item : result) {
            //FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(layout.getContext()).inflate(R.layout.result_item_frame, layout, false);
            ResultItemLayout itemLayout = (ResultItemLayout) LayoutInflater.from(layout.getContext()).inflate(R.layout.result_item_layout, layout, false);
            itemLayout.setAddTo(addTo);

            TextView priceText = (TextView) LayoutInflater.from(itemLayout.getContext()).inflate(R.layout.price_text_view, itemLayout, false);
            priceText.setText(item.getPrice());
            ImageView imageView = (ImageView) LayoutInflater.from(itemLayout.getContext()).inflate(R.layout.item_image_view, itemLayout, false);
            imageView.setImageBitmap(item.getImage());

            itemLayout.addView(imageView);
            itemLayout.addView(priceText);

            //ResultItemButton button = (ResultItemButton) LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.item_transparent_button, frameLayout, false);
            itemLayout.setItem(item);
            itemLayout.setFocusable(true);
            itemLayout.setClickable(true);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResultItemLayout resultItemLayout = (ResultItemLayout) view;
                    addAuchanItem(resultItemLayout.getItem(), resultItemLayout.getAddTo());
                    LinearLayout layout = (LinearLayout)  findViewById(R.id.temp_items_result_viewer);
                    layout.removeAllViews();
                }
            });
            //frameLayout.addView(itemLayout);
            //frameLayout.addView(button);

            layout.addView(itemLayout);
        }
        ScrollView scrollView = (ScrollView) findViewById(R.id.createlistogramscrollview);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void addAuchanItem(TempItem item, TempItem addTo){
        Toast.makeText(CreateListogramActivity.this, item.getPrice(), Toast.LENGTH_LONG)
                .show();
        if(addTo != null){
            ImageView imageView = (ImageView) LayoutInflater.from(addTo.getTempItemLayout().getContext()).inflate(R.layout.item_image_view, addTo.getTempItemLayout(), false);
            imageView.setImageBitmap(item.getImage());
            addTo.setImage(item.getImage());
            addTo.setPrice(item.getPrice());
            addTo.setDescription(item.getDescription());
            addTo.setCompound(item.getCompound());
            addTo.setAuchanId(item.getAuchanId());

            addTo.getTempItemLayout().addView(imageView);
        }
    }

    private void scrollToEnd(){
        getScrollView().post(new Runnable() {
            @Override
            public void run() {
                getScrollView().fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    private ScrollView getScrollView(){
        return (ScrollView) findViewById(R.id.createlistogramscrollview);
    }
    public void createPunct(TempItem tempItem) {
        LinearLayout listogramContainerLayout = (LinearLayout) findViewById(R.id.listogrampunctslayout);
        CardView cardView = (CardView) LayoutInflater.from(listogramContainerLayout.getContext()).inflate(R.layout.list_card, listogramContainerLayout, false);
        if (tempItem == null) {
            tempItem = new TempItem();
            tempItem.setState(true);
            showViewsForPunct(tempItem, cardView, false);
        } else {
            if (tempItem.getCardView() != null && tempItem.getLayout() != null && tempItem.getNameEditText() != null) {         //TODO never gettng inside
                cardView = tempItem.getCardView();
                tempItem.getNameEditText().setOnEditorActionListener(editorListenerOne);
                cardView.setVisibility(View.VISIBLE);
                tempItem.setCardView(cardView);
                if(TempItems != null) {
                    TempItems.add(tempItem);
                }
            } else {
                showViewsForPunct(tempItem, cardView, true);
            }
        }
        listogramContainerLayout.addView(cardView);
    }

    private void showViewsForPunct(TempItem tempItem, CardView cardView, boolean hasData){
        LinearLayout verticalLayout = (LinearLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.list_element_vertical_layout, cardView, false);

        LinearLayout addingListogramLayout = (LinearLayout) LayoutInflater.from(verticalLayout.getContext()).inflate(R.layout.create_listogram_linearlayout, verticalLayout, false);
        CreateListEditText itemNameEditText;
        itemNameEditText = (CreateListEditText) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.create_listogram_edittext, addingListogramLayout, false);
        if(hasData){
            itemNameEditText.setText(tempItem.getName());
        }
        itemNameEditText.setHint("Name");
        itemNameEditText.setTempItem(tempItem);
        itemNameEditText.setOnEditorActionListener(editorListenerTwo);
        addingListogramLayout.addView(itemNameEditText);
        FrameLayout buttonFrame = (FrameLayout) LayoutInflater.from(addingListogramLayout.getContext()).inflate(R.layout.small_image_button_frame, addingListogramLayout, false);
        ImageButton imageButton = (ImageButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.list_header_resend_image_button, buttonFrame, false);
        Uri uri = Uri.parse("android.resource://vovch.auchan_test.auchantest/mipmap/delete_custom_white_green");
        imageButton.setImageURI(uri);
        imageButton.setFocusable(false);
        imageButton.setClickable(false);
        ItemButton addingButton = (ItemButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.list_element_button, buttonFrame, false);
        View.OnClickListener deleteListenner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePunct((ItemButton) v);
            }
        };
        addingButton.setFocusable(true);
        addingButton.setClickable(true);
        addingButton.setOnClickListener(deleteListenner);
        buttonFrame.addView(imageButton);
        buttonFrame.addView(addingButton);
        addingListogramLayout.addView(buttonFrame);
        verticalLayout.addView(addingListogramLayout);
        cardView.addView(verticalLayout);
        tempItem.setTempItemLayout(verticalLayout);
        tempItem.setButton(addingButton);
        tempItem.setItemNameEditText(itemNameEditText);
        tempItem.setCardView(cardView);

        if(tempItem.getImage() != null){
            ImageView imageView = (ImageView) LayoutInflater.from(verticalLayout.getContext()).inflate(R.layout.item_image_view, verticalLayout, false);
            imageView.setImageBitmap(tempItem.getImage());
            verticalLayout.addView(imageView);
        }

        if(TempItems != null) {
            TempItems.add(tempItem);
        }
        addingButton.setTempItem(tempItem);
    }

    public void deletePunct(ItemButton button){
        int i = 0;
        if(button.getTempItem() != null){
            TempItem tempItem = button.getTempItem();
            tempItem.getCardView().setVisibility(View.GONE);
            if(TempItems != null) {
                TempItems.remove(tempItem);
                TempItems.trimToSize();
            }
            saveTempItemsState();
        }
    }
    public void saveTempItemsState(){
        if(TempItems != null) {
            /*TempItem[] tempItems = new TempItem[TempItems.size()];
            for (int i = 0; i < tempItems.length; i++) {
                tempItems[i] = TempItems.get(i);
                tempItems[i].setName(TempItems.get(i).getNameEditText().getText().toString());
            }
            if (provider != null) {
                provider.saveTempItems(tempItems);
            }*/
            if(TempItems.size() > 0){
                TempItem[] tempItems = new TempItem[TempItems.size()];
                provider.saveTempItems(TempItems.toArray(tempItems));
            }
        }
    }
    public void getAndShowTempItemsState(){
        if(provider != null) {
            TempItem[] tempItems;
            tempItems = provider.getTempItems();
            int length = tempItems.length;
            for (TempItem i : tempItems) {
                createPunct(i);
            }
            if (length == 0) {
                createPunct(null);
            }
        }
    }
    public Item[] makeSendingItemsArray(){
        TempItem[] tempItems = new TempItem[TempItems.size()];
        tempItems = TempItems.toArray(tempItems);
        for(TempItem i : tempItems){
            i.setName(i.getNameEditText().getText().toString());
        }
        return tempItems;
    }
    public void sendListogram(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        Item[] items = makeSendingItemsArray();
        if(items != null && provider != null){
            CompleteDialogFragment dialogFragment = new CompleteDialogFragment();
            dialogFragment.setActiveActivityProvider(provider);
            dialogFragment.setItems(items);
            dialogFragment.setLoadType(loadType);
            dialogFragment.setFab(fab);

            EditText listNameEdittext = (EditText) findViewById(R.id.create_listogram_toolbar_edittext);
            dialogFragment.setListName(listNameEdittext.getText().toString());

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
        }
    }

    public static class CompleteDialogFragment extends DialogFragment {
        private int loadType;
        private boolean clickable;
        private Item[] items;
        private FloatingActionButton fab;
        private ActiveActivityProvider activeActivityProvider;
        private String listName;
        protected void setFab(FloatingActionButton newFab){
            fab = newFab;
        }
        protected void setLoadType(int newLoadType){
            loadType = newLoadType;
        }
        protected void setItems(Item[] newItems){
            items = newItems;
        }
        protected void setListName(String name){
            listName = name;
        }
        protected void setActiveActivityProvider(ActiveActivityProvider newActiveActivityProvider){
            activeActivityProvider = newActiveActivityProvider;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            clickable = true;
            String message = getString(R.string.dialog_confirm_question);
            String button2String = getString(R.string.Yes);
            String button1String = getString(R.string.No);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getActivity(), getString(R.string.dialog_nothing_happened), Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clickable = false;

                    if(items.length > 0) {
                        if (items[items.length - 1].getName().equals("") || items[items.length - 1].getName() == null) {
                            if (items.length > 1) {
                                items = Arrays.copyOfRange(items, 0, items.length - 1);
                            } else {
                                items = new TempItem[0];
                            }
                        }
                    }

                    /*if(loadType == 1) {
                        activeActivityProvider.createOnlineListogram(activeActivityProvider.getActiveGroup(), items, listName);
                    }
                    else if(loadType == 0){
                        activeActivityProvider.createListogramOffline(items, listName);
                    } else if(loadType == 2){
                        activeActivityProvider.redactOfflineListogram(items, activeActivityProvider.getResendingList(), listName);
                    } else if(loadType == 3){
                        activeActivityProvider.redactOnlineListogram(items, activeActivityProvider.getResendingList(), listName);
                    }*/

                    if(loadType == 0){
                        activeActivityProvider.createListogram(items, listName);
                    } else if(loadType == 2){
                        activeActivityProvider.redactListogram(items, activeActivityProvider.getResendingList(), listName);
                    }

                    Toast.makeText(getActivity(), getString(R.string.dialog_confirm_action_processing),
                            Toast.LENGTH_LONG).show();
                }
            });
            if(fab != null) {
                fab.setFocusable(clickable);
                fab.setClickable(clickable);
            }
            builder.setCancelable(false);
            return builder.create();
        }
    }

    public void showAddListOfflineGood(){
        if(TempItems != null) {
            TempItems.clear();
            TempItems.trimToSize();
        }
        Intent intent;
        provider.setActiveListsActivityLoadType(1);
        provider.nullActiveActivity();
        intent = new Intent(CreateListogramActivity.this, ActiveListsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showAddListOfflineBad(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        fab.setFocusable(true);
        fab.setClickable(true);
    }

    public void showAddListOnlineGood(UserGroup group){
        if(TempItems != null) {
            TempItems.clear();
            TempItems.trimToSize();
        }
        provider.nullActiveActivity();
        provider.setActiveGroup(group);
        Intent intent;
        intent = new Intent(CreateListogramActivity.this, Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void showAddListOnlineBad(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_listogram_send_list_button);
        fab.setFocusable(true);
        fab.setClickable(true);
    }
}
