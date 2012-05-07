package org.example.mymap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SecondActivity extends Activity{
	private TextView  text;
	private EditText ip,port,email,password;
	private View view ;
	private Button login, submitButton, requestFriButton;
	private RadioButton rd1,rd2,rd3,radioSelectedButton;
	private RadioGroup radioGroup;
	private RadioButton r1,r2,r3,r4,r5,radioSelectedButton2;
	private RadioGroup radGroup;
	private String FILE_NAME_GPS;
	private String sessionTokens = null;
	private String userRequest = null;
	private String userLocation = null;
	private String userRequestFriEmail = null;
	private String userRequestFriLevel = null;
	private JSONArray responseList = new JSONArray();
	private int FriLevel = 0;
	private RadioButton radioG[] = new RadioButton[3];
	private AlertDialog alertDialog = null;
	private String responseLocation = null;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.second); 
            Intent it = this.getIntent();

            text = (TextView) this.findViewById(R.id.title);
            view = this.findViewById(R.id.hide);
            login = (Button)this.findViewById(R.id.login);
            ip = (EditText)this.findViewById(R.id.ip);
            port = (EditText)this.findViewById(R.id.port);
            email = (EditText)this.findViewById(R.id.email);
            password = (EditText)this.findViewById(R.id.password);
        	
            String toShow = it.getStringExtra("com.example.mymap.welcome");
            text.setText(toShow);
            view.setVisibility(View.VISIBLE);		//1:INVISIBLE 0:VISIBLE
            
            //****************GPS************************ 
            LocationManager locationManager = (LocationManager)SecondActivity.this.getSystemService(Context.LOCATION_SERVICE);
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new TestLocationListener());
    		
    		
    		login.setOnClickListener(new ButtonListener());
    }


  //****************GPS************************
    protected class TestLocationListener implements LocationListener{
    	
		public void onLocationChanged(Location location) {
			Double longtitude = location.getLongitude();
			Double latitude = location.getLatitude();
			
			String data = longtitude.toString() + "," + latitude.toString() + ";"; 
		}
		public void onProviderDisabled(String provider) {
			
		}
		public void onProviderEnabled(String provider) {
			
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	}
    
    
    private class ButtonListener implements OnClickListener {
        public void onClick(View v) {
        	JSONObject userObj = new JSONObject();
        	JSONArray userList;
    		try {
    			userObj.put("ip", ip.getText().toString());
    			userObj.put("port", port.getText().toString());
    			userObj.put("password", password.getText().toString());
    			userObj.put("email", email.getText().toString());
    		} catch (JSONException js) {
    			js.printStackTrace();	
    		}
    		
    	    ///////////////////////////////////////////////
    	    sessionTokens = Communication.getTokenFromServer(userObj);
    	    userList = Communication.getUsersFromServer(sessionTokens);
    	    selectUserRequest(userList);
        }
    }
    
  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    super.onCreateContextMenu(menu, v, menuInfo);  
        menu.setHeaderTitle("Please specify friendship level:");  
        menu.add(0, v.getId(), 0, "Level 1");  
        menu.add(0, v.getId(), 0, "Level 2"); 
        menu.add(0, v.getId(), 0, "Level 3"); 
        menu.add(0, v.getId(), 0, "Level 4"); 
        menu.add(0, v.getId(), 0, "Level 5"); 
    }  
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
    	String requestString = "";
        FriLevel = 0;
        int selectedId = radioGroup.getCheckedRadioButtonId();
		// find the radiobutton by returned id
		radioSelectedButton2 = (RadioButton) findViewById(selectedId);
		userRequestFriEmail = (String) radioSelectedButton2.getText();

    	if(item.getTitle()=="Level 1"){FriLevel = 0;}  
        else if(item.getTitle()=="Level 2"){FriLevel = 1;}  
        else if(item.getTitle()=="Level 3"){FriLevel = 2;} 
        else if(item.getTitle()=="Level 4"){FriLevel = 3;} 
        else if(item.getTitle()=="Level 5"){FriLevel = 4;} 
        //Log.v(null, "ID:"+item.getItemId());
        userRequestFriLevel = Integer.toString(FriLevel);
        requestString = "&protect_level="+ userRequestFriLevel + "&email=" + userRequestFriEmail;
        Log.v(null, "requestString:" + requestString);
		responseLocation = Communication.sendUserRequest(requestString);			
		Log.v(null, "resString:"+ responseLocation);
		alertDialog.show();
    return true;  
    } 
    
    /*public void function1(int id){  
        Toast.makeText(this, "function 1 called", Toast.LENGTH_SHORT).show();  
    }  
    public void function2(int id){  
        Toast.makeText(this, "function 2 called", Toast.LENGTH_SHORT).show();  
    } */ 
    
    public void selectUserRequest(JSONArray userList) {
    	this.setContentView(R.layout.third); 
    	submitButton = (Button)this.findViewById(R.id.submit);
    	submitButton.setVisibility(View.INVISIBLE);

    	radioGroup = (RadioGroup) findViewById(R.id.radio);
    	rd1 = (RadioButton) this.findViewById(R.id.radio1);
    	rd2 = (RadioButton) this.findViewById(R.id.radio2);
    	rd3 = (RadioButton) this.findViewById(R.id.radio3);
    	radioG[0] = rd1;
    	radioG[1] = rd2;
    	radioG[2] = rd3;


    	requestFriButton = (Button) findViewById(R.id.requestFriend);
    	registerForContextMenu(requestFriButton); 

    	//Initialize the responseJSONArray
    	try {
    		for(int i = 0; i < 3; i++) {
    			radioG[i].setText(userList.getJSONObject(i).getString("email"));
    			JSONObject responseObj = new JSONObject();
    			responseObj.put("id", radioG[i].getId());
    			responseObj.put("response_flag", false);
    			Log.v(null, "responseObj:"+ responseObj);

    			responseList.put(responseObj);
    		}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*
		OnClickListener radio_listener = new OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		        RadioButton rb = (RadioButton) v;
		        int currentId = rb.getId();
		        boolean request_flag = false;
		        //search for the button with flag in json array
		        
		        for (int i = 0; i < responseList.length(); ++i) {
				    JSONObject response;
					try {
						response = responseList.getJSONObject(i);
						int id = response.getInt("id");
					    if(id == currentId) {
					    	request_flag = response.getBoolean("response_flag");
					    	if(request_flag) {
					    		submitButton.setVisibility(View.VISIBLE);
					    		requestFriButton.setVisibility(View.INVISIBLE);
					    	}
					    } 
					} catch (JSONException e) {
						e.printStackTrace();
					}
				       
		        }
		        
		        
		        /////////////////////////[{id:1,flag:false},{id:2,flag:true}]

		    }
		};
	
		rd1.setOnClickListener(radio_listener);
		rd2.setOnClickListener(radio_listener);
		rd3.setOnClickListener(radio_listener);
*/
        alertDialog = new AlertDialog.Builder(SecondActivity.this).create();
        alertDialog.setTitle("User Response");
        alertDialog.setMessage("Okay, we are connected now!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//            	submitButton.setVisibility(View.VISIBLE);
//            	requestFriButton.setVisibility(View.INVISIBLE);
            	int selectedId = radioGroup.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioSelectedButton = (RadioButton) findViewById(selectedId);
				userRequest = (String) radioSelectedButton.getText();
				Toast.makeText(SecondActivity.this, userRequest , Toast.LENGTH_SHORT).show();
				Intent it = new Intent(SecondActivity.this, GoogleMap.class);
				it.putExtra("com.example.mymap.userLocation", responseLocation);
				startActivity(it);
          } });
		

		
/*
		submitButton.setOnClickListener(new OnClickListener() {		 
			public void onClick(View v) {
				// get selected radio button from radioGroup
				int selectedId = radioGroup.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioSelectedButton = (RadioButton) findViewById(selectedId);
				userRequest = (String) radioSelectedButton.getText();
				Toast.makeText(SecondActivity.this, userRequest , Toast.LENGTH_SHORT).show();
				
//				userLocation = Communication.sendUserRequest(userRequest);			
//				Intent it = new Intent(SecondActivity.this, GoogleMap.class);
//        		it.putExtra("com.example.mymap.userLocation", userLocation);
//        		startActivity(it);
			}

		});
*/		
		
    }
}