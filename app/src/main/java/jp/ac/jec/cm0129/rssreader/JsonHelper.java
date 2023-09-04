package jp.ac.jec.cm0129.rssreader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//public class JsonHelper {
//    public static ArrayList<RssItem> parseJson(String strJson){
//        ArrayList<RssItem> list = new ArrayList<>();
//        try{
//            JSONObject json = new JSONObject(strJson);
//            JSONObject channel = json.getJSONObject("channel");
//            String titlePubDate = channel.getString("pubDate");
//            JSONArray entries = channel.getJSONArray("item");
//            for(int i = 0; i< entries.length();i++){
//                JSONObject entry = entries.getJSONObject(i);
//                list.add(parseToItem(entry,titlePubDate));
//            }
//        }catch (Exception e){
//            Log.e("JsonHelper",e.getMessage());
//        }
//        return list;
//    }
//    public static RssItem parseToItem(JSONObject json,String titlePubDate) throws JSONException {
//        RssItem item = new RssItem();
//        item.setTitlePubDate(titlePubDate);
//        item.setPubDate(json.getString("pubDate"));
//        item.setTitle(json.getString("title"));
//        item.setLink(json.getString("link"));
//        item.setImageLink(json.getString("image"));
//
//        return item;
//    }
//}

public class JsonHelper {
    public static ArrayList<RssItem> parseJson(String strJson){
        ArrayList<RssItem> list = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(strJson);
            JSONObject channel = json.getJSONObject("channel");
            String pubDate = channel.getString("pubDate");
            JSONArray entries = channel.getJSONArray("item");
            for(int i = 0; i < entries.length(); i++){
                JSONObject entry = entries.getJSONObject(i);
                list.add(parseToItem(entry, pubDate));
            }
        }catch (Exception e){
            Log.e("JsonHelper", e.getMessage());
        }
        return list;
    }

    public static RssItem parseToItem(JSONObject json, String pubDate) throws JSONException {
        RssItem item = new RssItem();
        item.setTitlePubDate(pubDate);
        item.setTitle(json.getString("title"));
        item.setLink(json.getString("link"));
        item.setPubDate(json.getString("pubDate"));
        item.setImageLink(json.getString("image"));


        return item;
    }
}
