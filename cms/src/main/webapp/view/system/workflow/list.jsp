<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/tags" prefix="date"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${title }列表</title>
<link rel="stylesheet" href="/assets/css/chosen.css" />
</head>
<body>
	<script src="/js/list.js"></script>
	<form action="${baseUrl }/list${mid}.html" method="post" id="list_form"
		name="list_form"></form>
	<script src="/js/bootbox.min.js"></script>
	<script type="text/javascript">
		jQuery(function($) {
			$("a[id^='a_agree']").on(ace.click_event, function() {
				bootbox.confirm("确定审核通过吗?", function(result) {
					if (result) {
						alert($(this));
					}
				});
			});
			$("#a_refuse").on(ace.click_event, function() {
				bootbox.prompt("请填写拒绝理由!", function(result) {
					if (result === null) {
						//Example.show("Prompt dismissed");
					} else {
						//Example.show("Hi <b>"+result+"</b>");
					}
				});
			});
			$("#a_cancel").on(ace.click_event, function() {
				bootbox.confirm("确定取消审核吗?", function(result) {
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
											<c:when test="${column.tag == 'TEXT'}">
												<input name="${column.name }" type="text"
													value="${search.get(column.name) }" style="width: 172px;">
											</c:when>
											<c:when
												test="${column.tag == 'ITEM' || column.tag == 'DATAITEM'}">
												<select name="${column.name }" style="width: 172px;">
													<option value="">&nbsp;</option>
													<c:forEach var="item" items="${column.items }">
														<option value="${item.key }"
															<c:if test="${item.key == search.get(column.name)  }">selected="selected"</c:if>>${item.value}</option>
													</c:forEach>
												</select>
											</c:when>
											<c:when
												test="${column.tag == 'DATE' ||column.tag== 'DATETIME'}">
												<input type="text" name="${column.name }"
													value="${search.get(column.name)  }" id="date-range-picker"
													style="width: 172px;">
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
			<!--  数据显示区 -->
			<table id="sample-table-2"
				class="table table-striped table-bordered table-hover dataTable"
				aria-describedby="sample-table-2_info">
				<thead>
					<tr>
						<th class="center" style="width: 45px;"><span class="lbl">序号</span></th>
						<c:forEach var="column" items="${columns}">
							<c:if
								test="${column.display && empty powerColumns.get(column.name)}">
								<th
									style="<c:if test="${column.center }">text-align:center;</c:if> width: ${column.width}px;">
									${column.describe }</th>
							</c:if>
						</c:forEach>
						<th class="center" style="width:${actions.size() *23}px;">操作</th>
					</tr>
				</thead>
				<tbody role="alert" aria-live="polite" aria-relevant="all">
					<c:forEach var="data" items="${dataList}" varStatus="status">
						<tr
							class="<c:if test="${status.index % 2 == 0 }">even</c:if><c:if test="${status.index % 2 != 0 }">odd</c:if>">
							<td class="center"><span class="lbl">${ status.index + 1}</span></td>
							<c:forEach var="column" items="${columns}">
								<c:if
									test="${column.display && empty powerColumns.get(column.name)}">
									<td <c:if test="${column.center }">class="center"</c:if>>
										<c:choose>
											<c:when
												test="${ column.tag=='ITEM' || column.tag=='DATAITEM' }">
												<c:forEach var="datas" items="${column.items}">
													<c:if test="${data.get(column.name) == datas.key}">
															${datas.value}
															</c:if>
												</c:forEach>
											</c:when>
											<c:when test="${column.tag == 'TEXT'}">${data.get(column.name) }</c:when>
											<c:when test="${column.tag == 'DATE'}">
												<c:if test="${not empty data.get(column.name)}">
													<date:date value="${data.get(column.name) *1000}"
														pattern="yyyy-MM-dd" />
												</c:if>
											</c:when>
											<c:when test="${column.tag == 'DATETIME'}">
												<c:if test="${not empty data.get(column.name)}">
													<date:date value="${data.get(column.name)*1000}"
														pattern="yyyy-MM-dd HH:mm:ss" />
												</c:if>
											</c:when>
											<c:otherwise>
														${data.get(column.name)}
													</c:otherwise>
										</c:choose>
									</td>
								</c:if>
							</c:forEach>
							<td class="center">
								<div
									class='visible-md visible-lg hidden-sm hidden-xs action-buttons'>
									<c:forEach var="action" items="${actions }">
										<c:if
											test="${ action.type==1 && empty powerActions.get(action.name)}">
											<a class='${action.css}'
												<c:if test="${not empty action.confirm }">onclick="return confirm('${action.confirm}');"</c:if>
												href='${action.href }${mid }-${data.get(pk)}.html'
												title="${action.describe }"></a>
										</c:if>
									</c:forEach>
									<a class="green icon-ok bigger-130"
										id="a_agree${data.get(pk) }" title="同意"></a> <a
										class="red icon-remove bigger-130" id="a_refuse" title="拒绝"></a>
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
					<c:if
						test="${ action.type==0  && empty powerActions.get(action.name)}">
						<input onclick="window.location.href='${action.href}${mid}.html'"
							type="button" value="${action.describe }" class="${action.css }">
					</c:if>
				</c:forEach>
				<c:if test="${not empty pages and pages.totalPages > 1}">
					<span>共${pages.totalPages }页/${pages.totalRows }记录</span>
				</c:if>
			</div>
			<c:if test="${not empty pages and pages.totalPages > 1}">
				<div class="col-sm-6">
					<div class="dataTables_paginate paging_bootstrap">
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
											href='javascript:page("${baseUrl }/list${mid}-${p }.html");'>${p }</a></li>
									</c:if>
									<c:if test="${p > 1 }">
										<li><a
											href='javascript:page("${baseUrl }/list${mid}-${p }.html");'>${p }</a></li>
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
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>
