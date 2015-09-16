<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<script type="text/javascript">
	function checkPassword() {
		//alert(1);
		var pw1 = document.getElementById("password").value;
		var pw2 = document.getElementById("confirmpwd").value;
		if (pw1 == "" || pw2 == "") {
			alert("密码不能为空，请重新填写！");
			return false;
		} else if (pw1.length < 6 || pw2.length < 6) {
			alert("密码不能小于6个字符，请重新输入！");
			return false;
		} else if (pw1 != pw2) {
			alert("2次密码输入不一致！");
			return false;
		} else {
			document.getElementById("form").submit();
		}
	}

	function checkPass() {
		var pw1 = document.getElementById("password").value;
		var pw2 = document.getElementById("confirmpwd").value;
		if (pw1 != pw2) {
			alert("二次密码不一样，请重新输入。");
			document.getElementById("password").value = '';
			document.getElementById("confirmpwd").value = '';
		}
	}
</script>
</head>

<body>
	<form id="form" class="form-horizontal" role="form"
		action="${baseUrl}/save_psw${mid}-${id}.html" method="post">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">用户名 </label>
			<div class="col-sm-9">
				<input type="text" class="col-xs-10 col-sm-5" name="code"
					value="${code}" required="required" disabled />
			</div>
		</div>
		<div class="space-4"></div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">姓名 </label>
			<div class="col-sm-9">
				<input type="text" class="col-xs-10 col-sm-5" name="name"
					value="${name}" required="required" disabled />
			</div>
		</div>
		<div class="space-4"></div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">密码 </label>
			<div class="col-sm-9">
				<input type="password" id="password" class="col-xs-10 col-sm-5"
					name="password" />
			</div>
		</div>
		<div class="space-4"></div>
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right">确认密码 </label>
			<div class="col-sm-9">
				<input type="password" id="confirmpwd" class="col-xs-10 col-sm-5"
					name="confirmpwd" onblur="return checkPass();" />
				<div id="content"></div>
			</div>
		</div>
		<div class="space-4"></div>
		<div class="clearfix form-actions">
			<div class="col-md-offset-3 col-md-9">
				<button class="btn btn-info btn-sm" type="button"
					onClick="checkPassword()">
					<i class="icon-ok bigger-110"></i> 提交
				</button>
				&nbsp; &nbsp; &nbsp;
				<button class="btn btn-sm" type="reset"
					onclick="location.href='javascript:history.go(-1);'">
					<i class="icon-undo bigger-110"></i> 返回
				</button>
			</div>
		</div>
	</form>
</body>
</html>
