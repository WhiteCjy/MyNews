package com.cjy.mynews.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cjy.mynews.Bean.News;
import com.cjy.mynews.R;
import com.cjy.mynews.network.NetControl;

import java.util.ArrayList;

/**
 * Created by CJY on 2016/11/26.
 */

public class ListAdapter extends BaseAdapter implements NetControl.onCallbackImageListener {
    private ArrayList<News> newsData=new ArrayList<>();
    private Activity context;
    private NetControl mNetControl;
    //返回图片的大小
    int size= (int) (Runtime.getRuntime().maxMemory()/8);
    private LruCache<String,Bitmap> bitmapLruCache=new LruCache<String, Bitmap>(size){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public ListAdapter(ArrayList<News> data,Context context){
        this.context= (Activity) context;
        mNetControl=new NetControl(context);
        this.newsData=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return newsData.size();
    }

    @Override
    public Object getItem(int position) {
        return newsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
            ViewHolder holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder= (ViewHolder) convertView.getTag();
        News news=newsData.get(position);
        holder.itemTv.setText(news.getTitle());
        Bitmap bitmap=bitmapLruCache.get(news.getThumbnail());
        if(bitmap==null){
            holder.itemImg.setImageResource(R.mipmap.ic_launcher);
            //记录一个标志,用图片的uri
            holder.itemImg.setTag(news.getThumbnail());
            mNetControl.getImage(news.getThumbnail(),this,holder.itemImg);

        }else{
            holder.itemImg.setImageBitmap(bitmap);
        }
        return convertView;
    }

    @Override
    public void callBackImageListner(final String path, final Bitmap bitmap, final ImageView imageView) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(bitmap==null){
                    Toast.makeText(context, "图片加载错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                bitmapLruCache.put(path,bitmap);
                if(path.equals(imageView.getTag())){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

    }

    class ViewHolder{
        private ImageView itemImg;
        private TextView itemTv;
        public ViewHolder(View converView){
            itemImg= (ImageView) converView.findViewById(R.id.item_img);
            itemTv= (TextView) converView.findViewById(R.id.item_tv);
        }
    }
}
