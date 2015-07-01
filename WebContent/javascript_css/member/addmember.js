/**
 * 
 */



function doFirst() {
	str = '';
	//日期欄位跳出日期選擇方塊
	$("#birthday").datepicker({
		changeMonth: true,
		changeYear: true,
		showButtonPanel: true,
		yearRange: "1900:2015"
	});
	
	$("#btnAuto").click(function(){
		//id="email" id="password" id="rePwd" id="mem_name"   id="id_no" id="tel" id="address" id="birthday"
		$("#email").val('ZA101G1@gmail.com');
		$("#password").val('123');
		$("#rePwd").val('123');
		$("#id_no").val(idCreater());
		$("#mem_name").val('交換王');
		$("#tel").val('0911078078');
		$("#address").val('中壢火車站');
		$("#birthday").val('06/01/2015');
		
		
	});
	
	//按下email欄位要輸入時 , 把提示訊息清除
	$(".email").click(function (){
		document.getElementById('ems').innerHTML = '';
	});
	
	
	//輸入完成用ajax 查詢資料庫email是否有重複 ,把結果顯示在提示訊息上
	$(".email").change(function (){
		document.getElementById('ems').innerHTML = '';
		var email = document.getElementById('email').value;
		
		if (email != str) {
			verify(email);
		} else {
			document.getElementById('ems').innerHTML = '';
		}
	});
	

	$("#pic").change(function (){
		 	var pic = document.getElementById("preview");
		    var file = document.getElementById("pic");
		    var ext=file.value.substring(file.value.lastIndexOf(".")+1).toLowerCase();
		    //gif在ie上無法顯示
		    if(ext!='png'&&ext!='jpg'&&ext!='jpeg'){
		        alert("文件必須為圖片！"); return;
		    }
		    //ie用
		    if (document.all) {

		        file.select();
		        var reallocalpath = document.selection.createRange().text;
		        var ie6 = /msie 6/i.test(navigator.userAgent);
		        //ie6設置的img的src為本地路徑可以直接顯示
		        if (ie6) pic.src = reallocalpath;
		        else {
		            //非ie6的須透過filter來處理
		            pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
		            
		            pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
		        }
		    }else{
		        html5Reader(file);
		    }
		
	});
	

}

function idCreater(){
	var ary = new Array(10,11,12,13,14,15,16,17,34,18,19,20,21,22,35,23,24,25,26,27,28,29,32,30,31,33);  
    var en= new Array("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
    var num= new Array("0","1","2","3","4","5","6","7","8","9");
    var id;
    var head;
    var create=false;
    var newId = new Array();
    do{
        var eng =ary[ Math.floor(Math.random()*26)];
        newId[0] = eng/10;
        newId[1] = eng%10;
        for(var i=2;i<11;i++)
            newId[i]=Math.floor(Math.random()*9);
        for(var i=1;i<10;i++)
            newId[0]=newId[0]+newId[i]*(10-i);
        var t=(10-newId[0]%10)%10;
        if(t==newId[10] && newId[2]<=2 && newId[2]>=1 ){
            create=true;
            for(var i=0;i<ary.length;i++){
                if(eng==ary[i]){
                    head = en[i];
                    break;
                }
            }
          id=head;
            for(var i=2;i<newId.length;i++){ 
               id+=newId[i];
            }
         }
      }while(create==false);
    return id;
 }


function html5Reader(file){
    var file = file.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function(e){
    	var previewArea = document.getElementById("previewArea");
        previewArea.innerHTML = "<img id='preview' width='120' height='120'  >";
    	var pic = document.getElementById("preview");
        pic.src=this.result;
    }
}

function verify(memId) {
	var xhr = new XMLHttpRequest();
	var result;
	xhr.onreadystatechange = function() {
		// readyState 0:尚未初始化, 1:請求已被建立 , 2:請求已被送出 , 3請求正在處理 , 4請求處理完成
		if (xhr.readyState == 4) {
			/*
			 * Server端回應狀態 statue 1xx:參考資訊, 2xx:成功 , 3xx:重新導向 , 4xx:用戶端錯誤 ,
			 * 5xx:伺服器錯誤
			 */
			if (xhr.status == 200) {
				result = xhr.responseText;
				document.getElementById('ems').innerHTML = result;

			} else
				alert(xhr.statusText);
		}
	} 
	

	var url = $("#url").val()+"/javascript_css/member/emailCheck.jsp";
	xhr.open('Post', url, true);
	var queryString = 'email=' + document.getElementById('email').value;
	// alert(queryString);
	xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	xhr.send(queryString);

}

window.addEventListener('load', doFirst, false);