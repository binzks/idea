 /**
		 * 获取时间转化为字符串
		 * @param dayNum 当前为0，昨天为1
		 * @return yyyy-MM-dd
		 */
		function getDateToString(dayNum) {
			var date = new Date();
			date.setDate(date.getDate() + dayNum);
		    var y = date.getFullYear();
		    var m = date.getMonth()+1;
		    var d = date.getDate();
		    if (m < 10) {
		    	m = "0" + m;
		    }
		    if (d < 10) {
		    	d = "0" + d;
		    }
		    return y + "-" + m + "-" + d;
		}
		
		/** 
		 * 从某个时间点起，增加N天，返回对应的天数 
		 * @param String dateStr "2014-09-01 hh:mm"
		 * @param int dayNum 天数；为负值时是减少多少天；如果需要增加30天，则dayNum=29，因为dateStr当天算一天。
		 * @author mawenjun 
		 **/
		function getAddDays(dateStr, dayNum) {
			var strs = dateStr.split(" ")[0].split("-");
			var mm = strs[1];
			if (mm == "01") {
				mm = "0";
			} else {
				mm = mm - 1;
			}
			var date = new Date(strs[0], mm, strs[2]);
			date.setDate(date.getDate() + dayNum);
		    var y = date.getFullYear();
		    var m = date.getMonth()+1;
		    var d = date.getDate();
		    if (m < 10) {
		    	m = "0" + m;
		    }
		    if (d < 10) {
		    	d = "0" + d;
		    }
		    return y + "-" + m + "-" + d;
		}