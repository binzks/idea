<%--
  Created by IntelliJ IDEA.
  User: zhoubin
  Date: 15/9/17
  Time: 上午8:58
  To change this template use File | Settings | File Templates.
  list的列显示
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/tags" prefix="date" %>

<c:forEach var="column" items="${columns}">
    <c:if
            test="${column.display && empty powerColumns.get(column.name)}">
        <td <c:if test="${column.center }">class="center"</c:if>>
            <c:choose>
                <c:when
                        test="${ column.tag.toString() == 'ITEM' || column.tag.toString() == 'DATAITEM' }">
                    <c:forEach var="item" items="${column.items}">
                        <c:if test="${data.get(column.name) == item.key}">
                            ${item.value}
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:when test="${column.tag.toString() == 'DATE'}">
                    <c:if test="${not empty data.get(column.name)}">
                        <date:date value="${data.get(column.name) *1000}"
                                   pattern="yyyy-MM-dd"/>
                    </c:if>
                </c:when>
                <c:when test="${column.tag.toString() == 'DATETIME'}">
                    <c:if test="${not empty data.get(column.name)}">
                        <date:date value="${data.get(column.name)*1000}"
                                   pattern="yyyy-MM-dd HH:mm:ss"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    ${data.get(column.name)}
                </c:otherwise>
            </c:choose>
        </td>
    </c:if>
</c:forEach>