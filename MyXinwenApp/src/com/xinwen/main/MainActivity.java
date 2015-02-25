package com.xinwen.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.xinwen.user.UserActivity;
import com.xinwen.video.VideoActivity;

/**
 * TabHost��һ��������Ҫʵ��TabHost����Ҫ�̳�TabActivity��ʵ��OnCheckedChangerListener�ӿ�
 * */
public class MainActivity extends TabActivity implements OnCheckedChangeListener{
	
	private TabHost mTabHost;
	private RadioGroup radioGroup;
	private 	RadioButton rb1;
	private 	RadioButton rb2;
	private 	RadioButton rb3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bottommenu);
		
		radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		rb1 = (RadioButton)findViewById(R.id.home_button); 
		rb2 = (RadioButton)findViewById(R.id.linshi1); 
		rb3 = (RadioButton)findViewById(R.id.linshi2); 
		// ʵ����TabHost�������
		mTabHost = this.getTabHost();
		
		/**���ѡ�:
		 * newTabSpev(Tag):Tag���ַ�����Ϊѡ���Ψһ��ʶ
		 * setIndicator("ONE")��������ʾ�İ�ť����,������Ϊ���ڲ����ļ�������RadioButton�������������Ͳ���Ҫ����
		 * setContent(), ����������ͼ���, ��������Activity, Ҳ��������Fragement��ListView����������;
		 * sddTab��������������TabHost���������Tab
		 * Ĭ��һ��ʼ��ʾ���ǵ�һ��ʵ��addTab�����ģ��������������ģ�����Ȼ�����ô��������ʼ��ʾ��һ��
		 * */
		mTabHost.addTab(mTabHost.newTabSpec("linshi").setIndicator("")
				.setContent(new Intent(this, SlidemenuActivity.class)));
		rb1.setBackgroundResource(R.drawable.bottom_tab_linshi_pressed);
		mTabHost.addTab(mTabHost.newTabSpec("linshi1").setIndicator("")
				.setContent(new Intent(this,VideoActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("linshi2").setIndicator("")
				.setContent(new Intent(this, UserActivity.class)));			
		//ע�������
		radioGroup.setOnCheckedChangeListener(this);		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch(checkedId) {
		case R.id.home_button:
			mTabHost.setCurrentTabByTag("linshi");//���õ�ǰ��ʾ�ĸ���ǩ������
			rb1.setBackgroundResource(R.drawable.bottom_tab_linshi_pressed);
			rb2.setBackgroundResource(R.drawable.bottom_tab_linsh);
			rb3.setBackgroundResource(R.drawable.bottom_tab_linsh);
			break;
		case R.id.linshi1:
			mTabHost.setCurrentTabByTag("linshi1");
			rb1.setBackgroundResource(R.drawable.bottom_tab_linsh);
			rb2.setBackgroundResource(R.drawable.bottom_tab_linshi_pressed);
			rb3.setBackgroundResource(R.drawable.bottom_tab_linsh);
			break;
		case R.id.linshi2:
			mTabHost.setCurrentTabByTag("linshi2");
			rb1.setBackgroundResource(R.drawable.bottom_tab_linsh);
			rb2.setBackgroundResource(R.drawable.bottom_tab_linsh);
			rb3.setBackgroundResource(R.drawable.bottom_tab_linshi_pressed);
			break;		
		}
	}



}