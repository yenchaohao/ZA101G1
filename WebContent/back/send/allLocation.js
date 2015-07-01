//調整上下是緯度,左右是經度 ; 北半球,往北是+ 緯度 , 往南 是 -緯度 (從赤道開始算) ; 
//一公尺等於 0.00000900900901 度
var m = 0.00000900900901;

delay = 100;

nextAddress = 0;

var sendArray; //在jsp上儲存所有派送會員資訊物件的陣列,用json轉譯

var diatance = 0; //頁面上印出公里數的index
markers = [];	//圖標的陣列
var memberLatlng = []; //儲存會員經緯度的陣列 要給路徑最後排序用的  (waypts 物件)

var memberDistance = new Array(); //儲存會員地址的離中央大學的距離,作為排序memberLatlng[] ,arrayLatlng[] 的 依據

var directionsDisplay = new google.maps.DirectionsRenderer();
var directionsService = new google.maps.DirectionsService();
var arrayLatlng = [];  //儲存會員地址的Latlng物件 , 用來當作 calcRoute END 的位址 ,也會依照距離作排序

$(document).ready(function(){
	
	var area = document.getElementById('allLocation');
	var options ={
			zoom:11,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	
	latlngNCU = new google.maps.LatLng(24.9708264,121.18820769999999);
	
	
	displayALL = new google.maps.Map(area,options);
	google.maps.event.trigger(displayALL, "resize"); 
	directionsDisplay.setMap(displayALL);
	
	
	
	displayALL.setCenter(latlngNCU);
	/* var ncuMarker = new google.maps.Marker({
			position: latlng,
			map:display,
			title :'交換網' 
		});
	 
	ncuMarker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
	 
	 google.maps.event.addListener( ncuMarker, 'click', function() {
		  infowindow = new google.maps.InfoWindow({
		      content: '交換網'
		  });
		  infowindow.open(display,ncuMarker);
    });	*/
	
	sendArray = jQuery.parseJSON($("#sendList").val());
	
//	alert(sendArray[0]['memberImage']);
//	alert(sendArray[0]['mem_name']);
//	alert(sendArray[0]['goodsImage']);
//	alert(sendArray[0][ 'goods_name']);
	callGetPosition();
	
});

function mapMarker(lat,lng,map,address,memberimage,mem_name,goods_image,goods_name){
	
	var marker = new google.maps.Marker({
					position: new google.maps.LatLng(lat,lng),
					map:map,
					title :address,
					icon:memberimage
				});
	 //marker.setIcon('http://maps.google.com/mapfiles/ms/icons/purple-dot.png')
		  markers.push(marker);
		  
		  var contentString = "<div>會員姓名:<b>"+mem_name+"</b><br>商品名稱:<b>"+goods_name+"</b><img src='"+goods_image+"'><br>地址:<b>"+address+"</b></div>";	
		  var infowindow = new google.maps.InfoWindow({
			      content: contentString
			  }); 
		  google.maps.event.addListener(marker, 'click', function() { 
			  infowindow.open(map,marker);
		  });				
			
}



function getPosition(address , next){
	
	var position = new Array();
	
	var geocoder = new google.maps.Geocoder();
	
	geocoder.geocode({'address':address},
	function(results,status){
		if(status == google.maps.GeocoderStatus.OK){
			
			position[0] = results[0].geometry.location.lat();
			position[1] = results[0].geometry.location.lng();
			var latlng = new google.maps.LatLng(position[0],position[1]);
			
			//在頁面上印出公里數
			$("#distance"+diatance).html( decodeURI(getDistance(24.9708264,121.18820769999999,position[0],position[1])+''));
			diatance++;
			//儲存會員經緯度的陣列 要給路徑最後排序用的
			memberLatlng.push({
		          location:latlng,
		          stopover:true});
			//儲存會員地址的離中央大學的距離,作為排序memberLatlng[] 的 依據
			memberDistance.push(decodeURI(getDistance(24.9708264,121.18820769999999,position[0],position[1])));
			//儲存會員地址的Latlng物件 , 用來當作 calcRoute END 的位址 ,也會依照距離作排序
			arrayLatlng.push(latlng);
			 if(memberLatlng.length == sendArray.length){
				 drawLine();
			 }
			
		}else if(status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT){
			nextAddress--;
			delay++;
		}
		
		else{
			alert('getPosition錯: '+status);
			return;
		}
		
	});
	next();
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


function callGetPosition(){
	if(nextAddress < sendArray.length){
		setTimeout('getPosition("'+sendArray[nextAddress]['address']+'",callGetPosition)',delay);
		nextAddress++;
		//document.getElementById('info').innerHTML = 'Searching....';
	}else{
		
	
	}
}



function drawLine(){
	
	//把memberLatlng 依照離中央大學的距離作排序
	for(var i = 0 ; i < memberDistance.length-1 ; i++){
		for(var j=0 ; j < memberDistance.length-i-1 ; j++){
			//alert(memberDistance[j+1]+" > "+memberDistance[j]);
			if(parseFloat(memberDistance[j+1]) < parseFloat(memberDistance[j])) {
				//排序  waypts 的物件, 用來規畫路徑的先後順序
				var temp = memberLatlng[j+1];
				memberLatlng[j+1] = memberLatlng[j];
				memberLatlng[j] = temp;
				//排序latlng 物件 用來知道distant request 的 end 最後一個
				var temp2 = arrayLatlng[j+1];
				arrayLatlng[j+1] = arrayLatlng[j];
				arrayLatlng[j] = temp2;
				//memberDistance 存放各個會員跟公司的距離 .
				var temp3 = memberDistance[j+1];
				memberDistance[j+1] = memberDistance[j];
				memberDistance[j] = temp3;
				
				var temp6 = sendArray[j+1]
				sendArray[j+1] = sendArray[j];
				sendArray[j] = temp6;
				
			}
		}
		
	}//for
	newlatlng = [];
	for(var i = 0 ; i <memberLatlng.length-1 ; i ++){
		newlatlng.push(memberLatlng[i]);
	}
	//先劃出路徑圖; 如果先畫marker 會被路徑圖蓋掉
	calcRouteALL(latlngNCU,arrayLatlng[arrayLatlng.length-1],newlatlng);
	
	
	//劃出會員的marker
	for(var i = 0 ; i < sendArray.length ; i ++){
		var lat = arrayLatlng[i].lat()+m*100;
		//lat,lng,map,address, memberimage, mem_name , goods_image , goods_name
		mapMarker(lat,arrayLatlng[i].lng(),displayALL,sendArray[i]['address'],
				sendArray[i]['memberImage'],
				sendArray[i]['mem_name'],
				sendArray[i]['goodsImage'],
				sendArray[i]['goods_name']);

	}
	
	//畫出交換網的marker
	var lat = 24.9708264+m*100;
	mapMarker(lat,121.18820769999999,displayALL,'中央大學',$("#exicon").val(),'交換網','','交換網'); 
}



function calcRouteALL(start,end,ways) {
	 
	var area = document.getElementById('allLocation');
	var options ={
			zoom:11,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	
	latlngNCU = new google.maps.LatLng(24.9708264,121.18820769999999);
	
	
	displayALL = new google.maps.Map(area,options);
	google.maps.event.trigger(displayALL, "resize"); 
	directionsDisplay.setMap(displayALL);
	
	displayALL.setCenter(latlngNCU);	
	
	var start = start;
	var end = end;
	var request = {
	    origin:start,
	    destination:end,
	    waypoints:ways,
	    travelMode: google.maps.TravelMode.DRIVING
	  };
	  
	directionsService.route(request, function(result, status) {
	    if (status == google.maps.DirectionsStatus.OK) {
	      directionsDisplay.setDirections(result);
	    }
	  });
	  
	//劃出會員的marker
	for(var i = 0 ; i < sendArray.length ; i ++){
		var lat = arrayLatlng[i].lat()+m*100;
		//lat,lng,map,address, memberimage, mem_name , goods_image , goods_name
		mapMarker(lat,arrayLatlng[i].lng(),displayALL,sendArray[i]['address'],
				sendArray[i]['memberImage'],
				sendArray[i]['mem_name'],
				sendArray[i]['goodsImage'],
				sendArray[i]['goods_name']);

		 
	}
	
	//畫出交換網的marker
	var lat = 24.9708264+m*100;
	mapMarker(lat,121.18820769999999,displayALL,'中央大學',$("#exicon").val(),'交換網','','交換網'); 
	  
}