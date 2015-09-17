<%--
  Created by IntelliJ IDEA.
  User: zhoubin
  Date: 15/9/17
  Time: 上午8:22
  To change this template use File | Settings | File Templates.
  分页区域
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty pages and pages.totalPages > 1}">
    <div class="dataTables_paginate paging_bootstrap">
        <span style="text-align:center;">共${pages.totalPages }页/${pages.totalRows }记录</span>
        <ul class="pagination">
            <c:if test="${pages.startPage == 1 }">
                <li class="prev disabled"><a href="javascript:;"><i
                        class="icon-double-angle-left"></i></a></li>
            </c:if>
            <c:if test="${pages.startPage > 1 }">
                <li class="prev"><a
                        href='javascript:page("${baseUrl }/list${mid}.html");'><i
                        class="icon-double-angle-left"></i></a></li>
            </c:if>
            <c:if test="${pages.startPage > 1 }">
                <li class="prev"><a
                        href='javascript:page("${baseUrl }/list${mid}-${pages.startPage-1 }.html");'><i
                        class="icon-angle-left"></i></a></li>
            </c:if>
            <c:forEach var="p" begin="${pages.startPage }"
                       end="${pages.endPage }">
                <c:if test="${pages.currentPage == p }">
                    <li class="active"><a href="javascript:;">${p }</a></li>
                </c:if>
                <c:if test="${pages.currentPage != p }">
                    <c:if test="${p == 1 }">
                        <li><a
                                href='javascript:page("${baseUrl }/list${mid}-${p }.html");'>${p }</a>
                        </li>
                    </c:if>
                    <c:if test="${p > 1 }">
                        <li><a
                                href='javascript:page("${baseUrl }/list${mid}-${p }.html");'>${p }</a>
                        </li>
                    </c:if>
                </c:if>
            </c:forEach>
            <c:if test="${pages.endPage < pages.totalPages }">
                <li class="next"><a
                        href='javascript:page("${baseUrl }/list${mid}-${pages.endPage+1 }.html");'><i
                        class="icon-angle-right"></i></a></li>
            </c:if>
            <c:if test="${pages.endPage == pages.totalPages }">
                <li class="next disabled"><a href="javascript:;"><i
                        class="icon-double-angle-right"></i></a></li>
            </c:if>
            <c:if test="${pages.endPage < pages.totalPages }">
                <li class="next"><a
                        href='javascript:page("${baseUrl }/list${mid}-${pages.totalPages }.html");'><i
                        class="icon-double-angle-right"></i></a></li>
            </c:if>
        </ul>
    </div>
</c:if>