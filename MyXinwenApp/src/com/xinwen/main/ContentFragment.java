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
 * 这个类是实现每一个Fragment里的内容，即一个新闻页要显示的内容
 * 通常Fragment作为宿主Activity界面的一部分，它嵌入到整个视图层次结构中，并作为其中的一部分。
 * */
/**
 * 1、LayoutInflater这个类的作用类似于findViewById(),不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！
 * 		而findViewById()是找具体xml下的具体widget控件(如:Button,TextView等)。
 * 2、因为本类是定义的Fragment，并不是Activity，而且这个类中没有setContentView(R.layout.^^^)方法来设置布局文件，
 * 		所以要通过LayoutInflater来找XML文件
 * */
/**
 * SimpleAdapter用法的思路是这样的（而且基本就是固定的）：
 * 1、首先通过findViewById获得ListView控件，因为我们要向ListView控件里填充内容。
 * 2、为newslist设置适配器，所以在这之前我们就要实例化一个Simpleadapter
 * 3、当实例化一个simpleadapter时，我们发现里面有5个参数需要我们提供，所以在这之前我们就要分别提供这5个参数：
 * 		SimpleAdapter (Context context,List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
 * 		第一个参数是Context context：就是SimpleAdapter所在的Activity（一般而言），所以这个参数一般是“前Activity的名字.this”
 * 		第二个参数是List<? extends Map<String：是要去填充ListView每一个Item内容的List，所以我们要将需要显示的数据都放到这个List中
 * 		第三个参数是int resource：是这个List中每一个Item的布局文件
 * 		第四个参数是String[] from：因为在ArrayList存放的都是Map<String,Object>的item，from中的名字就是为了索引ArrayList中的Object。
 * 		第五个参数是int[] to：是索引layout中的id，对应前面每项的布局格式。 
 * */
public class ContentFragment extends Fragment{

	private ArrayList<HashMap<String,Object>> newsData ; //这个List的HashMap里存的是新闻列表里每一个条目的内容
	private PullToRefreshListView newslist ;        //PullToRefreshListView带有下拉刷新功能
	private  SimpleAdapter newsListAdapter;
	private GetDataTask getDataTsk;
	private int cId;                               //每一个新闻类型都有一个ID号
	private final int SUCCESS=0;
	private final int NONEWS=1;
	private final int NOMORENEWS=2;
	private final int LOADERROR=3;
	private int i=1,j=1;
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	          /**下面两行代码是版本问题,在4.0之后在主线程里面执行Http请求都会报错,所以应在发起Http请求的Activity里面的onCreate函数里面添加如下两行代码*/
		 	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
	        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());	       
	        View contextView = inflater.inflate(R.layout.fragment_item, container, false); 	//获得布局文件      
	        newsData = new ArrayList<HashMap<String,Object>>();       
	        Bundle mBundle = getArguments();    
	        int position = mBundle.getInt("arg"); 
	        cId=position;			
	        getCatNews(cId+1,newsData,0);
	        if( getCatNews(cId+1,newsData,0)!=0)
	        	Toast.makeText(getActivity(), "获得新闻失败", Toast.LENGTH_LONG).show();
	        newsListAdapter = new SimpleAdapter(getActivity(), newsData, R.layout.newslist_item, 
	        										new String[]{"news_title","news_digest","news_source","news_time"},
	        										new int[]{R.id.news_title,R.id.news_digest,R.id.news_source,R.id.news_time});        
	    	newslist = (PullToRefreshListView)contextView.findViewById(R.id.xinwen_list);
	        newslist.setMode(Mode.BOTH);  	//设置ListView包含下拉和上拉两种操作       	        
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
	        	             //这里写下拉刷新的任务
	        	         }

	        	         public void onPullUpToRefresh (PullToRefreshBase < ListView > refreshView )
	        	         {
	        	             //这里写上拉加载更多的任务
	        	          	 newslist.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
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
	  * 第一个参数：是在doInBackground方法里传递的参数类型
	  * 第二个参数：是在OnProgressUpdate里传递的参数类型
	  * 第三个参数：是doInBackground返回值的类型，而且这个类型要与onPOstExcute里的参数类型必须一致，因为这里的参数就是接收的doInBackground的返回值
	  * */
	  private class GetDataTask extends AsyncTask < Object , Integer , Integer >
	     {		  
		  /*在这个函数里写在进行后台操作前需要进行的对UI的操作，或其它操作*/
		  	@Override
		  	protected void onPreExecute() {
		  		super.onPreExecute();
		}
		  	/*在这个函数里写主要进行的后台操作，但是一定不可以进行UI方面的操作，否则一定会报错*/
		  	@Override
		  	protected Integer doInBackground(Object... params) {
		  		return getCatNews(cId+1, newsData, (Integer)params[0]);	  			
		  		}
		  	/*在这个函数里写在进行后台操作以后需要进行的对UI的操作，或其它操作*/
			protected void onPostExecute(Integer result) {
				switch (result)
				{
				case NONEWS:
					Toast.makeText(getActivity(), "该栏目下没有新闻", Toast.LENGTH_LONG).show();
				break;
				case NOMORENEWS:
					Toast.makeText(getActivity(), "该栏目下没有更多新闻", Toast.LENGTH_LONG).show();
					break;
				case LOADERROR:
					Toast.makeText(getActivity(), "获得新闻失败", Toast.LENGTH_LONG).show();
					break;
				}
				newsListAdapter.notifyDataSetChanged();
				newslist.onRefreshComplete();  
			}
			
	     }
}














