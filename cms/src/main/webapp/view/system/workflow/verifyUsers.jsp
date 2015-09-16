<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑审核人</title>

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
		var id = document.getElementById('id').value;
		$.ajax({
			url : "/workflow/getTree.do",
			data : {
				"id" : id
			},
			type : "post",
			dataType : "json",
			success : function(data) {
				if (data && data.length != 0) {
					$.fn.zTree.init($("#tree"), setting, data);
				}
			},
			error : function() {
				alert("数据加载异常");
			}
		});
	});

	function onCheck() {
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getCheckedNodes(true);
		var idVal = ",";
		var nameVal = ",";
		for (var i = 0; i < nodes.length; i++) {
			idVal += nodes[i].id + ",";
			nameVal += nodes[i].name + ",";
		}
		$("#idValChecked").val(idVal);
		$("#nameValChecked").val(nameVal);
	}
</SCRIPT>

</head>
<body>
	<form class="form-horizontal" role="form"
		action="${baseUrl }/save_verify_users${mid}-${id}.html" method="post">
		<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"></label>
			<div class="col-sm-9">
				<input type="hidden" id="id" value=${id } /> <input
					type="hidden" id="idValChecked" name="idValChecked" />
					<input
					type="hidden" id="nameValChecked" name="nameValChecked" />
				<div class="col-sm-6">
					<div class="widget-header header-color-blue2">
						<h5 class="lighter smaller">选择审核人[${name }]</h5>
					</div>
					<div class="widget-body">
						<div class="widget-main padding-8">
							<ul id="tree" class="ztree"></ul>
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
