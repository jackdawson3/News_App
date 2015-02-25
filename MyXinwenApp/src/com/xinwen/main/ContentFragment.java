package com.xinwen.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinwen.service.SyncHttp;

/**
 * �������ʵ��ÿһ��Fragment������ݣ���һ������ҳҪ��ʾ������
 * ͨ��Fragment��Ϊ����Activity�����һ���֣���Ƕ�뵽������ͼ��νṹ�У�����Ϊ���е�һ���֡�
 * */
/**
 * 1��LayoutInflater����������������findViewById(),��ͬ����LayoutInflater��������layout��xml�����ļ�������ʵ������
 * 		��findViewById()���Ҿ���xml�µľ���widget�ؼ�(��:Button,TextView��)��
 * 2����Ϊ�����Ƕ����Fragment��������Activity�������������û��setContentView(R.layout.^^^)���������ò����ļ���
 * 		����Ҫͨ��LayoutInflater����XML�ļ�
 * */
/**
 * SimpleAdapter�÷���˼·�������ģ����һ������ǹ̶��ģ���
 * 1������ͨ��findViewById���ListView�ؼ�����Ϊ����Ҫ��ListView�ؼ���������ݡ�
 * 2��Ϊnewslist��������������������֮ǰ���Ǿ�Ҫʵ����һ��Simpleadapter
 * 3����ʵ����һ��simpleadapterʱ�����Ƿ���������5��������Ҫ�����ṩ����������֮ǰ���Ǿ�Ҫ�ֱ��ṩ��5��������
 * 		SimpleAdapter (Context context,List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
 * 		��һ��������Context context������SimpleAdapter���ڵ�Activity��һ����ԣ��������������һ���ǡ�ǰActivity������.this��
 * 		�ڶ���������List<? extends Map<String����Ҫȥ���ListViewÿһ��Item���ݵ�List����������Ҫ����Ҫ��ʾ�����ݶ��ŵ����List��
 * 		������������int resource�������List��ÿһ��Item�Ĳ����ļ�
 * 		���ĸ�������String[] from����Ϊ��ArrayList��ŵĶ���Map<String,Object>��item��from�е����־���Ϊ������ArrayList�е�Object��
 * 		�����������int[] to��������layout�е�id����Ӧǰ��ÿ��Ĳ��ָ�ʽ�� 
 * */
public class ContentFragment extends Fragment{

	private ArrayList<HashMap<String,Object>> newsData ; //���List��HashMap�����������б���ÿһ����Ŀ������
	private PullToRefreshListView newslist ;        //PullToRefreshListView��������ˢ�¹���
	private  SimpleAdapter newsListAdapter;
	private GetDataTask getDataTsk;
	private int cId;                               //ÿһ���������Ͷ���һ��ID��
	private final int SUCCESS=0;
	private final int NONEWS=1;
	private final int NOMORENEWS=2;
	private final int LOADERROR=3;
	private int i=1,j=1;
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	          /**�������д����ǰ汾����,��4.0֮�������߳�����ִ��Http���󶼻ᱨ��,����Ӧ�ڷ���Http�����Activity�����onCreate������������������д���*/
		 	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());	       
	        View contextView = inflater.inflate(R.layout.fragment_item, container, false); 	//��ò����ļ�      
	        newsData = new ArrayList<HashMap<String,Object>>();       
	        Bundle mBundle = getArguments();    
	        int position = mBundle.getInt("arg"); 
	        cId=position;			
	        getCatNews(cId+1,newsData,0);
	        if( getCatNews(cId+1,newsData,0)!=0)
	        	Toast.makeText(getActivity(), "�������ʧ��", Toast.LENGTH_LONG).show();
	        newsListAdapter = new SimpleAdapter(getActivity(), newsData, R.layout.newslist_item, 
	        										new String[]{"news_title","news_digest","news_source","news_time"},
	        										new int[]{R.id.news_title,R.id.news_digest,R.id.news_source,R.id.news_time});        
	    	newslist = (PullToRefreshListView)contextView.findViewById(R.id.xinwen_list);
	        newslist.setMode(Mode.BOTH);  	//����ListView�����������������ֲ���       	        
	        newslist.setAdapter(newsListAdapter);
	        newslist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {				
					Intent intent = new Intent(getActivity(),NewsContentActivity.class);
					intent.putExtra("newsData", newsData);
					intent.putExtra("position", position);
					startActivity(intent);			
				}
			});
	              
	        newslist.setOnRefreshListener ( new OnRefreshListener2< ListView >() {
			
	        	         public void onPullDownToRefresh (
	        	                 PullToRefreshBase < ListView > refreshView )
	        	         {
	        	             //����д����ˢ�µ�����
	        	         }

	        	         public void onPullUpToRefresh (PullToRefreshBase < ListView > refreshView )
	        	         {
	        	             //����д�������ظ��������
	        	          	 newslist.getLoadingLayoutProxy(false, true).setRefreshingLabel("���ڼ���...");
	        	        	 getDataTsk = new GetDataTask();
	        	        	 getDataTsk. execute ( newsData.size()) ;
	        	         }
	        	     } );
	        return contextView;  
	    }  
	
	 
	 private int getCatNews(int cid,List<HashMap<String,Object>> newsList,int startId)
	 {
		// List<HashMap<String,Object>> newsList = new ArrayList<HashMap<String,Object>>();
		 String url = "http://10.0.2.2:8080/web/getSpecifyCategoryNews";
		 String params = "startnid="+startId+"&count=5&cid="+cid;
		 SyncHttp syncHttp = new SyncHttp();
		 try {
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			int retCode = jsonObject.getInt("ret");
			if(retCode==0)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				int totalnum = dataObject.getInt("totalnum");
				if(totalnum > 0)
				{										
					JSONArray newslist = dataObject.getJSONArray("newslist");
					for(int i=0;i<newslist.length();i++)
					{
						JSONObject newsObject = (JSONObject)newslist.opt(i);
						HashMap<String,Object> hashMap = new HashMap<String,Object>();
						hashMap.put("nid",newsObject.getInt("nid"));
						hashMap.put("news_title",newsObject.getString("title"));
						hashMap.put("news_digest",newsObject.getString("digest"));
						hashMap.put("news_source",newsObject.getString("source"));
						hashMap.put("news_time",newsObject.getString("ptime"));
						newsList.add(hashMap);
					}
					return SUCCESS;
				}
				else
					return NOMORENEWS;
			}
			else
				return LOADERROR;
		 } catch (Exception e) {
			e.printStackTrace();
				return LOADERROR;
		 }
	 }	 
	 /*
	  * ��һ������������doInBackground�����ﴫ�ݵĲ�������
	  * �ڶ�������������OnProgressUpdate�ﴫ�ݵĲ�������
	  * ��������������doInBackground����ֵ�����ͣ������������Ҫ��onPOstExcute��Ĳ������ͱ���һ�£���Ϊ����Ĳ������ǽ��յ�doInBackground�ķ���ֵ
	  * */
	  private class GetDataTask extends AsyncTask < Object , Integer , Integer >
	     {		  
		  /*�����������д�ڽ��к�̨����ǰ��Ҫ���еĶ�UI�Ĳ���������������*/
		  	@Override
		  	protected void onPreExecute() {
		  		super.onPreExecute();
		}
		  	/*�����������д��Ҫ���еĺ�̨����������һ�������Խ���UI����Ĳ���������һ���ᱨ��*/
		  	@Override
		  	protected Integer doInBackground(Object... params) {
		  		return getCatNews(cId+1, newsData, (Integer)params[0]);	  			
		  		}
		  	/*�����������д�ڽ��к�̨�����Ժ���Ҫ���еĶ�UI�Ĳ���������������*/
			protected void onPostExecute(Integer result) {
				switch (result)
				{
				case NONEWS:
					Toast.makeText(getActivity(), "����Ŀ��û������", Toast.LENGTH_LONG).show();
				break;
				case NOMORENEWS:
					Toast.makeText(getActivity(), "����Ŀ��û�и�������", Toast.LENGTH_LONG).show();
					break;
				case LOADERROR:
					Toast.makeText(getActivity(), "�������ʧ��", Toast.LENGTH_LONG).show();
					break;
				}
				newsListAdapter.notifyDataSetChanged();
				newslist.onRefreshComplete();  
			}
			
	     }
}














