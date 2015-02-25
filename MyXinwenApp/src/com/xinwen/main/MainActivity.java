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
 * TabHost是一个容器，要实现TabHost必须要继承TabActivity，实现OnCheckedChangerListener接口
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
		// 实例化TabHost，必须的
		mTabHost = this.getTabHost();
		
		/**添加选项卡:
		 * newTabSpev(Tag):Tag是字符串，为选项卡的唯一标识
		 * setIndicator("ONE")：设置显示的按钮名称,但是因为我在布局文件里用了RadioButton组件，所以这里就不需要设置
		 * setContent(), 可以设置视图组件, 可以设置Activity, 也可以设置Fragement、ListView或者其它的;
		 * sddTab方法的作用是向TabHost容器中添加Tab
		 * 默认一开始显示的是第一个实现addTab方法的（看样子是这样的），当然可以用代码设置最开始显示哪一个
		 * */
		mTabHost.addTab(mTabHost.newTabSpec("linshi").setIndicator("")
				.setContent(new Intent(this, SlidemenuActivity.class)));
		rb1.setBackgroundResource(R.drawable.bottom_tab_linshi_pressed);
		mTabHost.addTab(mTabHost.newTabSpec("linshi1").setIndicator("")
				.setContent(new Intent(this,VideoActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("linshi2").setIndicator("")
				.setContent(new Intent(this, UserActivity.class)));			
		//注册监听器
		radioGroup.setOnCheckedChangeListener(this);		
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch(checkedId) {
		case R.id.home_button:
			mTabHost.setCurrentTabByTag("linshi");//设置当前显示哪个标签的内容
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