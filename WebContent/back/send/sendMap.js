

var address;

$(document).ready(function(){
	var url = $('#url').val();
	var image = url +'/images/messages-icon.png';
	var mapArea = document.getElementById('googleMap');
	var latlng ;
	
	$(".btn").click(function (event){
		var address = event.target.id;
		var geocoder = new google.maps.Geocoder();
		
		infowindow = new google.maps.InfoWindow({
		      content: address
		  });
		ncuwindow = new google.maps.InfoWindow({
		      content: '交換網'
		  });
		
		geocoder.geocode({'address':address},
				function(results,status){
					if(status == google.maps.GeocoderStatus.OK){
						var ncu = new google.maps.LatLng(24.9708264,121.18820769999999);
						var add = new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng());
						ncuMarker = new google.maps.Marker({
							position:ncu,
							map:display,
							animation: google.maps.Animation.DROP,
						    title :'交換網地址'
						});
						ncuMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
						
						marker = new google.maps.Marker({
							position:results[0].geometry.location,
							map:display,
							animation: google.maps.Animation.DROP,
						    title :'會員住家地址'
						});
						
						
						 calcRoute(ncu,add);
						
					latlng = new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng());
					$("#myModalLabel").html(decodeURI('派送起點:中央大學  ;  終點:'+address+'<BR> 距離 '+
							getDistance(24.9708264,121.18820769999999,results[0].geometry.location.lat(),results[0].geometry.location.lng())+
							' 公里'));
					
					}else{
						//alert('Google was not found address for following reason: '+status);
					}
				});
		var options ={
				zoom:12,
				mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		display = new google.maps.Map(mapArea,options);
		
		
	
	});
	
	
	
	$('#mapModal').on("shown.bs.modal", function () {
	    google.maps.event.trigger(display, "resize"); 
	    display.setCenter(latlng);
	    directionsDisplay.setMap(display);

		 google.maps.event.addListener(marker, 'click', function() {
			    infowindow.open(display,marker);
			  });
		 google.maps.event.addListener(ncuMarker, 'click', function() {
			 	ncuwindow.open(display,ncuMarker);
			  });
	});
	$('#mapModal').on('hidden.bs.modal', function () {
		//modal隱藏後,重新再生成一次所有派送的地圖
		calcRouteALL(latlngNCU,arrayLatlng[arrayLatlng.length-1],newlatlng);
		});
	
});

function calcRoute(start,end) {
	  var start = start;
	  var end = end;
	  var request = {
	    origin:start,
	    destination:end,
	    travelMode: google.maps.TravelMode.DRIVING
	  };
	  directionsService.route(request, function(result, status) {
	    if (status == google.maps.DirectionsStatus.OK) {
	      directionsDisplay.setDirections(result);
	    }
	  });
	}


function getDistance(x,y,x1,y1){
	//alert('傳入值: '+x+' , '+y+','+x1+' ,'+y1);
	var m = 0.00000900900901;  //一公尺 = 0.00000900900901 度
	var lat = Math.abs(x - x1);
	var lng = Math.abs(y - y1);
	//alert('相差經緯度數: '+lat+' , '+lng);
	var compareKm = ((lat/m)/1000)+((lng/m)/1000);
	compareKm = Math.floor(compareKm*10)/10;
	//alert(compareKm+'  :  compareKm值');
	return compareKm;
	 
}

