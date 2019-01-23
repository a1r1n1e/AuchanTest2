package vovch.auchan_test.auchantest.data_types;

import android.graphics.Bitmap;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by vovch on 23.12.2017.
 */

public class Item /*implements Parcelable*/ {
    private int id;
    private String comment;

    private String name;
    private Bitmap image;
    private String description;
    private String compound;
    private String price;
    private String auchanId;

    private SList list;
    private boolean state;
    private LinearLayout layout;
    private LinearLayout verticalLayout;
    private ItemButton button;
    private String owner;
    private String ownerName;
    private TextView ownerTextView;
    public Item(int newId, String newName, String newComment, boolean newState){
        id = newId;
        name = newName;
        comment = newComment;
        state = newState;
        owner = null;
        ownerName = null;
        this.image = null;
        this.description = "";
        this.compound = "";
        this.auchanId = "";
        this.price = "";
        this.image = null;
    }
    public Item(String newName, String newComment, boolean newState){
        id = 0;
        name = newName;
        comment = newComment;
        state = newState;
        owner = null;
        ownerName = null;
        this.image = null;
        this.description = "";
        this.compound = "";
        this.auchanId = "";
        this.price = "";
        this.image = null;
    }
    public Item(String newName, String newComment) {
        id = 0;
        state = true;
        name = newName;
        comment = newComment;
        owner = null;
        ownerName = null;
        this.image = null;
        this.description = "";
        this.compound = "";
        this.auchanId = "";
        this.price = "";
        this.image = null;
    }
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public void clear(){
        layout = null;
        button = null;
    }

    public String getAuchanId() {
        return auchanId;
    }

    public void setAuchanId(String auchanId) {
        this.auchanId = auchanId;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCompound() {
        return compound;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId(){
        return id;
    }
    public void setId(int newId){
        id = newId;
    }
    public boolean getState(){
        return  state;
    }
    public void setState(boolean newState){
        state = newState;
    }
    public String getName(){
        return name;
    }
    public void setName(String newName){
        name = newName;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String newComment){
        comment = newComment;
    }
    public void setList(SList newList){
        list = newList;
    }
    public SList getList(){
        return list;
    }
    public void setLayout(LinearLayout newLayout){
        layout = newLayout;
    }
    public LinearLayout getLayout(){
        return layout;
    }
    public void setButton(ItemButton newButton){
        button = newButton;
    }
    public ItemButton getButton(){
        return button;
    }
    public void setOwner(String newOwner){
        owner = newOwner;
    }
    public String getOwner(){
        return owner;
    }
    public void setOwnerName(String newOwnerName){
        ownerName = newOwnerName;
    }
    public String getOwnerName(){
        return ownerName;
    }
    public void setOwnerTextView(TextView newTextView){
        ownerTextView = newTextView;
    }
    public TextView getOwnerTextView(){
        return ownerTextView;
    }
    public void setVerticalLayout(LinearLayout newLayout){
        verticalLayout = newLayout;
    }
    public LinearLayout getVerticalLayout(){
        return verticalLayout;
    }
}
