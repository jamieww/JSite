<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>智慧夹管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ifolder/file/">智慧夹列表</a></li>
		<shiro:hasPermission name="ifolder:file:edit"><li><a href="${ctx}/ifolder/file/form">智慧夹添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="file" action="${ctx}/ifolder/file/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>文件名 ：</label><form:input path="name" htmlEscape="false" maxlength="255" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>文件名</th><th>父目录</th><th>类型</th><th>下载地址</th><shiro:hasPermission name="ifolder:file:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="file">
			<tr>
				<td><a href="${ctx}/ifolder/file/form?id=${file.id}">${file.name}</a></td>
                <td>${file.parent.name}</td>
                <td>${fns:getDictLabel(file.flag,'ifolder_file_flag','')}</td>
                <td>${file.address}</td>
				<shiro:hasPermission name="ifolder:file:edit"><td>
    				<a href="${ctx}/ifolder/file/form?id=${file.id}">修改</a>
					<a href="${ctx}/ifolder/file/delete?id=${file.id}" onclick="return confirmx('确认要删除该智慧夹吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
