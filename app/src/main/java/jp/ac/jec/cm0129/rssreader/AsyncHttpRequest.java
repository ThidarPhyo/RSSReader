package jp.ac.jec.cm0129.rssreader;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AsyncHttpRequest implements Runnable {

    private Handler handler;

    private MainActivity mainActivity;
    private String urlStr,resStr;

    public AsyncHttpRequest(Handler handler, MainActivity mainActivity, String urlStr) {
        this.handler = handler;
        this.mainActivity = mainActivity;
        this.urlStr = urlStr;
    }

    @Override
    public void run() {
        Log.i("RSSReader","BackgroundTask Start.....");

        resStr = "取得に失敗しました。";
        //setup
        HttpsURLConnection connection = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            resStr = inputStreamToString(connection.getInputStream());
            Log.i("TEST",resStr);
        } catch (Exception e){
            e.printStackTrace();
            Log.e("AsyncHttpRequest",e.toString());
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                //running Background
                onPostExecute();
            }
        });
    }

    private void onPostExecute() {
        //call this after background
        Log.i("RssReader", "onPostExecute start...");
        ArrayList<RssItem> ary = JsonHelper.parseJson(resStr);
        for(RssItem tmp: ary){
            mainActivity.adapter.add(tmp);

        }
        ListView list = mainActivity.findViewById(R.id.resultList);
        list.setAdapter(mainActivity.adapter);
    }

    //add data
    //change data to string
    private String inputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
