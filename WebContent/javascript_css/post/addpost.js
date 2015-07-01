$(document).ready(function(){
	
	$("#pic").change( 
			function(){
					var file = document.getElementById("pic");
				    var ext=file.value.substring(file.value.lastIndexOf(".")+1).toLowerCase();
				    //gif在ie上無法顯示
				    if(ext!='png'&&ext!='jpg'&&ext!='jpeg'){
				        alert("文件必須為圖片！"); return;
				    }else{
				        html5Reader(file);
				    }
			});
	});

function html5Reader(file){
    var file = file.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function(e){
        var preview = document.getElementById("preview");
        preview.innerHTML = "<img id='prePic' width='120' height='120'  >";
    	var pic = document.getElementById("prePic");
        pic.src=this.result;
    }
}