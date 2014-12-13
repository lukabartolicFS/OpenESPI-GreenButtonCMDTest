package com.rhythmicsoftware.cmd.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.common.net.HttpHeaders;

@SuppressWarnings("deprecation")
public class HTTPConnector {
	private Logger log = null;
	private String baseUrl;
	private String cookies;

	HTTPConnector(String url, String cookies, Logger log) {
		this.baseUrl = url;
		this.cookies = cookies;
		this.log = log;
	}
	// this function should return true on success or false on failure
	public boolean doGET(String contentType, String url, String params, final int status) throws ClientProtocolException, IOException {
		
        debug("----------------------------------------");
        debug("HTTPConnector.doGET Starting");
        debug("contentType: " + contentType);
        debug("url: " + url);
        debug("params: " + params);
        debug("status: " + status);
             
		
        CloseableHttpClient client = new DefaultHttpClient();
		 
        	debug("----------------------------------------");
		 	debug("HTTPConnector.request -- httpClient: " + client);
		 		 
	        try {
	      
	            HttpGet httpGet = new HttpGet(baseUrl + url + params);
	            httpGet.setHeader("Cookie", HTTPConnector.this.cookies);

	            debug("Executing request: " + httpGet.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                	
	                    int statusCode = response.getStatusLine().getStatusCode();
	                    if (statusCode == status) {
	                    	
	                    	if (response.containsHeader("Set-Cookie")) {
	                    		
		                    	// Save "Set-Cookie" header to preserve session cookie	                    		
	            				debug("Set-Cookie: " + response.getFirstHeader("Set-Cookie").getValue());
	            				cookies = response.getFirstHeader("Set-Cookie").getValue();
	            				cookies = (String) cookies.subSequence(0, cookies.indexOf(";"));	            				
	                    	}
	                    	
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }
	            };
	            
	            String responseBody = client.execute(httpGet, responseHandler);
	            debug("----------------------------------------");
	            if (responseBody != null) {
	            	debug(responseBody);
	            } else {
	            	debug("No response body received");
	            }
	            
	        } finally {
	        		client.close();
	        }		
		
		return true;
	}
	
	// this function should return true on success or false on failure
	public boolean doPOST(String contentType, String url, String params, final int status) throws ClientProtocolException, IOException {
		
        debug("----------------------------------------");
        debug("HTTPConnector.doPOST Starting");
        debug("contentType: " + contentType);
        debug("url: " + url);
        debug("params: " + params);
        debug("status: " + status);
             
		
        CloseableHttpClient client = new DefaultHttpClient();
		 
        	debug("----------------------------------------");
		 	debug("HTTPConnector.request -- httpClient: " + client);
		 		 
	        try {
	            HttpPost httpPost = new HttpPost(baseUrl + url + params);
	            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

	            debug("Executing request: " + httpPost.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                	
	                    int statusCode = response.getStatusLine().getStatusCode();
	                    if (statusCode == status) {
	                    	
	                    	if (response.containsHeader("Set-Cookie")) {
	                    		
		                    	// Save "Set-Cookie" header to preserve session cookie	                    		
	            				debug("Set-Cookie: " + response.getFirstHeader("Set-Cookie").getValue());
	            				cookies = response.getFirstHeader("Set-Cookie").getValue();
	            				cookies = (String) cookies.subSequence(0, cookies.indexOf(";"));
	                    	}
	                    	
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                        
	                    } else {
	                    	
	                        throw new ClientProtocolException("Unexpected response status: " + statusCode);
	                    }
	                }
	            };
	            
	            String responseBody = client.execute(httpPost, responseHandler);
	            debug("----------------------------------------");
	            if (responseBody != null) {
	            	debug(responseBody);
	            } else {
	            	debug("No response body received");
	            }
	            
	        } finally {
	        		client.close();
	        }		
		
		return true;
	}

	private void debug(String message) {
		if(log!=null) {
			log.info(message); 
		}
	}
}