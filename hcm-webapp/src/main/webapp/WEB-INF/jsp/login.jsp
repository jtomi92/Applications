<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta charset="UTF-8">
<!-- If IE use the latest rendering engine -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Set the page to the width of the device and set the zoon level -->
<meta name="viewport" content="width = device-width, initial-scale = 1">
<title>jTech Login</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
<link
	href="${pageContext.request.contextPath}/resources/custom/css/custom.css"
	rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">

</head>
<body>

	<jsp:include page="wrapper/header.jsp">
		<jsp:param name="firstname" value="${firstname}" />
	</jsp:include>

	<div class="container">
		<form name='login' action="<c:url value='/login' />" method='POST'>
			<div class="col-md-8 col-md-offset-2">

				<!-- page-header adds space aroundtext and enlarges it. It also adds an underline at the end -->

				<div class="page-header" style="text-align: center;">
					<h1>Login to My Console</h1>
				</div>

				<div class="form-group">
					<h4>
						<label for="inputlg"> <b>Email Address</b>
						</label>
					</h4>
					<input class="form-control input-lg" id="inputlg" name="username"
						type="text">
				</div>

				<div class="form-group">
					<h4>
						<label for="inputlg"> <b>Password</b>
						</label>
					</h4>
					<input class="form-control input-lg" id="inputlg" name="password"
						type="password">
				</div>

				<div style="text-align: center;">
					<h4><label for="remember-me">Remember me <input type="checkbox"
						name="remember-me" id="remember-me"></label></h4> 
				</div>

				<br>

				<div class="text-center" role="group" aria-label="...">
					<button type="submit" class="btn btn-primary btn-lg">LOGIN</button>
				</div>

				<c:if test="${param.error ne null}">
					<br>
					<div class="text-center alert-danger">
						<h4>Invalid username or password.</h4>
					</div>
				</c:if>
				<c:if test="${param.logout ne null}">
					<br>
					<div class="text-center alert-success">
						<h4>You have been logged out.</h4>
					</div>
				</c:if>

				<br>
				<hr>

				<div class="text-center" role="group" aria-label="...">
					<a href="${pageContext.request.contextPath}/register">Create
						jTech Account</a>
				</div>

			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>
	</div>

	<jsp:include page="wrapper/footer.jsp" />

	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>