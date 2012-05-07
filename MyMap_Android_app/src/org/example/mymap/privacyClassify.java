package org.example.mymap;

public class privacyClassify {

	private static int zoomLevel = 0;
	public static int locationClassify(int protect_level){
		switch(protect_level) 
		  {
		    case 0:
		      zoomLevel = 15;
		      break;
		    case 1:
		      zoomLevel = 12;
		      break;
		    case 2:
		      zoomLevel = 10;
		      break;
		    case 3:
		      zoomLevel = 8;
		      break;
		    case 4:
		      zoomLevel = 5;
		      break;
		    default:
		      zoomLevel = 2;
		  }                      
		return zoomLevel;
	}
}
