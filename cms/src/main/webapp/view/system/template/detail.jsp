<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/tags" prefix="date" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title }详情</title>
</head>
<body>
<div class="profile-info profile-user-info-striped">
    <c:forEach var="column" items="${columns}">
        <c:if test="${ column.detail &&  empty powerColumns.get(column.name)}">
            <c:choose>
                <c:when test="${column.tag.toString() == 'DATE' }">
                    <div class="profile-info-row">
                        <div class="profile-info-name">${column.describe }</div>
                        <div class="profile-info-value">
                            <date:date value="${data.get(column.name) *1000}"
                                       pattern="yyyy-MM-dd"/>
                            &nbsp;
                        </div>
                    </div>
                </c:when>
                <c:when test="${ column.tag.toString() == 'DATETIME'}">
                    <div class="profile-info-row">
                        <div class="profile-info-name">${column.describe }</div>
                        <div class="profile-info-value">
                            <date:date value="${data.get(column.name) *1000}"
                                       pattern="yyyy-MM-dd HH:mm:ss"/>
                            &nbsp;
                        </div>
                    </div>
                </c:when>
                <c:when test="${ column.tag.toString() == 'ITEM' || column.tag.toString() == 'DATAITEM' }">
                    <c:forEach var="item" items="${column.items }">
                        <c:if test="${data.get(column.name) == item.key }">
                            <div class="profile-info-row">
                                <div class="profile-info-name">${column.describe }</div>
                                <div class="profile-info-value">${item.value }&nbsp;</div>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="profile-info-row">
                        <div class="profile-info-name">${column.describe }</div>
                        <div class="profile-info-value">${data.get(column.name) }&nbsp;</div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:forEach>
</div>
<div class="row-fluid wizard-actions">
    <button class="btn btn-prev btn-sm"
            onclick="location.href='javascript:history.go(-1);'">
        <i class="icon-arrow-left">返回</i>
    </button>
</div>
</body>
</html>