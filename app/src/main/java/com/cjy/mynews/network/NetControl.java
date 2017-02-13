package com.cjy.mynews.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.cjy.mynews.Bean.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CJY on 2016/11/26.
 */

public class NetControl {
    //返回数据给指定fragment
    public interface onCallbackResultListener{
        void callBacklistener( List<News>[] allData);
    }
    //回调图片的接口
    public interface onCallbackImageListener{
        void callBackImageListner(String path, Bitmap bitmap, ImageView imageView);
    }
    private Context context;
    private File cacheDir;//图片缓存目录
    private ExecutorService executors;//线程池
    public NetControl(Context context){
        this.context=context;
        cacheDir=context.getCacheDir();
        executors=Executors.newFixedThreadPool(5);
    }
    //请求网络获取图片
    public void getImage(final String path, final onCallbackImageListener listener, final ImageView imageView){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                String fileName=path.substring(path.lastIndexOf("/")+1);
                File file=new File(cacheDir,fileName);
                if(file.exists()){
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    listener.callBackImageListner(path,bitmap,imageView);
                }else{
                    fromNetImg(path,listener,imageView);
                }
            }
        });
    }
    //联网获取图片
    private void fromNetImg(final String path, final onCallbackImageListener listener, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        InputStream is=conn.getInputStream();
                        Bitmap bitmap= BitmapFactory.decodeStream(is);
                        //回調結果
                        listener.callBackImageListner(path,bitmap,imageView);
                        //將圖片存到本地
                        String fileName=path.substring(path.lastIndexOf("/")+1);//文件名
                        File file=new File(cacheDir,fileName);
                        FileOutputStream fos=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.callBackImageListner(path,null,imageView);
                }

            }
        }).start();
    }
    //请求网络获取数据
    public void getNetData(final onCallbackResultListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
                //2.新建一个请求
                Request request= new Request.Builder().url("http://news.ifeng.com/").build();
                //3.执行请求，获取响应数据
                Call call= okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            //请求成功，获取到数据
                            String str=response.body().string();
                            //得到指定json数据
                           String json= getJson(str);
                            try {
                                //得到每个fragment上的数据
                                List<News>[] allData=parseJson(json);
                                //设置数据回去
                                listener.callBacklistener(allData);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.callBacklistener(null);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    //截取返回数据中的json格式数据
    private String getJson(String str) {
        String json=null;
        if(str!=null){
            //indexof不包含前面的字符，所以后边要加3个字符
            json=str.substring(str.indexOf("[[{"),str.indexOf("}]]")+3);
        }
        return json;
    }
    //解析数据
    private ArrayList<News>[] parseJson(String json) throws JSONException {
        List<News>[] allData;
        JSONArray jsonArray=new JSONArray(json);
        allData=new ArrayList[jsonArray.length()];
        for (int i = 0; i <jsonArray.length() ; i++) {
            allData[i]=new ArrayList<News>();
            JSONArray jsonArray2=jsonArray.getJSONArray(i);
            for (int j = 0; j <jsonArray2.length() ; j++) {
                JSONObject jsonObject=jsonArray2.getJSONObject(j);
                News data=new News();
                data.setThumbnail(jsonObject.getString("thumbnail"));
                data.setTitle(jsonObject.getString("title"));
                data.setUrl(jsonObject.getString("url"));
                allData[i].add(data);
            }

        }
        //返回解析过后的所有数据
        return (ArrayList<News>[]) allData;

    }
}
