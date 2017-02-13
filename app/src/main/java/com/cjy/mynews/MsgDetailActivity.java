package com.cjy.mynews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MsgDetailActivity extends AppCompatActivity {
   private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_detail);
        webView= (WebView) findViewById(R.id.webView_msg);
        //打开界面时，自适应屏幕
        WebSettings webSettings=webView.getSettings();
        webSettings.setUseWideViewPort(true);
        int position = getIntent().getIntExtra("position",0);
        String url=getUrlByPosition(position);
        webView.loadUrl(url);

    }
    private String getUrlByPosition(int position){
        String oldurl =getIntent().getStringExtra("url");
        StringBuilder url=new StringBuilder();
    //获取网页形式路径中的数字
        String num=getMainNum(oldurl);
        switch (position){
            case 0://要闻
                    url=url.append("http://inews.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 1://财经
                //http://istock.ifeng.com/14868184/news.shtml
                url = url.append("http://istock.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 2://奥运
                //http://isports.ifeng.com/49938164/news.shtml
                url = url.append("http://isports.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 3://军事
                url = url.append("http://imil.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 4://科技
                url = url.append("http://itech.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 5://历史
                //http://ihistory.ifeng.com/?srctag=xzydh12
                url = url.append("http://ihistory.ifeng.com/").append(num).append("/news.shtml");
                break;
        }
        return url.toString();
    }

    private String getMainNum(String oldurl){
        oldurl=oldurl.substring(oldurl.lastIndexOf("/")+1,oldurl.lastIndexOf("."));
        if(oldurl.contains("-")){
            oldurl=oldurl.substring(0,oldurl.lastIndexOf("-"));
        }
        return oldurl;
    }
}
