package com.rhythmicsoftware.cmd.utils;

import java.io.IOException;
//import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.params.HttpMethodParams;

public class HTTPConnector {
	private Logger log = null;
	private String baseUrl;
	private List<String> cookies;

	HTTPConnector(String url, Logger log) {
		this.baseUrl = url;
		this.cookies = new ArrayList<String>();
		this.log = log;
	}
	
	// this function should return true on success or false on failure
	public boolean doPost(String contentType, String url, String params) throws ClientProtocolException, IOException {
		
        debug("----------------------------------------");
        debug("HTTPConnector.doPost Starting");
        debug("contentType: " + contentType);
        debug("url: " + url);
        debug("params: " + params);
             
		
        CloseableHttpClient client = new DefaultHttpClient();
		 
        	debug("----------------------------------------");
		 	debug("HTTPConnector.request -- httpClient: " + client);
		 		 
	        try {
	            HttpPost httpPost = new HttpPost(baseUrl + url);

	            debug("Executing request " + httpPost.getRequestLine());

	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status >= 200 && status < 300) {
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }
	            };
	            
	            String responseBody = client.execute(httpPost, responseHandler);
	            debug("----------------------------------------");
	            debug(responseBody);
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