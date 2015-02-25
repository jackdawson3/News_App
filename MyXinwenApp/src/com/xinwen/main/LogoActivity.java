package com.xinwen.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class LogoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_logo);
		
		ImageView logoImage = (ImageView)findViewById(R.id.imagelogo);		
		AlphaAnimation logoAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		logoAlphaAnimation.setDuration(1000);		
		logoImage.setAnimation(logoAlphaAnimation);
		logoAlphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(LogoActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	

}
