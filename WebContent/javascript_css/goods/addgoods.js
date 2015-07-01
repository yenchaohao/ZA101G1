/**
 * 
 */

function doFirst() {
	
	$("#pic2").attr("disabled",true);
	$("#pic3").attr("disabled",true);

	str = '';
	// 日期欄位跳出日期選擇方塊
	$("#birthday").datepicker({
		changeMonth : true,
		changeYear : true,
		showButtonPanel : true,
		yearRange : "1900:2015"
	});

	// 按下email欄位要輸入時 , 把提示訊息清除
	$(".email").click(function() {
		document.getElementById('ems').innerHTML = '';
	});

	// 輸入完成用ajax 查詢資料庫email是否有重複 ,把結果顯示在提示訊息上
	$(".email").change(function() {
		document.getElementById('ems').innerHTML = '';
		var email = document.getElementById('email').value;

		if (email != str) {
			verify(email);
		} else {
			document.getElementById('ems').innerHTML = '';
		}
	});

	$("#send_button").click(function(){
		$("#pic1").attr("disabled",false);
		$("#pic2").attr("disabled",false);
		$("#pic3").attr("disabled",false);
		addGoodsForm.submit();
	});
	
	
	$(".pic").change(
					//event 當前的物件
					function(event) {
						
						//取得當前的物件的id
						var file = document.getElementById(event.target.id);
						if(event.target.id == 'pic1')
							$("#pic2").attr("disabled",false);
						else if(event.target.id == 'pic2')
							$("#pic3").attr("disabled",false);
						else if(event.target.id == 'pic3')
							$("#pic1").attr("disabled",false);
						
						
						var ext = file.value.substring(
								file.value.lastIndexOf(".") + 1).toLowerCase();
						// gif在ie上無法顯示
						if (ext != 'png' && ext != 'jpg' && ext != 'jpeg') {
							alert("文件必須為圖片！");
							return;
						}
						// ie用
						if (document.all) {

							file.select();
							var reallocalpath = document.selection
									.createRange().text;
							var ie6 = /msie 6/i.test(navigator.userAgent);
							// ie6設置的img的src為本地路徑可以直接顯示
							if (ie6)
								pic.src = reallocalpath;
							else {
								// 非ie6的須透過filter來處理
								pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\""
										+ reallocalpath + "\")";

								pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
							}
						} else {
							html5Reader(file,event.target.id);
						}
					
					
					
					});

}

//傳進來的id 就是使用者當前點選要上傳的圖片event.target.id
function html5Reader(file,id) {
	
	var file = file.files[0];
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e) {
			
			//先取得要顯示圖片的id
			var img = document.getElementById("img"+id);
			//如果該圖片已經存在
			if(img != null){
				img.src = this.result;
				return;
			}
			
			//如果不存在
			var previewArea = document.getElementById("previewArea");
			var node = document.createElement('img');
		    node.id = 'img'+id;
			node.height = '120';
			node.width = '120';
			node.src = this.result;
			previewArea.appendChild(node);
			
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

	var url = "../member/emailCheck.jsp";
	xhr.open('Post', url, true);
	var queryString = 'email=' + document.getElementById('email').value;
	// alert(queryString);
	xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	xhr.send(queryString);

}

window.addEventListener('load', doFirst, false);