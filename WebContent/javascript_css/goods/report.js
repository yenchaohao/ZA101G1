$(document).ready(function(){
	
	$("#report_button").click(function(){
		if($("#member_id").val() == 'null'){
			//如果沒登入 就送出表單,表單會送到登入頁面
			reportForm.submit();
		}else if($("#isReport").val() != 'null'){
			$("#reportMessage").html(decodeURI("此商品你已經檢舉過摟,感謝你的回報"));
			$("#message").html(decodeURI(""));
		}else{
			//如果會員已經登入,也沒有檢舉過商品,在#report_button 後面家商  data-target = #report 會參照到 report.file 的 div 的 id
			$("#report_button").attr('data-target','#report');
			//然後再按下 就會彈跳出視窗了
			$("#report_button").click();
		}
		
	});
});

function creatQueryString(){
	var res_member_id=encodeURI($("#res_member_id").val());
	var res_gid= encodeURI($("#res_gid").val()) ;
	var queryString={res_member_id:res_member_id,res_gid:res_gid};
	return queryString;
 }