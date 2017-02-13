package com.cjy.mynews.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.cjy.mynews.R;

/**
 * Created by CJY on 2016/9/27.
 * 自定义加载弹窗样式
 */
public class CommonProgressDialog extends Dialog{
    public CommonProgressDialog(Context context) {
        super(context, R.style.commonProgressDialog);
        setContentView(R.layout.commonprogressdialog);
        //显示在屏幕中间
        getWindow().getAttributes().gravity= Gravity.CENTER;
    }
    //设置加载消息的方法
    public void setMessage(String s){
        TextView textView= (TextView) this.findViewById(R.id.tv_loading);
        textView.setText(s);
    }
}
