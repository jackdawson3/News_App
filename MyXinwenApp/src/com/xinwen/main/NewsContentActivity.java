package com.xinwen.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;

import com.xinwen.service.SyncHttp;

public class NewsContentActivity extends Activity{

	private final int FINISH = 0;
	private ArrayList<HashMap<String,Object>> newsData ; 
	private int mNid;
	private int position;
	private TextView newsDetails;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case FINISH:
				newsDetails.setText(Html.fromHtml(msg.obj.toString()));
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newscontent);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		position=bundle.getInt("position");
		position--;
		Serializable serializable = bundle.getSerializable("newsData");
		newsData = (ArrayList<HashMap<String,Object>>)serializable;
		HashMap<String,Object> hashMap = newsData.get(position);
		
		TextView newsTitle = (TextView)findViewById(R.id.news_content_title);
		newsTitle.setText(hashMap.get("news_title").toString());
		TextView newsPtimeAndSource = (TextView)findViewById(R.id.news_content_source_time);
		newsPtimeAndSource.setText(hashMap.get("news_source").toString()+"     "+hashMap.get("news_time").toString());
		newsDetails = (TextView)findViewById(R.id.news_content_details);
		mNid =(Integer)hashMap.get("nid");		
		new UpdateNewsThread().start();
	}
	
	private class UpdateNewsThread extends Thread
	{
		@Override
		public void run() {
			String newsStr = getNewsBody();
			Message msg = handler.obtainMessage();
			msg.arg1 = FINISH;
			msg.obj = newsStr;
			handler.sendMessage(msg);
		}
		
	}
	/**
	 * 获取新闻详细信息
	 * @return
	 */
	private String getNewsBody()
	{
		String retStr = "网络连接失败，请稍后再试";
		SyncHttp syncHttp = new SyncHttp();
		String url = "http://10.0.2.2:8080/web/getNews";
		String params = "nid=" + mNid;
		try
		{
			String retString = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retString);
			//获取返回码，0表示成功
			int retCode = jsonObject.getInt("ret");
			if (0 == retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				JSONObject newsObject = dataObject.getJSONObject("news");
				retStr = newsObject.getString("body");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return retStr;
	}
}
