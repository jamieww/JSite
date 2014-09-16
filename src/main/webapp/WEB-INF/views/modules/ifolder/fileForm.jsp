<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>智慧夹管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ifolder/file/">智慧夹列表</a></li>
		<li class="active"><a href="${ctx}/ifolder/file/form?id=${file.id}">智慧夹<shiro:hasPermission name="ifolder:file:edit">${not empty file.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="ifolder:file:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="file" action="${ctx}/ifolder/file/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">文件名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="required"/>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label">父目录：</label>
            <div class="controls">
                <tags:treeselect id="file" name="parent.id" value="${file.parent.id}" labelName="parent.name" labelValue="${file.parent.name}"
                                 title="目录" url="/ifolder/file/treeData" extId="${file.id}"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">类型：</label>
            <div class="controls">
                <form:select path="flag">
                    <form:options items="${fns:getDictList('ifolder_file_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">下载地址：</label>
            <div class="controls">
                <form:input path="address" htmlEscape="false" maxlength="1000"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">上次修改时间：</label>
            <div class="controls">
                <input id="modified" name="modified" type="text" readonly="readonly" maxlength="20" class="Wdate required"
                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
            </div>
        </div>
		<div class="form-actions">
			<shiro:hasPermission name="ifolder:file:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
