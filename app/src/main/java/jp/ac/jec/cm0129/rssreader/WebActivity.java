package jp.ac.jec.cm0129.rssreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

    private WebView wView;

    private static final int MENU_YAHOO = 0;
    private static final int MENU_GOOGLE = 1;
    private static final int MENU_END = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        wView = findViewById(R.id.wView);

        String link = getIntent().getStringExtra("webLink");

        Log.i("web_link","web_link_link"+link);

        wView.getSettings().setJavaScriptEnabled(true);
        wView.setWebViewClient(new WebViewClient());
        wView.loadUrl(link);
    }

}