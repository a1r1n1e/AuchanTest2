package vovch.auchan_test.auchantest.data_types;

import android.support.v7.widget.CardView;
import android.widget.LinearLayout;

/**
 * Created by vovch on 10.01.2018.
 */

public class TempItem extends Item {
    private CreateListEditText itemNameEditText;
    private CreateListEditText itemCommentEditText;
    private CardView cardView;
    private LinearLayout tempItemLayout;
    public TempItem(){
        super(null, null);
        itemCommentEditText = null;
        itemNameEditText = null;
        cardView = null;
    }
    public TempItem(String newName, String newComment){
        super(newName, newComment);
        itemCommentEditText = null;
        itemNameEditText = null;
        cardView = null;
    }
    public void setTempItemLayout(LinearLayout tempItemLayout){
        this.tempItemLayout = tempItemLayout;
    }

    public LinearLayout getTempItemLayout() {
        return tempItemLayout;
    }

    public void setItemNameEditText(CreateListEditText editText){
        itemNameEditText = editText;
    }
    public void setItemCommentEditText(CreateListEditText editText){
        itemCommentEditText = editText;
    }
    public CreateListEditText getNameEditText(){
        return itemNameEditText;
    }
    public CreateListEditText getItemCommentEditText(){
        return itemCommentEditText;
    }
    public void setCardView(CardView newCardView){
        cardView = newCardView;
    }
    public CardView getCardView(){
        return cardView;
    }
}
