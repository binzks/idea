<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags" prefix="date" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>编辑${title }</title>

</head>
<body>
<form class="form-horizontal" role="form"
      action="${baseUrl }/save${mid}-${id }.html" method="post">
    <c:forEach var="column" items="${columns}">
        <c:if test="${column.add }">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">
                        ${column.describe} </label>

                <div class="col-sm-9">
                    <c:choose>
                        <c:when test="${ column.tag.toString() == 'ITEM' || column.tag.toString() == 'DATAITEM' }">
                            <select class="select option, select.form-control option"
                                    <c:if test="${column.required }">required="required"</c:if>
                                    name="${column.name }">
                                <option></option>
                                <c:forEach var="item" items="${column.items}">
                                    <option value="${item.key}" id="${item.key}"
                                            <c:if test="${item.key==data.get(column.name) }">selected</c:if>>${item.value}</option>
                                </c:forEach>
                            </select>
                        </c:when>
                        <c:when test="${ column.tag.toString() == 'DATE' }">
                            <div class="form-group">
                                <div class="col-xs-4">
                                    <div class="input-group input-group-sm">
                                        <input type="text" class="Wdate" name="${column.name }"
                                               value="<date:date value="${data.get(column.name)*1000}" pattern="yyyy-MM-dd"/>"
                                               onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true})"
                                               placeholder="选择日期">
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${ column.tag.toString() == 'DATETIME' }">
                            <div class="form-group">
                                <div class="col-xs-4">
                                    <div class="input-group input-group-sm">
                                        <input type="text" class="Wdate" name="${column.name }"
                                               value="<date:date value="${data.get(column.name)*1000}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                                               onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                                               placeholder="选择时间">
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${ column.tag.toString() == 'PASSWORD' }">
                            <input type="password" class="col-xs-10 col-sm-5"
                                   name="${column.name }" value="${data.get(column.name) }"
                                   <c:if test="${column.required }">required="required"</c:if> />
                        </c:when>
                        <c:when test="${column.tag.toString() == 'INT' }">
                            <input type="text" class="col-xs-10 col-sm-5"
                                   onkeyup="value=value.replace(/[^\d]/g,'') "
                                   onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
                                   name="${column.name }" value="${data.get(column.name) }"
                                   <c:if test="${column.required }">required="required"</c:if> />
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="col-xs-10 col-sm-5"
                                   name="${column.name }" value="${data.get(column.name) }"
                                   <c:if test="${column.required }">required="required"</c:if> />
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="space-4"></div>
        </c:if>
    </c:forEach>

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