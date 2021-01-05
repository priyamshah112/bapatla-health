package com.droiduino.companionappcourse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HeatMapWebview  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heatmap_webview);

        // final TextView heading = findViewById(R.id.heading);
        // heading.setText("Did you know");

        //APP BAR PROPERTIES
         getSupportActionBar().hide(); // hides appbar
//        getSupportActionBar().setTitle("DEVICE SETUP");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //displays back button on app bar

        WebView w = (WebView) findViewById(R.id.heatMapWebview);

        // loading http://www.google.com url in the the WebView.
        w.loadUrl("https://bapfoundation.org");

        // this will enable the javascipt.
        w.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        w.setWebViewClient(new WebViewClient());
    }
}