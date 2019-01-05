package com.jt.common.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService2 {
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
		String result=null;
		if(StringUtils.isEmpty(charset))charset="UTF-8";
		//1.定义请求方式
		HttpPost post = new HttpPost(url);
		try {
			if(params!=null) {
				//2.参数封装
				List<BasicNameValuePair> parameters = new ArrayList<>();
				//动态获取用户数据
				for (Map.Entry<String, String> entry : params.entrySet()) {
					parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				//封装form表单实体对象,作用传递参数
				UrlEncodedFormEntity paraEntity = new UrlEncodedFormEntity(parameters);
				post.setEntity(paraEntity);
			}

			//3.发起url请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			//4.判断结果
			if(httpResponse.getStatusLine().getStatusCode()==200) {
				//跨域返回来的结果
				result=EntityUtils.toString(httpResponse.getEntity(),charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return charset;
	}

	public String doPost(String url,Map<String,String> params) {
		return this.doPost(url,params,null);
	}
	//编辑请求发送的方式,获取返回值结果给调用者
	public String doGet(String url,Map<String,String> params,String charset) {
		String result="";
		//1.判断字符集编码是否为null
		if(StringUtils.isEmpty(charset))charset="UTF-8";
		try {
			//拼接url
			if(params!=null) {
				URIBuilder builder  = new URIBuilder(url);
				//2.封装用户提交的参数
				for (Map.Entry<String, String> entry : params.entrySet()) {
					//3.封装请求参数类型
					builder.addParameter(entry.getKey(), entry.getValue());
				}
				url = builder.build().toString();
			}
			//4.发起请求,获取响应结果
			HttpGet get = new HttpGet(url);
			CloseableHttpResponse httpResponse = httpClient.execute(get);
			//5.判断返回值是否正确
			if(httpResponse.getStatusLine().getStatusCode()==200) {
				//6.解析返回值数据
				result = EntityUtils.toString(httpResponse.getEntity(), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return charset;
	}
	public String doGet(String url) {
		return doGet(url,null,null);
	}
	public String doGet(String url,Map<String,String> params) {
		return doGet(url,params,null);
	}
}
