var geocoder;
var map;
var zoomLevel;
var infowindow;
var currentLocation;
//For protect_level 0-4
//0: no protection, shows the exact location
//1: protection level 1, shows 
//Google Map Zoom Level: 0-21

function initialize() {
  latlng = new google.maps.LatLng(-34.397, 150.644);
  var myOptions = {
   zoom: 8,
   mapTypeId: google.maps.MapTypeId.ROADMAP,
   zoomControl: false
  };
  map = new google.maps.Map(document.getElementById("map_canvas"),
     myOptions);
  getCurrentLocation();
  infowindow = new google.maps.InfoWindow({  
       content: 'Default protect Level: 3<br/> Your current zoom level: 8'
    });
  infowindow.open(map);
}

function lockZoomLevel(protect_level) {
  zoomLevel = changeZoomLevel(protect_level);
  map.setZoom(zoomLevel);
  //Set info window to show current zoom level
  infowindow.setContent("Current protect level:" + protect_level + "<br/>Current zoom level:" +
    zoomLevel);
}

function changeZoomLevel(protect_level) {
  switch(protect_level) 
  {
    case "0":
      zoomLevel = 15;
      break;
    case "1":
      zoomLevel = 12;
      break;
    case "2":
      zoomLevel = 10;
      break;
    case "3":
      zoomLevel = 8;
      break;
    case "4":
      zoomLevel = 5;
      break;
    default:
      zoomLevel = 2;
  }
  return zoomLevel;
}

function getCurrentLocation() {
//get current user's location
  if(navigator.geolocation) {    
    navigator.geolocation.getCurrentPosition(function(position) {
    currentLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
    map.setCenter(currentLocation);
    infowindow.setPosition(currentLocation);}
    , function() {
      handleNoGeolocation(true);}
    );
  }
  //Browser doesn't support Geolocation
  else { 
    handleNoGeolocation(true);
  }
}    

function handleNoGeolocation(errorFlag) {
  if (errorFlag == true) {
    alert("Geolocation service failed.");
    //Place you to newyork
    currentLocation = new google.maps.LatLng(40.69847032728747, -73.9514422416687);
  }
}

function codeAddress() {
  var address = document.getElementById("address").value;
  geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
          map: map,
          position: results[0].geometry.location
      });
    } else {
      alert("Geocode was not successful for the following reason: " + status);
    }
  });
}