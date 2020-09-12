package com.twilio.video.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;


public class WebViewPage extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    String skillAndClassId,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);
        setui();
        Sprite foldingCube = new FoldingCube();
        foldingCube.setColor(Color.parseColor("#2073CC"));
        foldingCube.setScale(0.7f);
        progressBar.setIndeterminateDrawable(foldingCube);
        setWebView();
        skillAndClassId  = getIntent().getStringExtra("url_pay").split("=")[1];
        uid = getIntent().getStringExtra("uid");
        webView.loadUrl(getIntent().getStringExtra("url_pay"));



    }

    private void setWebView() {
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                Log.d("Pay_Url>>",url);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },1000);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.equalsIgnoreCase("https://virtualskill.in/api/pay-success/"+skillAndClassId+"/"+uid))
                {
                    Toast.makeText(WebViewPage.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(WebViewPage.this,HomePage.class));
                        }
                    },100);
                }



                super.onPageFinished(view, url);
            }
        });
    }

    private void setui() {
        webView = findViewById(R.id.web_v_pay);
        progressBar = findViewById(R.id.pb_payments);
    }
}