package com.cjy.mynews;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cjy.mynews.Adapters.TabAdapter;
import com.cjy.mynews.fragments.MyFragment;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;


public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
        private ViewPager mViewPager;
        private TabLayout mTabTitle;
        private String[] mListTitle;//存放标题的数组
        private List<MyFragment> mFragments;//存放Fragment的集合
        private TabAdapter mTabAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        mTabTitle= (TabLayout) findViewById(R.id.tab_title);
        mListTitle=getResources().getStringArray(R.array.tab_title);
        mFragments=new ArrayList<MyFragment>();
        for (int i = 0; i <mListTitle.length ; i++) {
            MyFragment fragment=new MyFragment();
            //根据标题数创建MyFragment
            mFragments.add(fragment);
        }
        mTabAdapter=new TabAdapter(getSupportFragmentManager(),mFragments,mListTitle);
        mViewPager.setAdapter(mTabAdapter);
        //标题绑定viewpager
        mTabTitle.setupWithViewPager(mViewPager);
        mTabTitle.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("info","onTabSelected"+fragment);
        int position=tab.getPosition();
        MyFragment fragment= mFragments.get(position);
        fragment.upDataPosition(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
