package org.example.mymap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FirstActivity extends Activity{
        private Button submit = null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.setContentView(R.layout.first);
                submit = (Button)this.findViewById(R.id.first);
                submit.setOnClickListener(new ButtonListener());
        }
        
        private class ButtonListener implements OnClickListener {
            public void onClick(View v) {
               	  
        		Intent it = new Intent(FirstActivity.this, SecondActivity.class);
        		it.putExtra("com.example.mymap.welcome", "Please login to the server.");
        		startActivity(it);
            }
        }
}