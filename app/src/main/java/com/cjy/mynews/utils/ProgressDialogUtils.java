package com.cjy.mynews.utils;

import android.app.Activity;

/**
 * Created by CJY on 2016/9/27.
 * 弹窗工具类
 */
public class ProgressDialogUtils {
    private  CommonProgressDialog dialog;
    private Activity activity;
    //显示方法
    public void showProgressDialog(Activity activity, String msg){
        this.activity=activity;
        if(dialog==null){
            dialog=new CommonProgressDialog(activity);
        }
        if(msg==null){
            msg="";
        }
        if(!activity.isFinishing()&&!dialog.isShowing()){
            dialog.show();
        }
        dialog.setMessage(msg);

    }


    //关闭方法
    public void closeProgressDialog(){
        if(dialog!=null&&!activity.isFinishing()){
            dialog.dismiss();
            dialog=null;
        }
    }


}
