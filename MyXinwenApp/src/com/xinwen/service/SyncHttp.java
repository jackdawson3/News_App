package com.xinwen.service;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.xinwen.model.Parameter;


/**
 *
 *��ͬ����ʽ����Http����
 */
public class SyncHttp
{
	
	/**
	 * ͨ��GET��ʽ��������
	 * @param url URL��ַ
	 * @param params ����
	 * @return 
	 * @throws Exception
	 */
	public String httpGet(String url, String params) throws Exception
	{
		String response = null; //������Ϣ
		//ƴ������URL
		if (null!=params&&!params.equals(""))
		{
			url += "?" + params;
		}
		
		int timeoutConnection = 3000;  
		int timeoutSocket = 5000;  
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  
	    
		// ����HttpClient��ʵ��
		HttpClient httpClient = new DefaultHttpClient(httpParameters);  
		// ����GET������ʵ��
		HttpGet httpGet = new HttpGet(url);
		try
		{
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) //SC_OK = 200
			{
				// ��÷��ؽ��
				response = EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				response = "�����룺"+statusCode;
			}
		} catch (Exception e)
		{
			throw new Exception(e);
		} 
		return response;
	}

	/**
	 * ͨ��POST��ʽ��������
	 * @param url URL��ַ
	 * @param params ����
	 * @return
	 * @throws Exception
	 */
	public String httpPost(String url, List<Parameter> params) throws Exception
	{
		String response = null;
		int timeoutConnection = 3000;  
		int timeoutSocket = 5000;  
		HttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.  
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);  
		// ����HttpClient��ʵ��
		HttpClient httpClient = new DefaultHttpClient(httpParameters);  
		HttpPost httpPost = new HttpPost(url);
		if (params.size()>=0)
		{
			//����httpPost�������
			httpPost.setEntity(new UrlEncodedFormEntity(buildNameValuePair(params),HTTP.UTF_8));
		}
		//ʹ��execute��������HTTP Post���󣬲�����HttpResponse����
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if(statusCode==HttpStatus.SC_OK)
		{
			//��÷��ؽ��
			response = EntityUtils.toString(httpResponse.getEntity());
		}
		else
		{
			response = "�����룺"+statusCode;
		}
		return response;
	}
	
	/**
	 * ��Parameter���ͼ���ת����NameValuePair���ͼ���
	 * @param params ��������
	 * @return
	 */
	private List<BasicNameValuePair> buildNameValuePair(List<Parameter> params)
	{
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		for (Parameter param : params)
		{
			BasicNameValuePair pair = new BasicNameValuePair(param.getName(), param.getValue());
			result.add(pair);
		}
		return result;
	}
}
