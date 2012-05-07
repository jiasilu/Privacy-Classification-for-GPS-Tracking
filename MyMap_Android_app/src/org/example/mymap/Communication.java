package org.example.mymap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.util.Log;

public class Communication  {
	private static JSONObject jObject;
	static String sessionTokens = null;
	static String ip = null;
	static String port = null;
	static DefaultHttpClient httpclient = new DefaultHttpClient();	
	public static String getTokenFromServer(JSONObject userObj) {
		String url = null;
		try {
			ip = userObj.getString("ip");
			port = userObj.getString("port");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		url = "http://" + ip + ":" + port + "/api/tokens.json";

		JSONObject jsonObjRecv =null;
		try {
			
			HttpPost httpPostRequest = new HttpPost(new URI(url));
			HttpResponse response = null;
			String responseStr = null;
			StringEntity se = new StringEntity(userObj.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			//        httpPostRequest.setHeader("Authorization", usercredential);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");

			long t = System.currentTimeMillis();

			try {
				Log.v(null, "request:" + userObj.toString());
				response = httpclient.execute(httpPostRequest);
				responseStr = getResponse(response.getEntity());
				Log.i("console", "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
				Log.v(null, "response:" + responseStr);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				sessionTokens = parseToken(responseStr);
				Log.v(null, "token:" + sessionTokens);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {

			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return sessionTokens;
	}
	
	public static JSONArray getUsersFromServer(String sessionTokens) {
		String url = null;
		String responseStr = null;
    	JSONArray userList = null;

		long t = System.currentTimeMillis();
		url = "http://" + ip + ":" + port + "/show_users.json?auth_token=" + sessionTokens;
		try {
			HttpGet httpGetRequest = new HttpGet(new URI(url));
			HttpResponse response = null;

			// Set HTTP parameters
			httpGetRequest.setHeader("Accept", "application/json");
			httpGetRequest.setHeader("Content-type", "application/json");

			try {
				Log.v(null, "request_url:" + url);
				response = httpclient.execute(httpGetRequest);
				responseStr = getResponse(response.getEntity());
				Log.i("console", "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
				Log.v(null, "response_user_list:" + responseStr);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			/////////parse the json array and store it in a hash map
			userList = new JSONArray(responseStr);
			for (int i = 0; i < userList.length(); ++i) {
			    JSONObject user = userList.getJSONObject(i);
			    String email = user.getString("email");
			    int id = user.getInt("id");
				Log.v(null, "user_email:" + email + " user_id" + id );
			}
			Log.v(null, "user_list:" + userList );

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}
	
	public static String sendUserRequest(String user_info) {
		String url = null;
		String responseStr = null;
		String userLocation = null;
		long t = System.currentTimeMillis();
		url = "http://" + ip + ":" + port + "/user_request.json?auth_token="
				+ sessionTokens + user_info;
		
		
		// Set HTTP parameters
		
		try {
			HttpGet httpGetRequest = new HttpGet(new URI(url));
			HttpResponse response = null;

			// Set HTTP parameters
			httpGetRequest.setHeader("Accept", "application/json");
			httpGetRequest.setHeader("Content-type", "application/json");

			try {
				Log.v(null, "user_request_url:" + url);
				response = httpclient.execute(httpGetRequest);
				responseStr = getResponse(response.getEntity());
				
				Log.i("console", "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");
				Log.v(null, "user_server_response:" + responseStr);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		userLocation = "[" + responseStr + "]"; 
		return userLocation;
	}

	public static String getResponse(HttpEntity entity )
	{
		String response = "";

		try
		{
			int length = ( int ) entity.getContentLength();
			StringBuffer sb = new StringBuffer( length );
			InputStreamReader isr = new InputStreamReader( entity.getContent(), "UTF-8" );
			char buff[] = new char[length];
			int cnt;
			while ( ( cnt = isr.read( buff, 0, length - 1 ) ) > 0 )
			{
				sb.append( buff, 0, cnt );
			}

			response = sb.toString();
			isr.close();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}

		return response;
	}

	public static String parseToken(String jsonResponse) throws Exception {
		String token = null;
		if(jsonResponse != null) {
			jObject = new JSONObject(jsonResponse);
			token = jObject.getString("token");
		} else {
			Log.v(null, "Response is null!");
		}
		return token;
	}

}
