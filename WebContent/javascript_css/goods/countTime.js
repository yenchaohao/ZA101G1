$(document).ready(
		function() {
			setInterval(function() {

				count = $("#count").val();

				if (count != null) {
					for (var i = 0; i < count; i++) {
						if ($("#goods_time" + i).val() != null) {
							var time = $("#goods_time" + i).val();

							time = time - 1000;
							// alert(time);
							$("#goods_time" + i).val(time);
							var date = new Date(time);
							if (time > 0) {
								miles = Math.floor(time / 1000);

								day = Math.floor(((miles / 60) / 60) / 24);
								hour = Math.floor(((miles / 60) / 60) % 24);
								minutes = Math.floor((miles / 60) % 60);

								seconds = Math.floor(miles % 60);

								$("#quitdate" + i).html(
										decodeURI("<font color='red' size='1'><b>還有:" + day
												+ "天" + hour + "時" + minutes
												+ "分" + seconds + "秒"
												+ "</b></font>"));
 
							} else {
								$("#quitdate" + i).html(decodeURI('此商品已下架'));
								$("#imageLink"+i).attr("href","");
							}
						}
					}
				}

			}, 1000);
		});
