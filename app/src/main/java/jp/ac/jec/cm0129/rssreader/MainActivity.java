package jp.ac.jec.cm0129.rssreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    protected RowModelAdapter adapter;
    private ExecutorService executorService;
    Bitmap bitmap = null;
    ImageView img;

    TextView txtTitleDate;

    private static final int MENU_IT = 0;
    private static final int MENU_SPORTS = 1;
    private static final int MENU_LIFE = 2;

    private static final int MENU_END = 3;

    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new RowModelAdapter(this);

//        Looper mainLooper = getMainLooper();
//        Handler handler = HandlerCompat.createAsync(mainLooper);
//
//        executorService = Executors.newSingleThreadExecutor();


//        Button btnLoad = findViewById(R.id.loadBtn);
//        btnLoad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<RssItem> items = JsonHelper.parseJson(getData());
//                StringBuffer sb = new StringBuffer();
//                for(RssItem item: items ){
//                    sb.append(item.getTitle() + "\n");
//                    sb.append(item.getLink() + "\n");
//                }
//                TextView txt = findViewById(R.id.txtTitle);
//                txt.setText(sb.toString());
//            }
//        });
        txtTitleDate = findViewById(R.id.titleDate);



//        ArrayList<RssItem> ary = JsonHelper.parseJson(getData());
//        for(RssItem item: ary){
//            adapter.add(item);
//        }
        ListView ls = findViewById(R.id.resultList);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RssItem item = (RssItem) adapterView.getAdapter().getItem(i);
                //Toast.makeText(MainActivity.this, item.getLink(), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(item.getLink());

//                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                Intent intent = new Intent(MainActivity.this,WebActivity.class);
                intent.putExtra("webLink",item.getLink());
                Log.i("IMAGE","WEB_LINK_PARSE"+item.getLink());
                startActivity(intent);
            }
        });

//        Uri.Builder uriBuilder = new Uri.Builder();
//        uriBuilder.scheme("https");
//        uriBuilder.authority("jec-cm-linux2020.lolipop.io");
//        uriBuilder.path("test.php");
//        uriBuilder.appendQueryParameter("url", "https://news.yahoo.co.jp/rss/categories/it.xml");
//        Log.i("MainActivity", uriBuilder.build().toString());
//        AsyncHttpRequest asyncHttpRequest =new AsyncHttpRequest(handler, MainActivity.this, uriBuilder.toString());
//        executorService.submit(asyncHttpRequest);

        callUrl("https://news.yahoo.co.jp/rss/categories/sports.xml");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,MENU_IT,Menu.NONE,"IT");
        menu.add(Menu.NONE,MENU_SPORTS,Menu.NONE,"SPORTS");
        menu.add(Menu.NONE,MENU_LIFE,Menu.NONE,"LIFE");
        menu.add(Menu.NONE,MENU_END,Menu.NONE,"完了");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case MENU_IT:
                adapter.clear();
                //Toast.makeText(this, "Yahoo", Toast.LENGTH_SHORT).show();
                Log.i("my_sport_url","android_sport_url");
                callUrl("https://news.yahoo.co.jp/rss/categories/it.xml");
                return true;
            case MENU_SPORTS:
                adapter.clear();
                //Toast.makeText(this, "Google", Toast.LENGTH_SHORT).show();
                callUrl("https://news.yahoo.co.jp/rss/categories/sports.xml");
                return true;
            case MENU_LIFE:
                adapter.clear();
                //Toast.makeText(this, "Google", Toast.LENGTH_SHORT).show();
                callUrl("https://news.yahoo.co.jp/rss/categories/life.xml");
                return true;
            case MENU_END:
                finish();
                return true;
        }
        return false;
    }
    public void callUrl(String urlStr){
        Looper mainLooper = getMainLooper();
        Handler handler = HandlerCompat.createAsync(mainLooper);

        executorService = Executors.newSingleThreadExecutor();

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https");
        uriBuilder.authority("jec-cm-linux2020.lolipop.io");
        uriBuilder.path("test.php");
//        uriBuilder.appendQueryParameter("url", "https://news.yahoo.co.jp/rss/categories/it.xml");
        uriBuilder.appendQueryParameter("url", urlStr);
        Log.i("MainActivity", uriBuilder.build().toString());
        AsyncHttpRequest asyncHttpRequest =new AsyncHttpRequest(handler, MainActivity.this, uriBuilder.toString());
        executorService.submit(asyncHttpRequest);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... urls) {
            String imageUrl = urls[0];
            Drawable drawable = null;
            try {
                InputStream inputStream = (InputStream) new URL(imageUrl).getContent();
                drawable = Drawable.createFromStream(inputStream, "src name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                img.setImageDrawable(drawable);
            }
        }
    }


    private String getData() {
        String json = "";
        BufferedReader br = null;
        try {
            InputStream in = getAssets().open("rss.json");
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            json = sb.toString();
        } catch (Exception e){
            Log.e("MainActivity",Log.getStackTraceString(e));
        } finally {
            try {
                if(br != null) br.close();
            }catch (IOException e){
                Log.e("MainActivity",Log.getStackTraceString(e));
            }
        }
        return json;
    }
    public class LoadImageTaskForDownload extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTaskForDownload(ImageView imageView) {
            this.imageView = imageView;
        }

//        @Override
//        protected Bitmap doInBackground(String... params) {
//            String imageUrl = params[0];
//            // Perform the necessary steps to load the image from the provided URL
//            // Return the loaded Bitmap
//        }
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public String dateConverter(String strDate) {
        String outputFormat = "yyyy/MM/dd(EEE)HH:mm:ss";

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.JAPAN);

        try {
            Date date = inputDateFormat.parse(strDate);
            outputDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            String outputDate = outputDateFormat.format(date);


            return outputDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calculateHourDifference(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)HH:mm:ss", Locale.JAPAN);
        LocalDateTime dateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(date2, formatter);

        Duration duration = Duration.between(dateTime2, dateTime1);
        long hourDifference = duration.toHours();
        long minuteDifference = duration.toMinutes() % 60;
        long secondDifference = duration.getSeconds() % 60;

        return String.format("%d hours, %d minutes, %d seconds", hourDifference, minuteDifference, secondDifference);
    }

    public static Bitmap loadImageFromUrl(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

    class RowModelAdapter extends ArrayAdapter {

        public RowModelAdapter(@NonNull Context context) {
            super(context,R.layout.row_item);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            RssItem item = (RssItem) getItem(position);

            Log.i("RSSITEMMMMM","RSS_ITEM_BECKY"+position);
//            txtTitleDate.setText(item.getTitlePubDate());
            txtTitleDate.setText(dateConverter(item.getTitlePubDate()));

            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_item,null);
            }
            if(item != null){

                Log.i("RSSITEMMMMM","RSS_ITEM_BECKY"+item);

                TextView txtDate = convertView.findViewById(R.id.date);
                if(txtDate != null){


                    String date1 = dateConverter(item.getTitlePubDate());
                    String date2 = dateConverter(item.getPubDate());
                    //Log.i("RSSITEMMMMM","RSS_ITEM_BECKY"+dateConverter(item.getTitlePubDate()) +"D "+
                            dateConverter(item.getPubDate());

                    String hourDifference = calculateHourDifference(date1, date2);
//                    Log.i("RSSITEMMMMM","RSS_ITEM_BECKY"+hourDifference);

                    //txtDate.setText(dateConverter(item.getPubDate())+" "+hourDifference);
                    txtDate.setText(hourDifference+" ago");
                }

                TextView txtTitle = convertView.findViewById(R.id.txtTitle);
                if(txtTitle != null){
                    txtTitle.setText(item.getTitle());
                }
//                TextView txtLink = convertView.findViewById(R.id.txtLink);
//                if(txtLink != null){
//                    txtLink.setText(String.valueOf(item.getLink()));
//                }

                ImageButton imgLink = convertView.findViewById(R.id.img);
                imgLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String imageUrl = item.getImageLink();

                        // Create a Dialog
                        Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.popup_image_dialog);
                        ImageView popupImage = dialog.findViewById(R.id.popupImage);

                        // Load the image from the provided URL using a background thread
                        new LoadImageTaskForDownload(popupImage).execute(imageUrl);

                        // Set the desired dimensions for the popup dialog
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        // Show the dialog
                        dialog.show();
                    }
                });
                //Log.i("RSSITEMMMMM","RSS_ITEM_BECKY"+item.getImageLink());
                if (imgLink != null) {

                    String imageUrl = item.getImageLink();
                   // String imageUrl = "https://newsatcl-pctr.c.yimg.jp/t/amd-img/20230601-00118433-phileweb-000-1-view.jpg?pri=l&w=450&h=450&exp=10800";
                    new DownloadImageTask(imgLink).execute(imageUrl);
                }
                next = convertView.findViewById(R.id.nextBtn);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this,WebActivity.class);
                        intent.putExtra("webLink",item.getLink());
                        Log.i("IMAGE","WEB_LINK_PARSE"+item.getLink());
                        startActivity(intent);
                    }
                });

            }
            return convertView;
        }
    }
    public void onClickNext(){


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Next", Toast.LENGTH_SHORT).show();
            }
        });
    }
}