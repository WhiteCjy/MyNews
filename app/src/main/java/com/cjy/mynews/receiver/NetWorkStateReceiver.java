package com.cjy.mynews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by CJY on 2016/11/28.
 */

public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取系统的网络管理器
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取当前正在活动的网络状态
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(info==null){
            Toast.makeText(context, "网络已断开", Toast.LENGTH_LONG).show();
        }else{
            //如果可以上网，区分是wifi还是数据
            if(info.isConnected()){
                int type=info.getType();
                switch (type){
                    case ConnectivityManager.TYPE_WIFI:
                        Toast.makeText(context, "WIFI网络已连接", Toast.LENGTH_LONG).show();
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        Toast.makeText(context, "移动数据网络已连接", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
