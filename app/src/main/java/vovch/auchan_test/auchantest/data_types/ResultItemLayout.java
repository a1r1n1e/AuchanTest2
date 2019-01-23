package vovch.auchan_test.auchantest.data_types;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ResultItemLayout extends LinearLayout {
    private TempItem item;
    private TempItem addTo;
    private Item trueItem;
    private LinearLayout layout;
    public ResultItemLayout(Context context){
        super(context);
    }
    public ResultItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ResultItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Item getTrueItem() {
        return trueItem;
    }

    public void setTrueItem(Item trueItem) {
        this.trueItem = trueItem;
    }

    public TempItem getAddTo() {
        return addTo;
    }

    public void setAddTo(TempItem addTo) {
        this.addTo = addTo;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public TempItem getItem() {
        return item;
    }

    public void setItem(TempItem item) {
        this.item = item;
    }
}
