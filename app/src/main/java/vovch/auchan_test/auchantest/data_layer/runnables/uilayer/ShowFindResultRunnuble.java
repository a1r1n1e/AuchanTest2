package vovch.auchan_test.auchantest.data_layer.runnables.uilayer;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.LinearLayout;

import vovch.auchan_test.auchantest.ActiveActivityProvider;
import vovch.auchan_test.auchantest.data_types.TempItem;

public class ShowFindResultRunnuble implements Runnable {
    private ActiveActivityProvider provider;
    private TempItem[] result;
    private TempItem addTo;

    public ShowFindResultRunnuble(TempItem[] result, ActiveActivityProvider provider, TempItem addTo) {
        this.result = result;
        this.provider = provider;
        this.addTo = addTo;
    }


    @Override
    public void run() {
        try {
            provider.showResultInAuchanBase(result, addTo);
        } catch (Exception e) {
            Log.d("WhoBuys", "OfflineGetterTaskDE");
        }
    }
}
