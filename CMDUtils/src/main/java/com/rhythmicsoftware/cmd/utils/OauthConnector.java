package com.rhythmicsoftware.cmd.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.params.HttpMethodParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OauthConnector {
	private Logger log = null;
	private String baseUrl;
	private List<String> cookies;

	OauthConnector(String url, Logger log) {
		this.baseUrl = url;
		this.cookies = new ArrayList<String>();
		this.log = log;
	}
	
	// this function should return true on success or false on failure
	public boolean request(String Method, String contentType, String url, Map<String, Serializable> params) throws ClientProtocolException, IOException {
	/*	
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		
		// Create a method instance.
		GetMethod method = new GetMethod(baseUrl + url);
		
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		
		try {
		  // Execute the method.
		  int statusCode = client.executeMethod(method);
		
		  if (statusCode != HttpStatus.SC_OK) {
		    debug("Method failed: " + method.getStatusLine());
		    return false;
		  }
		
		  // Read the response body.
		  byte[] responseBody = method.getResponseBody();
		
		  // Deal with the response.
		  // Use caution: ensure correct character encoding and is not binary data
		  debug(new String(responseBody));
		
		} catch (HttpException e) {
			debug("Fatal protocol violation: " + e.getMessage());
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			debug("Fatal transport error: " + e.getMessage());
			//e.printStackTrace();
		  return false;
		} finally {
		  // Release the connection.
		  method.releaseConnection();
		}  
		*/
		
		 CloseableHttpClient httpclient = HttpClients.createDefault();
	        try {
	            HttpGet httpget = new HttpGet(baseUrl + url);

	            debug("Executing request " + httpget.getRequestLine());

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
	            String responseBody = httpclient.execute(httpget, responseHandler);
	            debug("----------------------------------------");
	            debug(responseBody);
	        } finally {
	            httpclient.close();
	        }		
		
		return true;
	}

	private void debug(String message) {
		if(log!=null) {
			log.info(message); 
		}
	}
}