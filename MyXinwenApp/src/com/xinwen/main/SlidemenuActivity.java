package com.xinwen.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.viewpagerindicator.TabPageIndicator;

public class SlidemenuActivity extends FragmentActivity {

	//新闻栏目
	private static final String[] TITLE = new String[]{"热点","财经","科技","娱乐","体育","国际","热点","股票","更多"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉Activity的标题栏
		setContentView(R.layout.activity_slidemenu);
		
		//ViewPager的adapter,ViewPager是指下方的内容（本程序中是指新闻内容的部分），一页新闻内容这里是一个Fragment
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(adapter);//通过adapter为ViewPager提供数据
		
		//实例化TabPageIndicator,然后设置ViewPager与之关联
		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		//对ViewPager进行监听
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {	
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {	
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
							
			}
		});
	}
	
	
	class TabPageIndicatorAdapter extends FragmentPagerAdapter{

		public TabPageIndicatorAdapter(FragmentManager fm) {
			
			super(fm);
		}
		@Override
			public Fragment getItem(int position) {
			//新建一个Fragment来展示ViewPager item的内容，并传递参数  
            Fragment fragment = new ContentFragment();    
            Bundle args = new Bundle();    //Bundle类的作用是可以在Activity之间传递数据
          //  args.putString("arg", TITLE[position]);    
           // fragment.setArguments(args);    //用Fragment里的setArgument方法传递参数
              args.putInt("arg",position);
              fragment.setArguments(args);
            return fragment;  
		}

		 @Override
	        public CharSequence getPageTitle(int position) {
	            return TITLE[position % TITLE.length].toUpperCase();  //直接用适配器来完成标题的显示,
	        }

		 @Override
		 	public int getCount() {
			 
			 	return TITLE.length;
		}
		
	}




}
