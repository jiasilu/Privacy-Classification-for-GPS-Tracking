/***
 * Excerpted from "Hello, Android!",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband for more book information.
***/

package org.example.mymap;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class GoogleMap extends MapActivity {
   private MapView map;
   private MapController controller;
   int lat = 1000000, lng = 2000000, level = 4;
   private int act_level = 1;
;
   @Override
   public void onCreate(Bundle savedInstanceState) {
	  
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
//      initMyLocation();
      Intent it = this.getIntent();
      String userLocation = it.getStringExtra("com.example.mymap.userLocation");
      Log.v(null, "location:" + userLocation);

      JSONArray userL;

      try {
		userL = new JSONArray(userLocation);
		lat = (int)Math.round(Double.valueOf(userL.getJSONObject(0).getString("lat"))  * 1e6);
	    lng = (int)Math.round(Double.valueOf(userL.getJSONObject(0).getString("lng")) * 1e6);
	    level = Integer.valueOf(userL.getJSONObject(0).getString("protect_level"));

	} catch (JSONException e) {
		e.printStackTrace();
	}   
	  act_level = privacyClassify.locationClassify(level);
	  Log.v(null, "act_level:" + act_level);
	  initMapView();
	  //initMyLocation();
	  GeoPoint point = new GeoPoint(lat,lng);
      List<Overlay> mapOverlays = map.getOverlays();
      Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
      MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, this);
      
      OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm here!");
     itemizedoverlay.addOverlay(overlayitem);
      mapOverlays.add(itemizedoverlay);
   }
   

   
   /** Find and initialize the map view. */
   private void initMapView() {
      map = (MapView) findViewById(R.id.map);
      controller = map.getController();
      map.setSatellite(false);
      map.setBuiltInZoomControls(false);
      GeoPoint point = new GeoPoint(lat,lng);
      controller.setCenter(point);
      controller.setZoom(act_level);
   }
   

   
   /** Start tracking the position on the map. */
   private void initMyLocation() {
      final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
      overlay.enableMyLocation();
      //overlay.enableCompass(); // does not work in emulator
      overlay.runOnFirstFix(new Runnable() {
         public void run() {
            // Zoom in to current location
        	
            controller.setZoom(act_level);
            controller.animateTo(overlay.getMyLocation());
         }
      });
      map.getOverlays().add(overlay);
   }
   

   
   @Override
   protected boolean isRouteDisplayed() {
      // Required by MapActivity
      return false;
   }
}
