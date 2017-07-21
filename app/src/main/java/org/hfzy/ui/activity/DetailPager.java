package org.hfzy.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.example.hfzy.R;

import org.hfzy.bean.ItemDtail;
import org.hfzy.globle.SeviceUrl;
import org.hfzy.util.Cache;
import org.hfzy.util.HtmlUtil;
import org.hfzy.util.OkHttpUtil;
import org.hfzy.util.UIUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DetailPager extends AppCompatActivity {

    private WebView vb;
    private int id;
    private String body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detilger);
        id = getIntent().getIntExtra("id", 0);
        initHtml();
        vb = (WebView) findViewById(R.id.webView);
    //    vb.loadUrl(SeviceUrl.DETAILURL+ id);



//        WebSettings settings = vb.getSettings();
//        settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
//        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
//        settings.setJavaScriptEnabled(true);// 支持js功能

        vb.setWebViewClient(new WebViewClient() {
            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("开始加载网页了");
             //   pbLoading.setVisibility(View.VISIBLE);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
              //  pbLoading.setVisibility(View.INVISIBLE);
            }

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载
                return true;
            }
        });

        // mWebView.goBack();//跳到上个页面
        // mWebView.goForward();//跳到下个页面

        vb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 进度发生变化
                System.out.println("进度:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 网页标题
                System.out.println("网页标题:" + title);
            }
        });
    }

    private void initHtml() {
        OkHttpUtil.sendOkHttpRequest(SeviceUrl.DETAILURL + id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Cache.SetChche(SeviceUrl.DETAILURL,result, UIUtils.getContext());
                Log.e("result------------------------------:",result);
                initBody( result);
            }


        });
    }

    private void initBody(String result) {

        ItemDtail itemDtail = JSON.parseObject(result, ItemDtail.class);
        final String html = HtmlUtil.structHtml(itemDtail);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vb.loadDataWithBaseURL("file:///android_asset/",html, "text/html", "UTF-8", null);
            }
        });

    }
}

