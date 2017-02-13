package com.cjy.mynews.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cjy.mynews.fragments.MyFragment;

import java.util.List;

/**
 * Created by CJY on 2016/11/25.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private List<MyFragment> mFragments;//存放Fragment的集合
    private String[] mListTltle;//存放标题 的数组

    public TabAdapter(FragmentManager fm, List<MyFragment> mFragments, String[] mListTltle) {
        super(fm);
        this.mFragments=mFragments;
        this.mListTltle=mListTltle;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
    //返回页面的数量
    @Override
    public int getCount() {
        return mFragments.size();
    }
    //获取指定页面上的标题来显示
    @Override
    public CharSequence getPageTitle(int position) {
        return mListTltle[position];
    }
}
