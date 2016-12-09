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
<title>jTech MyAccount</title>
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
		<jsp:param name="firstname" value="${firstName}" />
	</jsp:include>


	<div class="container">
		<form name='myaccount' action="<c:url value='/myaccount' />"
			method='POST'>
			<div class="col-md-16 col-md-offset-2">
				<!-- page-header adds space aroundtext and enlarges it. It also adds an underline at the end -->

				<div class="page-header" style="text-align: center;">
					<h1>My Account</h1>
				</div>

				<table class="table table-condensed">
					<thead>
						<tr>
							<th colspan="2" style="text-align: center; "><h4>
									<b>Login</b> Information
								</h4></th>
						</tr>
						<tr>
							<c:if test="${not empty passwordsuccess}">
								<th colspan="2" style="text-align: center;"><br>
									<div class="text-center alert-danger">
										<h4>${passwordsuccess}</h4>
									</div></th>
							</c:if>

							<c:if test="${not empty passworderror}">
								<th colspan="2" style="text-align: center;"><br>
									<div class="text-center alert-danger">
										<h4>${passworderror}</h4>
									</div></th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><h4>Old Password</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="oldPassword" type="password"></td>
						</tr>

						<tr>
							<td><h4>New Password</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="newPassword" type="password"></td>
						</tr>

						<tr>
							<td><h4>Confirm New Password</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="confirmPassword" type="password"></td>
						</tr>

					</tbody>
				</table>




				<table class="table">
					<thead>
						<tr>
							<th colspan="2" style="text-align: center;"><h4>
									<b>My</b> Information
								</h4></th>
						</tr>

						<tr>
							<c:if test="${not empty success}">
								<th colspan="2" style="text-align: center;"><br>
									<div class="text-center alert-success">
										<h4>${success}</h4>
									</div></th>
							</c:if>

							<c:if test="${not empty error}">
								<th colspan="2" style="text-align: center;"><br>
									<div class="text-center alert-danger">
										<h4>${error}</h4>
									</div></th>
							</c:if>
						</tr>
					</thead>
					<tbody>

						<tr>
							<td><h4>First Name</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="firstName" value="${firstName}" type="text"></td>
						</tr>

						<tr>
							<td><h4>Last Name</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="lastName" value="${lastName}" type="text"></td>
						</tr>
						<tr>
							<td><h4>Email Address</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="email" value="${email}" type="text"></td>
						</tr>

						<tr>
							<td><h4>Phone Number</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="phone" value="${phone}" type="text"></td>
						</tr>

						<tr>
							<td><h4>Address</h4></td>
							<td><input class="form-control input-lg" id="inputlg"
								name="address" value="${address}" type="text"></td>

						</tr>

						<tr>
							<td><h4>City</h4></td>

							<td><input class="form-control input-lg" id="inputlg"
								name="city" value="${city}" type="text"></td>

						</tr>
				</table>

				<br>

				<div class="text-center" role="group" aria-label="...">
					<button type="submit" class="btn btn-primary btn-lg">SAVE
						CHANGES</button>
				</div>


				<c:if test="${param.logout ne null}">
					<br>
					<div class="text-center alert-success">
						<h4>You have been logged out.</h4>
					</div>
				</c:if>

				<br>


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