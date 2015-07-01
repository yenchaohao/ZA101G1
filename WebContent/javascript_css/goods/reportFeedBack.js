var gid;
$(document).ready(function(){
	
	$(".btn").click(function(event){
		alert('hi');
		$("#"+gid).attr('data-target','#report');
		$.ajax({
			type:"POST",
			url:$("#url").val()+"/back/goods/reportFeedBack.jsp",
			data:creatQueryString(),
			dataType:"text",
			success:function(data){$("#modal-body").html(decodeURI(data))}
		});
		$("#"+gid).click();
	});
	
	
/*	$("#btn2").click(function(){
		if($("#member_id").val() == 'null'){
			tran.submit();
		}else{
			$("#bg").show();
			$.ajax({
				type:"POST",
				url:$("#url").val()+"/javascript_css/goods/tran.jsp",
				data:creatQueryString(),
				dataType:"text",
				success:function(data){$("#content").html(decodeURI(data))}
			});
			$("#content").show();
		}
			
		$("#bg").click(function(){
			$("#bg").hide();
			$("#content").hide();
		}); 
	
		
	}); */
});

function creatQueryString(){
	var queryString={gid:gid};
	return queryString;
 }