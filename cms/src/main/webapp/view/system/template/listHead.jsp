<%--
  Created by IntelliJ IDEA.
  User: zhoubin
  Date: 15/9/17
  Time: 上午9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<th class="center" style="width: 45px;"><span class="lbl">序号</span></th>
<c:forEach var="column" items="${columns}">
    <c:if test="${column.display && empty powerColumns.get(column.name)}">
        <th style="
        <c:if test="${column.center }">text-align:center;</c:if> width: ${column.width}px;">
                ${column.describe }</th>
    </c:if>
</c:forEach>
<th class="center" style="width:${optWidth}px;">操作</th>