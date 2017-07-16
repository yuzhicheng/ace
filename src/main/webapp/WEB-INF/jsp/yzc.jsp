<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>列表</title>

<link rel='stylesheet' href='/ace/static/stylesheets/bootstrap.css'>
<link rel='stylesheet' href='/ace/static/stylesheets/bootstrap.min.css'>
<link rel="stylesheet" href="/ace/static/stylesheets/dataTables.bootstrap.css">
</head>
<body>
	<script type="text/javascript" src="/ace/static/javascripts/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="/ace/static/javascripts/bootstrap.min.js"></script>
	<script type="text/javascript" src="/ace/static/javascripts/jquery.metisMenu.js"></script>
	<!-- Data Tables -->
	<script src="/ace/static/javascripts/jquery.dataTables.js"></script>
	<script src="/ace/static/javascripts/dataTables.bootstrap.js"></script>
	<script>
		$(document).ready(function() {
			$('.dataTables-example').dataTable();
		});
	</script>

	<div class="row">
		<div class="col-lg-12">
			<section class="panel">
				<header class="panel-heading">
					<h2>列表</h2>
				</header>
				<table
					class="table table-striped table-bordered table-hover dataTables-example">
					<thead>
						<tr>
							<th>序 号</th>
							<th>卡 号</th>
							<th>姓 名</th>
							<th>性 别</th>
							<th>电 话</th>
							<th>金额</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${userList}" var="user" varStatus="status">
							<tr>
								<td>${status.index+1 }</td>
								<td>${empty user.cardId?" ":user.cardId }</td>
								<td>${empty user.name?" ":user.name }</td>
								<c:choose>
									<c:when test="${empty user.sex }">
										<td>&nbsp;</td>
									</c:when>
									<c:when test="${user.sex == 0}">
										<td>男</td>
									</c:when>
									<c:when test="${user.sex == 1 }">
										<td>女</td>
									</c:when>
								</c:choose>
								<td>${empty user.tel?" ":user.tel }</td>
								<td>${empty user.count?" ":user.count }</td>
								<td><a href="/ace/user/modifyUser?id=${user.id }"
									class="btn btn-primary btn-xs "><span
										class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
									<a href="" class="btn btn-danger btn-xs "><span
										class="glyphicon glyphicon-trash" aria-hidden="true"></span></a> <a
									href="/ace/user/personstatistics?cardId=${user.cardId }"
									class="btn btn-success btn-xs "><span
										class="glyphicon glyphicon-link" aria-hidden="true"></span></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</section>
		</div>
	</div>

</body>
</html>