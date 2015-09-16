function initMenu() {
	$.ajax({
		url:"/sys/getMenu.do",
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

function getMenu(data, pid) {
	var content = "";
	for (var i = 0; i < data.length; i++) {
		if (data[i].parent_id == pid) {
			if (data[i].type == 0) { // 模块组
				content += '<li><a href="#" class="dropdown-toggle">';
				content += '<i class=" + ' + data[i].icon + ' + "></i>';
				content += '<span class="menu-text">' + data[i].name
						+ '</span>';
				content += '<b class="arrow icon-angle-down"></b></a>';
				content += '<ul class="submenu">';
				content += getMenu(data, data[i].id);
				content += '</ul>';
			} else { // 模块
				content += '<li><a href="' + data[i].url + '.html">';
				content += '<i class="icon-double-angle-right"></i>'
						+ data[i].name + '</a><li>';
			}
		}
	}
	return content;
}

function docss() {
	var url = window.location.href;
	url = url.substring(url.indexOf(window.location.host)
			+ window.location.host.length, url.length);
	var i = url.indexOf("&");
	if (i > 0) {
		url = url.substring(0, i);
	}
	var a = $('ul.menu-ul').find('a[href="' + url + '"]');
	if (a.text().length == 0) {
		url = document.referrer;
		url = url.substring(url.indexOf(window.location.host)
				+ window.location.host.length, url.length);
		var i = url.indexOf("-");
		if (i > 0) {
			url = url.substring(0, i);
			url += ".html";
		}
		a = $('ul.menu-ul').find('a[href="' + url + '"]');
	}
	var li = a.parent();
	li.attr("class", "active");
	var ul = li.parent();
	ul.show();
	activeMenu(ul.parent());
}

function activeMenu(li) {
	if (li.attr("class") != undefined) {
		if (li.attr("class").indexOf("sidebar") == -1) {
			li.attr("class", "active open");
		}
	}
	var ul = li.parent();
	if (ul.attr("class") != undefined) {
		ul.show();
		activeMenu(ul.parent());
	}
}