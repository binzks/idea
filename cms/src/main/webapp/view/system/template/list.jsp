<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/tags" prefix="date" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title }列表</title>
</head>
<body>
<form action="${baseUrl }/list${mid}.html" method="post" id="list_form"
      name="list_form">
    <script language="javascript">
        function page(action) {
            document.list_form.action = action;
            list_form.submit();
        }
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
                                <c:forEach var="action" items="${actions }">
                                    <c:if test="${ action.type==1 && empty powerActions.get(action.name)}">
                                        <a class='${action.css}'
                                           <c:if test="${not empty action.confirm }">onclick="return confirm('${action.confirm}');"</c:if>
                                           href='${action.href }${mid }-${data.get(pk)}.html'
                                           title="${action.describe }"></a>
                                    </c:if>
                                </c:forEach>
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
</form>
</body>
</html>