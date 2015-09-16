<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/tags" prefix="date"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>授权</title>

<link rel="stylesheet" href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css"
	type="text/css">
<script type="text/javascript" src="/js/zTree_v3/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="/js/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"
	src="/js/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>

<SCRIPT type="text/javascript">
	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onCheck : onCheck
		}
	};

	$(document).ready(function() {
		var roleid = document.getElementById('roleid').value;
		$.ajax({
			url : "/role/getTree.do",
			data : {
				"id" : roleid
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data && data.length != 0) {
					$.fn.zTree.init($("#modelTrees"), setting, data);
				}
			},
			error : function() {
				alert("数据加载异常");
			}
		});
	});

	function onCheck(e, treeId, treeNode) {
		var treeObj = $.fn.zTree.getZTreeObj("modelTrees");
		var nodes = treeObj.getCheckedNodes(true);
		var moduleVal = ",";
		for (var i = 0; i < nodes.length; i++) {
			var id = nodes[i].id;
			if (id.indexOf("@m@") != -1) {
				moduleVal += id + ",";
			}
		}
		$("#moduleChecked").val(moduleVal);
		var nodes = treeObj.getCheckedNodes(false);
		var columnVal = "";
		var actionVal = "";
		for (var i = 0; i < nodes.length; i++) {
			var id = nodes[i].id;
			if (id.indexOf("@c@") != -1) {
				columnVal += id + ",";
			} else if (id.indexOf("@a@") != -1) {
				actionVal += id + ",";
			}
		}
		$("#columnUnChecked").val(columnVal);
		$("#actionUnChecked").val(actionVal);
	}
</SCRIPT>

</head>
<body>
	<form class="form-horizontal" role="form"
		action="${baseUrl }/save_power${mid}-${id}.html" method="post">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"></label>
			<div class="col-sm-9">
				<input type="hidden" id="roleid" value=${id } /> <input
					type="hidden" id="moduleChecked" name="moduleChecked" /> <input
					type="hidden" id="columnUnChecked" name="columnUnChecked" /> <input
					type="hidden" id="actionUnChecked" name="actionUnChecked" />
				<div class="col-sm-6">
					<div class="widget-header header-color-blue2">
						<h5 class="lighter smaller">选择权限[${code }][${name }]</h5>
					</div>
					<div class="widget-body">
						<div class="widget-main padding-8">
							<ul id="modelTrees" class="ztree"></ul>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="space-4"></div>


		<div class="clearfix form-actions">
			<div class="col-md-offset-3 col-md-9">
				<button class="btn btn-info btn-sm" type="submit">
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
