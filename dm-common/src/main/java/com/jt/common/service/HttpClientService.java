package com.jt.common.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {
	@Autowired(required=false)
	private CloseableHttpClient httpClient;
	@Autowired(required=false)
	private RequestConfig requestConfig;

	/** 
	 * 1.定义请求方式 httpPost
	 * 2.将参数进行表单实体封装
	 * 3.获取返回值
	 *  */
	public String doPost(String url,Map<String,String> params,String charset) {
		String result = null;
		if(StringUtils.isEmpty(charset))charset="UTF-8";
		//1.定义请求方式
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);//定义链接时长

		try {
			//2.参数封装
			if(params!=null) {
				List<BasicNameValuePair> parameters = new ArrayList<>();
				//动态获取用户数据
				for (Map.Entry<String, String> entry : params.entrySet()) {
					parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				//封装form表单实体对象,作用传递参数
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,charset);
				post.setEntity(entity);
			}
				//3.发起url请求
				CloseableHttpResponse httpResponse = httpClient.execute(post);
				if(httpResponse.getStatusLine().getStatusCode()==200) {
					result = EntityUtils.toString(httpResponse.getEntity(),charset);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return result;
	}
	public String doPost(String url) {
		return this.doPost(url, null, null);
	}
	public String doPost(String url,Map<String,String> params) {
		return this.doPost(url,params,null);
	}
	//编辑请求发送的方式,获取返回值结果给调用者
	public String doGet(String url,Map<String,String> params,String charset) {
		String result = "";
		//1.判断字符集编码是否为null
		if(StringUtils.isEmpty(charset)) {
			charset="UTF-8";
		}
		//拼接url
		//2.封装用户提交的参数
		if(params!=null) {
			try {
				URIBuilder builder = new URIBuilder(url);
				for (Map.Entry<String, String> e : params.entrySet()) {
					builder.addParameter(e.getKey(), e.getValue());
				}
				url=builder.build().toString();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		//3.封装请求参数类型
		HttpGet httpGet = new HttpGet(url);
		//4.发起请求,获取响应结果
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			//5.判断返回值是否正确
			if(response.getStatusLine().getStatusCode()==200) {
				//6.解析返回值数据
				result = EntityUtils.toString(response.getEntity(),charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String doGet(String url) {
		return doGet(url,null,null);
	}
	public String doGet(String url,Map<String,String> params) {
		return doGet(url,params,null);
	}
}
