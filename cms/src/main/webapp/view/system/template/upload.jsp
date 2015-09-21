<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/tags" prefix="date" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title }</title>
</head>
<body>

<script src="/iJs/dropzone.min.js"></script>

<form id="edit_form" class="form-horizontal" method="post" enctype="multipart/form-data"
      action="${baseUrl}/savePic${mid}-${id}.html">
    <input type="hidden" name="menuId" value="${instance.menuId}">
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right"> icon </label>
        <div class="col-sm-4">
            <input type="file" name="imgPathImage" id="imgPathImage">

            <div class="col-xs-10 no-padding-right no-padding-left"></div>
        </div>
        <label class="errormsg"></label>
        <script type="text/javascript">
            jQuery(function ($) {
                $('input[name="imgPathImage"]').ace_file_input(
                        {
                            no_file: '请上传文件',
                            btn_choose: ' 选择 ',
                            btn_change: ' 更换 ',
                            droppable: false,
                            onchange: null,
                            thumbnail: 'small',
                            thumbnail: false,
                            blacklist: 'exe|zip|rar'
                        });
            });
        </script>
    </div>
    <div style="height: 50px;" class="">
        <div class="col-md-offset-3 col-md-9">
            <button type="submit" class="btn btn-info">
                <i class=""></i>保存
            </button>
            <button class="btn" type="button" onclick="javascript:history.back(-1);">
                <i class=""></i>取消
            </button>
        </div>
    </div>
</form>

<div>
    <label class="col-sm-3 control-label no-padding-right"> ${filePath} </label>
</div>


<script type="text/javascript">
    function onSubmit() {
        $('#edit_form').submit();
    }
</script>

</body>
</html>