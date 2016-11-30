<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<!-- If IE use the latest rendering engine -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Set the page to the width of the device and set the zoon level -->
<meta name="viewport" content="width = device-width, initial-scale = 1">
<title>jTech Login</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
<link href="${pageContext.request.contextPath}/resources/custom/css/custom.css" rel="stylesheet">


</head>
<body>

	<!-- HEADER START -->

	<div class="collapse navbar-collapse navbar-inverse"
		id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<li class="active"><a href="#">Home <span class="sr-only">(current)</span></a></li>
			<li><a href="#">About</a></li>
			<li><a href="#">Contact Us</a></li>
		</ul>
		<!-- navbar-left will move the search to the left -->
		<form class="navbar-form navbar-right" role="search">
			<a href="${pageContext.request.contextPath}/console" type="button" class="btn btn-warning">My Console</a>
		</form>
	</div>
	<br>


	<!-- HEADER END -->

	<div class="container">
		<form action="/login" method="post">
			<div class="col-md-8 col-md-offset-2">

				<!-- page-header adds space aroundtext and enlarges it. It also adds an underline at the end -->

				<div class="page-header" style="text-align: center;">
					<h1>Login to My Console</h1>
				</div>
				
				<div class="form-group">
					<h4><label for="inputlg">
							<b>Email Address</b>
						</label></h4> <input class="form-control input-lg" id="inputlg" name="username" type="text">
				</div>

				<div class="form-group">
					<h4><label for="inputlg">
							<b>Password</b>
						</label></h4> <input class="form-control input-lg" id="inputlg" name="password" type="password">
				</div>
				
				<br>

				<div class="text-center" role="group" aria-label="...">
					<button type="submit" class="btn btn-primary btn-lg">LOGIN</button>
				</div>
				
				<c:if test="${param.error ne null}">
					<br><div class="text-center alert-danger"><h4>Invalid username or password.</h4></div>
				</c:if>
				<c:if test="${param.logout ne null}">
					<br><div class="text-center alert-success"><h4>You have been logged out.</h4></div>
				</c:if>
			
				<br>
				<hr>

				<div class="text-center" role="group" aria-label="...">
					<a href="${pageContext.request.contextPath}/register">Create jTech Account</a>
				</div>

			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
	</div>

	<!-- FOOTER START -->
	<footer class="footer">
		<div class="container">
			<p class="text-muted">2016 jTech. All rights reserved.</p>
		</div>
	</footer>
	<!-- FOOTER END -->

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>