/**
 * 
 */
//建立增加圖片的按鈕
var addImage = document.createElement('input');
addImage.type = 'button';
addImage.id = 'addImage';
addImage.value = '增加附加圖片';
//建立刪除圖片的按鈕
var deleteImage = document.createElement('input');
deleteImage.type = 'button';
deleteImage.id = 'deleteImage';
deleteImage.value = '刪除圖片';
//紀錄使用者按下刪除圖片的index , 當使用者按下增加圖片時,才可以由此陣列的資料做依據要去生成第幾張圖片
var indexs = [];

var imageCount;
var index;

function doFirst() {
	//網頁載入時先把增加圖片的按鈕放到span中,並且隱藏
	$('#buttonArea').append(addImage);
	$('#addImage').hide();
	
	//會取得這個商品總共有幾張附圖
	imageCount = parseInt($("#imageCount").val());
	
	//如果沒有任何一張圖,input value會是空值,故給他0
	if($("#imageCount").val() == null){
		imageCount = 0;
	}

	//如果總圖小於3 ,就代表要先把可以更新圖片的欄位隱藏起來,等到使用者按下"增加圖片按鈕"時,才顯示
	if (imageCount < 3) {
		for (var i = imageCount + 1; i <= 3; i++) {
			$("#pic" + i).hide();
			$("#picTitle" + i).hide();
			$("#" + i + "delete").hide();
		}//顯示增加圖片按鈕
		$('#addImage').show();
	}

	$('#addImage').click(function() {
		
		if (imageCount < 3) {
			//index 和 indexs[] 都是記錄使用者按下刪除後的該圖片編號,如果是0或是null 就代表使用者沒有按下任何刪除按鈕,純粹是想增加圖片
			if (index == null || indexs.length == 0) {
				//imageCount 紀錄者當前頁面總共有幾張圖準備被使用者上傳或更新,因為最多不可以超過3張,imageCount 要紀錄者現在頁面上傳圖片按鈕的狀況
				imageCount++;
				$("#pic" + imageCount).show(); //input file
				$("#picTitle" + imageCount).show(); //圖片標題
				$("#" + imageCount + "delete").show(); //圖片刪除的按鈕
				
				$("#imgpic" + index).show(); //圖片 img src
				if (imageCount == 3) //如果按下3張了,增加圖片按鈕就隱藏
					$('#addImage').hide();
			//else 代表使用者有按下刪除按鈕後,然後又按下增加按鈕 index 或 indexs 才會有值
			} else {
				imageCount++;
				if(indexs.length > 0){
					//把該圖的change欄位設定成1 == 更新 ,讓後湍可以知道此圖是要做更新的動作
					if($("#pic"+indexs[0]).val() != null)
						$("#pic"+indexs[0]+"change").val(1);
					
					$("#pic" + indexs[0]).show();
					$("#picTitle" + indexs[0]).show();
					$("#" + indexs[0] + "delete").show();
					$("#imgpic" + indexs[0]).show();
					//at position 0 , remove 1 item
					//把剛剛存到 indexs[0] 的欄位刪掉,因為已經顯示了. 該陣列紀錄者"第幾張" 圖被刪掉,再依據此index 顯示回去 例: index[0] = 2 (第二張圖被刪掉,第二章圖顯示回去)
					indexs.splice(0,1);
				}

				if (imageCount == 3)
					$('#addImage').hide();
			}
		} else
			$('#addImage').hide();

	});
	
	$("#send_button").click(function(){
		addGoodsForm.submit();
	});

	$(".delete").click(function(event) {
		//所有delete 的按鈕 的class 都是delete,此監聽可以知道使用者按下的是第幾顆刪除按鈕
		index = event.target.id.substring(0, 1);
		//將change 欄位設定成2 ,後端會依據此數字做刪除
		$("#pic"+index+"change").val(2); 
		imageCount--;
		$("#pic" + index).hide();
		$("#picTitle" + index).hide();
		$("#" + index + "delete").hide();
		$("#imgpic" + index).hide();
		//把這個數字記錄起來,如果使用者又後悔案新增圖片新增回去,可以依據indexs 陣列存的值,知道他剛剛刪掉的是第幾張圖,就顯示回去第幾張圖
		indexs.push(index);
		//把陣列座由小到大排序. 由小到大顯示回去
		indexs.sort();
		if (imageCount < 3) {
			$('#addImage').show();
		}

	});

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

	$(".pic")
			.change(
					// event 當前的物件
					function(event) {
						
						//在這邊代表使用者有更換圖片,用更換的id去抓是哪張圖片被更換,然後再input hidden 塞進去值代表此圖使用者有更換
						$("#"+event.target.id+"change").val(1);
						//alert("#"+event.target.id+"change"+"  "+$("#"+event.target.id+"change").val());
						// 取得當前的物件的id
						var file = document.getElementById(event.target.id);

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
							html5Reader(file, event.target.id);
						}

					});

}

// 傳進來的id 就是使用者當前點選要上傳的圖片event.target.id
function html5Reader(file, id) {

	var file = file.files[0];
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function(e) {

		// 先取得要顯示圖片的id
		var img = document.getElementById("img" + id);
		// 如果該圖片已經存在
		if (img != null) {
			img.src = this.result;
			return;
		}

		// 如果不存在
		var previewArea = document.getElementById("previewArea");
		var node = document.createElement('img');
		node.id = 'img' + id;
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