<%--
  Created by IntelliJ IDEA.
  User: zhoubin
  Date: 15/9/17
  Time: 上午8:55
  To change this template use File | Settings | File Templates.
  搜索区域
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="widget-box collapsed">
    <div class="widget-header">
        <h5>搜索</h5>

        <div class="widget-toolbar">
            <a href="javascript:;" data-action="collapse"> <i
                    class="1 icon-chevron-down bigger-125"></i></a>
        </div>
    </div>
    <div class="widget-body">
        <div class="widget-main row">
            <div class="search">
                <c:forEach var="column" items="${columns}">
                    <c:if test="${column.search }">
                        <div style="float: left; margin: 5px 5px;">
                            <label style="width: 70px; text-align: right;">${column.describe }：</label>
                            <c:choose>
                                <c:when test="${column.tag.toString() == 'TEXT'}">
                                    <input name="${column.name }" type="text"
                                           value="${search.get(column.name) }" style="width: 172px;">
                                </c:when>
                                <c:when
                                        test="${column.tag.toString() == 'ITEM' || column.tag.toString() == 'DATAITEM'}">
                                    <select name="${column.name }" style="width: 172px;">
                                        <option value="">&nbsp;</option>
                                        <c:forEach var="item" items="${column.items }">
                                            <option value="${item.key }"
                                                    <c:if test="${item.key == search.get(column.name)  }">selected="selected"</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:when
                                        test="${column.tag.toString() == 'DATE' || column.tag.toString() == 'DATETIME'}">
                                    <input type="text" name="${column.name }"
                                           value="${search.get(column.name)  }"
                                           id="date-range-picker" style="width: 172px;">
                                </c:when>
                            </c:choose>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <div class="widget-toolbox padding-8 clearfix">
            <button
                    class="btn btn-sm btn-purple ui-state-hover pull-right searchBtn"
                    onclick="submit()">
                <span class="icon-search"></span>开始查找
            </button>
        </div>
    </div>
</div>