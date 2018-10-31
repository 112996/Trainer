package com.example.asus_pc.trainer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.asus_pc.trainer.Fragment.fragment_course;
import com.example.asus_pc.trainer.Fragment.fragment_lineShow;
import com.example.asus_pc.trainer.Fragment.fragment_me;
import com.example.asus_pc.trainer.R;

import java.util.ArrayList;
import java.util.List;

public class LineShowActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager pager;
    private List<Fragment> fragments;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_show);

        initView();
    }

    private void initView(){
        pager = findViewById(R.id.viewPager);
        radioGroup = findViewById(R.id.radioGroup);
        findViewById(R.id.lineShow).setOnClickListener(this);
        findViewById(R.id.course).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);

        fragments =  new ArrayList<Fragment>();
        fragments.add(new fragment_lineShow());
        fragments.add(new fragment_course());
        fragments.add(new fragment_me());

        pager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        pager.setOnPageChangeListener(this);
        radioGroup.check(R.id.lineShow);

    }

    @Override
    public void finish(){
        ViewGroup view  = (ViewGroup)getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i){
            case 0:
                radioGroup.check(R.id.lineShow);
                break;
            case 1:
                radioGroup.check(R.id.course);
                break;
            case 2:
                radioGroup.check(R.id.me);
                break;
             default:
                 break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lineShow:
                pager.setCurrentItem(0,true);
                break;
            case R.id.course:
                pager.setCurrentItem(1,true);
                break;
            case R.id.me:
                pager.setCurrentItem(2,true);
                break;
             default:
                 break;

        }

    }
}
