package com.cjy.mynews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjy.mynews.Adapters.HeadAdapter;
import com.cjy.mynews.Adapters.ListAdapter;
import com.cjy.mynews.Bean.News;
import com.cjy.mynews.MsgDetailActivity;
import com.cjy.mynews.R;
import com.cjy.mynews.network.NetControl;
import com.cjy.mynews.utils.ProgressDialogUtils;
import com.cjy.mynews.view.AdvertismentContainer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJY on 2016/11/25.
 */

public class MyFragment extends Fragment implements NetControl.onCallbackResultListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final int SEND_DATA = 0x111;
    private static final int UPDATA = 0x112;
    private View mFragmentView;
    private ListView tListView;
    private View mAdvertist; //轮播图的根View
    private NetControl netControl;
    private int mPosition = 0;//记录当前Fragment的位置
    private ProgressDialogUtils progressDialogUtils;
    private boolean isFirstRequest = true;//判断是否是第一次请求网络
    private int loadNum=9;//初始加载的条数
    //回调回来的数据
    List<News>[] allData = new ArrayList[6];
    private TextView footTv;//底部的tv

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_layout, container, false);
        netControl = new NetControl(getContext());
        progressDialogUtils = new ProgressDialogUtils();
            //联网请求数据
        progressDialogUtils.showProgressDialog(getActivity(), null);
        netControl.getNetData(this);
        initCreatView(mFragmentView, inflater);
        return mFragmentView;
    }

    private View footContanier;
    private ListAdapter listAdapter;

    private void initCreatView(View v, LayoutInflater inflater) {
        Log.d("info","initCreatView");
        tListView = (ListView) v.findViewById(R.id.fragment_list);
        mAdvertist = inflater.inflate(R.layout.head_layout, tListView, false);
        footContanier = inflater.inflate(R.layout.foot_layout, tListView, false);
        footTv= (TextView) footContanier.findViewById(R.id.foot_tv);
        //找到轮播图的view启动
        startAdvertise();
        listAdapter = new ListAdapter(myData,getActivity());
        //添加头部
        tListView.addHeaderView(mAdvertist);
        //添加尾部
        tListView.addFooterView(footContanier);
        //设置适配器
        tListView.setAdapter(listAdapter);
        //监听滚动
        tListView.setOnScrollListener(this);
        tListView.setOnItemClickListener(this);

    }
    //轮播图
    private void startAdvertise() {
        AdvertismentContainer advertismentContainer = (AdvertismentContainer) mAdvertist.findViewById(R.id.auToImg);
        HeadAdapter headAdapter = new HeadAdapter();
        advertismentContainer.setPointDrawableRes(R.drawable.point_bg);
        advertismentContainer.setAdapter(headAdapter);
        advertismentContainer.startAutoPlay();
    }
    //更新位置
    public void upDataPosition(int position) {
        mPosition = position;
        initData();
        if(myData!=null){
            handler.sendEmptyMessage(SEND_DATA);
        }
    }
    //初始化数据
    private  void initData() {
        if(allData[mPosition]!=null){
            ArrayList<News> data= (ArrayList<News>) allData[mPosition];
            //清除缓存
            myData.clear();
            for (int i = 0; i <loadNum; i++) {
                if(i>99){
                    return;
                }
                myData.add(data.get(i));
                handler.sendEmptyMessage(SEND_DATA);
            }
            Log.d("info","数据更新了");
        }
    }
    //每一组显示的数据
    ArrayList<News> myData=new ArrayList<>();
    private MyHandler handler = new MyHandler(this);
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if(firstVisibleItem+visibleItemCount==totalItemCount){
                if(isGetNext||myData.size()>=99){
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNum+=4;
                        initData();
                    }
                },1200);
                isGetNext=true;
            }
        }
    }
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isGetNext;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }




    @Override
    public void callBacklistener(List<News>[] allData) {
        Log.d("info","callBacklistener");
        this.allData = allData;
        initData();
        progressDialogUtils.closeProgressDialog();
        handler.sendEmptyMessage(SEND_DATA);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View viem, int position, long id) {
        String url = myData.get(position-1).getUrl();
        Intent intent=new Intent(getActivity(), MsgDetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("position",mPosition);
        startActivity(intent);
    }

    private static class MyHandler extends Handler {
        private WeakReference<MyFragment> weakReference;

        public MyHandler(MyFragment fragment) {
            weakReference = new WeakReference<MyFragment>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            final MyFragment fragment = weakReference.get();
            if (fragment != null) {
                if (msg.what == SEND_DATA) {
                    Log.d("info","myData"+fragment.myData.size());
                    fragment.isGetNext=false;
                    fragment.listAdapter.notifyDataSetChanged();
                    if(fragment.myData.size()>=100){
                       fragment.footTv.setText("没有更多数据");
                    }
                }
            }
        }

    }
}
