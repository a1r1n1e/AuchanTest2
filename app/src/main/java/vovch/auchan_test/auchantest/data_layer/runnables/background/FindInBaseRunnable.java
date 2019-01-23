package vovch.auchan_test.auchantest.data_layer.runnables.background;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import vovch.auchan_test.auchantest.ActiveActivityProvider;
import vovch.auchan_test.auchantest.data_layer.DataBaseTask2;
import vovch.auchan_test.auchantest.data_layer.WebCall;
import vovch.auchan_test.auchantest.data_layer.runnables.uilayer.DBUpdateOneGroupOnUITask;
import vovch.auchan_test.auchantest.data_layer.runnables.uilayer.ShowFindResultRunnuble;
import vovch.auchan_test.auchantest.data_types.TempItem;
import vovch.auchan_test.auchantest.data_types.UserGroup;

public class FindInBaseRunnable implements Runnable {
    private String request;
    public ActiveActivityProvider provider;
    private TempItem addTo;

    public FindInBaseRunnable(String request, ActiveActivityProvider provider, TempItem addTo){
        this.provider = provider;
        this.request = request;
        this.addTo = addTo;
    }

    @Override
    public void run() {
        try{
            String response = "";
            HttpURLConnection conn;
            URL url = new URL("http://www.whobuys.ru/auchan_test/test_search.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);                                                 // Enable POST stream
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            HashMap<String, String> postDataParams = new HashMap<String, String>();

            postDataParams.put("name", request);

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

            JSONArray jsonArray = new JSONArray(response.substring(3));
            int length = jsonArray.length();
            TempItem[] result = new TempItem[length];
            for(int i = 0; i < length; i++){
                JSONObject tempItemJSON = (JSONObject) jsonArray.get(i);
                TempItem tempItem = new TempItem();
                tempItem.setName(tempItemJSON.getString("name"));
                tempItem.setImage(WebCall.getBitmapFromURL(tempItemJSON.getString("image_link")));
                tempItem.setDescription(tempItemJSON.getString("description"));
                tempItem.setCompound(tempItemJSON.getString("compound"));
                tempItem.setPrice(tempItemJSON.getString("price"));
                tempItem.setAuchanId(tempItemJSON.getString("id"));

                result[i] = tempItem;
            }
            Handler handler = new Handler(Looper.getMainLooper());
            ShowFindResultRunnuble runnuble = new ShowFindResultRunnuble(result, provider, addTo);
            handler.post(runnuble);

        } catch (Exception e){
            Log.d("AuchanTest2", "find in base");
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
