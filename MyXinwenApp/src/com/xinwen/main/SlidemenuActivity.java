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

	//������Ŀ
	private static final String[] TITLE = new String[]{"�ȵ�","�ƾ�","�Ƽ�","����","����","����","�ȵ�","��Ʊ","����"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��Activity�ı�����
		setContentView(R.layout.activity_slidemenu);
		
		//ViewPager��adapter,ViewPager��ָ�·������ݣ�����������ָ�������ݵĲ��֣���һҳ��������������һ��Fragment
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(adapter);//ͨ��adapterΪViewPager�ṩ����
		
		//ʵ����TabPageIndicator,Ȼ������ViewPager��֮����
		TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		
		//��ViewPager���м���
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
			//�½�һ��Fragment��չʾViewPager item�����ݣ������ݲ���  
            Fragment fragment = new ContentFragment();    
            Bundle args = new Bundle();    //Bundle��������ǿ�����Activity֮�䴫������
          //  args.putString("arg", TITLE[position]);    
           // fragment.setArguments(args);    //��Fragment���setArgument�������ݲ���
              args.putInt("arg",position);
              fragment.setArguments(args);
            return fragment;  
		}

		 @Override
	        public CharSequence getPageTitle(int position) {
	            return TITLE[position % TITLE.length].toUpperCase();  //ֱ��������������ɱ������ʾ,
	        }

		 @Override
		 	public int getCount() {
			 
			 	return TITLE.length;
		}
		
	}




}
