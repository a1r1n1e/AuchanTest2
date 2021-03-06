package vovch.auchan_test.auchantest.activities.simple;

import android.app.Dialog;
import android.content.Context;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class NewGroup extends WithLoginActivity {

    private static final String FRAGMENT_TRANSACTION_DIALOG = "dialog";
    private static final int THIS_ACTIVITY_NUMBER = 5;

    protected ActiveActivityProvider provider;

    protected int getThisActivityNumber(){
        return THIS_ACTIVITY_NUMBER;
    }

    protected Context getThisActivityContext(){
        return NewGroup.this;
    }

    protected String getThisActivityType(){
        return "NewGroup";
    }

    TextView.OnEditorActionListener editorListenerOne = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_NEXT ||
                    actionId == EditorInfo.IME_ACTION_SEND ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                addUser();
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
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(getThisActivityNumber(), getThisActivityContext());

        setContentView(R.layout.activity_new_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.group_settings_toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(24);
        }
        setSupportActionBar(toolbar);

        View.OnClickListener addUserButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
                nullUserIdEditText();
            }
        };
        FloatingActionButton addUserButton = (FloatingActionButton) findViewById(R.id.group_settings_add_user_fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addUserButton.setImageDrawable(getDrawable(R.mipmap.add_user_custom_green_white));
        } else {
            addUserButton.setImageDrawable(getResources().getDrawable(R.mipmap.add_user_custom_green_white));
        }
        addUserButton.setOnClickListener(addUserButtonListener);

        Button confirmGroupAdding = (Button) findViewById(R.id.newgroupsubmitbutton);
        View.OnClickListener confirmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAll();
            }
        };
        confirmGroupAdding.setOnClickListener(confirmListener);

        CreateListEditText editText = (CreateListEditText) findViewById(R.id.new_user_id_edittext);
        editText.setOnEditorActionListener(editorListenerOne);

        initLayout();
    }

    protected void initLayout(){
        provider.addYourselfToGroup();
        AddingUser[] users = provider.getPossibleMembers();
        drawDeletableMembers(users);
    }

    public void drawDeletableMembers(AddingUser[] newMembers) {
        for (AddingUser newMember : newMembers) {
            drawNewUserLayout(newMember, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        provider = (ActiveActivityProvider) getApplicationContext();
        provider.setActiveActivity(getThisActivityNumber(), getThisActivityContext());
    }

    @Override
    protected void onStop() {
        if (provider.getActiveActivityNumber() == getThisActivityNumber()) {
            provider.nullActiveActivity();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        provider.setActiveListsActivityLoadType(0);
        super.onBackPressed();
        //goOutOfActivity(ActiveListsActivity.class);
    }

    protected void goOutOfActivity(Class<?> cls){
        if (provider.getActiveActivityNumber() == getThisActivityNumber()) {
            provider.nullActiveActivity();
            provider.setActiveGroup(null);
        }
        Intent intent = new Intent(getThisActivityContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    public void addUser() {
        String id = getAddingUserId();
        if (id != null) {
            if (providerCheckUser(id)) { // if user already added
                Toast.makeText(NewGroup.this, getString(R.string.user_added_inormer), Toast.LENGTH_LONG)
                        .show();
            } else {
                //FloatingActionButton button = (FloatingActionButton) findViewById(R.id.group_settings_add_user_fab);
                //button.setFocusable(false);
               //button.setClickable(false);
                CreateListEditText editText = (CreateListEditText) findViewById(R.id.new_user_id_edittext);
                editText.setOnEditorActionListener(editorListenerTwo);
                nullUserIdEditText();
                providerAddUser(id); // error id checked inside
            }
        } else {
            Toast.makeText(NewGroup.this, getString(R.string.enter_user_id_informer), Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected boolean providerCheckUser(String id){
        return provider.checkUser(id);
    }

    protected void providerAddUser(String id){
        provider.addUserToGroup(id, getThisActivityType());
    }

    public String getAddingUserId() {
        String value;
        TextView newUserIdTextView = (TextView) findViewById(R.id.new_user_id_edittext);
        value = newUserIdTextView.getText().toString();
        String id;
        if (!value.equals("") && android.text.TextUtils.isDigitsOnly(value)) {
            id = newUserIdTextView.getText().toString();
        } else {
            id = null;
        }
        return id;
    }

    public void drawNewUserLayout(AddingUser user, boolean loadType) {
        LinearLayout addingUsersLayout = (LinearLayout) findViewById(R.id.group_members_linear_layout);
        CardView cardView = (CardView) LayoutInflater.from(addingUsersLayout.getContext()).inflate(R.layout.list_card, addingUsersLayout, false);

        LinearLayout addingUserLayout = (LinearLayout) LayoutInflater.from(cardView.getContext()).inflate(R.layout.create_listogram_linearlayout, cardView, false);
        addingUserLayout.setGravity(Gravity.CENTER);

        EditText userNameEditText;
        userNameEditText = (EditText) LayoutInflater.from(addingUserLayout.getContext()).inflate(R.layout.adding_user_edittext, addingUserLayout, false);
        userNameEditText.setText(user.getUserName());
        userNameEditText.setGravity(Gravity.CENTER);
        userNameEditText.setFocusable(false);
        userNameEditText.setClickable(false);
        if(loadType){
            addingUserLayout.addView((FrameLayout) LayoutInflater.from(addingUserLayout.getContext()).inflate(R.layout.small_image_button_frame, addingUserLayout, false));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addingUserLayout.setBackground(getDrawable(R.drawable.no_corners_layout_color_2));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                addingUserLayout.setBackground(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            } else {
                addingUserLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_corners_layout_color_2));
            }
        }
        addingUserLayout.addView(userNameEditText);

        if(loadType) {
            userNameEditText.setGravity(Gravity.CENTER);
            FrameLayout buttonFrame = (FrameLayout) LayoutInflater.from(addingUserLayout.getContext()).inflate(R.layout.small_image_button_frame, addingUserLayout, false);
            ImageButton imageButton = (ImageButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.list_header_resend_image_button, buttonFrame, false);
            Uri uri = Uri.parse("android.resource://vovch.auchan_test.auchantest/mipmap/delete_custom_white_green");
            imageButton.setImageURI(uri);
            imageButton.setFocusable(false);
            imageButton.setClickable(false);
            UserButton userButton = (UserButton) LayoutInflater.from(buttonFrame.getContext()).inflate(R.layout.adding_user_button, buttonFrame, false);
            View.OnClickListener deleteListenner = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserButton button = (UserButton) v;
                    provider.removeAddedUser(button.getUser());
                }
            };
            userButton.setFocusable(true);
            userButton.setClickable(true);
            userButton.setOnClickListener(deleteListenner);
            buttonFrame.addView(imageButton);
            buttonFrame.addView(userButton);
            user.setCardView(cardView);
            userButton.setUser(user);
            addingUserLayout.addView(buttonFrame);
        }

        cardView.addView(addingUserLayout);
        addingUsersLayout.addView(cardView);
    }

    public void showRemoveUserGood(AddingUser user) {
        CardView cardView = user.getCardView();
        cardView.setVisibility(View.GONE);
    }

    public void showRemoveUserBad(AddingUser user) {
        Toast.makeText(NewGroup.this, getString(R.string.not_removable_user_error), Toast.LENGTH_LONG)
                .show();
    }

    protected void nullUserIdEditText() {
        EditText edit = (EditText) findViewById(R.id.new_user_id_edittext);
        edit.setHint(R.string.enter_new_user_id);
        edit.setText("");
    }

    protected void confirmAll() {
        EditText nameTextView = (EditText) findViewById(R.id.group_settings_group_name_textview);
        String newGroupName = nameTextView.getText().toString();
        if (!newGroupName.equals("")) {
            providerAddNewGroup(newGroupName);
        } else {
            Toast.makeText(NewGroup.this, getString(R.string.enter_group_name), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static class ConfirmDialogFragment extends DialogFragment {
        private ActiveActivityProvider activeActivityProvider;
        private String name;
        private Button confirmButton;
        boolean clickable;
        protected void setConfirmButton(Button newButton){
            confirmButton = newButton;
        }
        protected void setNewName(String newName){
            name = newName;
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
                    activeActivityProvider.addNewGroup(name);
                    Toast.makeText(getActivity(), getString(R.string.dialog_confirm_action_processing),
                            Toast.LENGTH_LONG).show();
                }
            });
            if(confirmButton != null) {
                confirmButton.setFocusable(clickable);
                confirmButton.setClickable(clickable);
            }
            builder.setCancelable(false);
            return builder.create();
        }
    }

    protected void providerAddNewGroup(String newGroupName){

        Button confirmButton = (Button) findViewById(R.id.newgroupsubmitbutton);
        confirmButton.setFocusable(false);
        confirmButton.setClickable(false);

        ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment();
        dialogFragment.setActiveActivityProvider(provider);
        dialogFragment.setNewName(newGroupName);
        dialogFragment.setConfirmButton(confirmButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        dialogFragment.show(transaction, FRAGMENT_TRANSACTION_DIALOG);
    }

    public void showGood(UserGroup result) {
        provider.setActiveGroup(result);
        provider.makeAllMembersPossible(result);
        Intent intent = new Intent(getThisActivityContext(), Group2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void showBad(UserGroup result) {
        Button confirmButton = (Button) findViewById(R.id.newgroupsubmitbutton);
        confirmButton.setFocusable(true);
        confirmButton.setClickable(true);
    }

    public void showUserCheckGood(AddingUser result) {
        drawNewUserLayout(result, true);
        //FloatingActionButton button = (FloatingActionButton) findViewById(R.id.group_settings_add_user_fab);
        //button.setFocusable(true);
        //button.setClickable(true);
        CreateListEditText editText = (CreateListEditText) findViewById(R.id.new_user_id_edittext);
        editText.setOnEditorActionListener(editorListenerOne);
    }

    public void showUserCheckBad(AddingUser result) {
        Toast.makeText(NewGroup.this, getString(R.string.error_no_user), Toast.LENGTH_LONG)
                .show();
        //FloatingActionButton button = (FloatingActionButton) findViewById(R.id.group_settings_add_user_fab);
        //button.setFocusable(true);
        //button.setClickable(true);
        CreateListEditText editText = (CreateListEditText) findViewById(R.id.new_user_id_edittext);
        editText.setOnEditorActionListener(editorListenerOne);
    }
}