function getDs(urlString) {
	$.ajax({
		url:urlString,
		type:"post",
		dataType:"json",
		success:function(data){
			if(data&&data.length!=0){
				content = getMenu(eval(data), 0);
				$("#mainMenus").append(content);
				docss();
			}
		},
		error:function(){
			
		}
	});	
}