package com.cjy.mynews.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjy.mynews.R;
import com.cjy.mynews.view.AdvertisementAdapter;

/**
 * Created by CJY on 2016/11/25.
 */

public class HeadAdapter extends AdvertisementAdapter {
    private int[] imgRes={R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4};

    public void setImgRes(int[] imgRes){
        this.imgRes=imgRes;
    }
    @Override
    public int getRealCount() {
        return imgRes.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=new ImageView(container.getContext());
        imageView.setImageResource(imgRes[position%imgRes.length]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
