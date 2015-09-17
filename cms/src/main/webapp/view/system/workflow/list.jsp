<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/tags" prefix="date" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title }列表</title>
    <link rel="stylesheet" href="/assets/css/chosen.css"/>
</head>
<body>
<form action="${baseUrl }/list${mid}.html" method="post" id="list_form"
      name="list_form"></form>
<script src="/js/bootbox.min.js"></script>
<script type="text/javascript">
    jQuery(function ($) {
        $("a[id^='a_agree']").on(ace.click_event, function () {
            bootbox.confirm("确定审核通过吗?", function (result) {
                if (result) {
                    alert($(this));
                }
            });
        });
        $("#a_refuse").on(ace.click_event, function () {
            bootbox.prompt("请填写拒绝理由!", function (result) {
                if (result === null) {
                    //Example.show("Prompt dismissed");
                } else {
                    //Example.show("Hi <b>"+result+"</b>");
                }
            });
        });
        $("#a_cancel").on(ace.click_event, function () {
            bootbox.confirm("确定取消审核吗?", function (result) {
                if (result) {
                    //
                }
            });
        });
    });
</script>

<div class="table-responsive">
    <div id="sample-table-2_wrapper" class="dataTables_wrapper"
         role="grid">
        <%@include file="/view/system/template/search.jsp" %>
        <!--  数据显示区 -->
        <table id="sample-table-2"
               class="table table-striped table-bordered table-hover dataTable"
               aria-describedby="sample-table-2_info">
            <thead>
            <tr>
                <%@include file="/view/system/template/listHead.jsp" %>
            </tr>
            </thead>
            <tbody role="alert" aria-live="polite" aria-relevant="all">
            <c:forEach var="data" items="${dataList}" varStatus="status">
                <tr class="<c:if test="${status.index % 2 == 0 }">even</c:if><c:if test="${status.index % 2 != 0 }">odd</c:if>">
                    <td class="center"><span class="lbl">${ status.index + 1}</span></td>
                    <%@include file="/view/system/template/listColumn.jsp" %>
                    <td class="center">
                        <div class='visible-md visible-lg hidden-sm hidden-xs action-buttons'>
                            <a class="blue icon-zoom-in bigger-130" href="/tpl/detail${mid}-${data.get(pk)}.html"
                               title="查看详情"></a>
                            <a class="green icon-pencil bigger-130" href="/tpl/edit${mid}-${data.get(pk)}.html"
                               title="编辑"></a>
                            <a class="green icon-ok bigger-130" id="a_agree${data.get(pk) }" title="同意"></a>
                            <a class="red icon-remove bigger-130" id="a_refuse" title="拒绝"></a>
                            <a class="red icon-off bigger-130" id="a_cancel" title="取消审核"></a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="row">
        <div class="col-sm-6">
            <c:forEach var="action" items="${actions }">
                <c:if test="${ action.type==0  && empty powerActions.get(action.name)}">
                    <input onclick="window.location.href='${action.href}${mid}.html'"
                           type="button" value="${action.describe }" class="${action.css }">
                </c:if>
            </c:forEach>
        </div>
        <%@include file="/view/system/template/paging.jsp" %>
    </div>
</div>
</body>
</html>
