<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<title>jTech Contact Us</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>


<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/custom.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/simple-sidebar.css">

<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">
</head>
<body>

	<jsp:include page="wrapper/header.jsp">
		<jsp:param name="firstname" value="${firstname}" />
	</jsp:include>

	<div class="container-fluid"
		style="padding-right: 0px; padding-left: 0px;">

		<div class="masthead center-block">

			<div class="panel panel-default">

				<h2 style="text-align: center;">
					<label>Contact <b>Us</b></label>
				</h2>
			</div>
			<div class="panel panel-default">
				<div id="product-dropdown" class="dropdown panel-heading">

					<form:form action="contact" commandName="contactForm">
						<div class="form-group">



							<table class="table table-condensed">

								<tbody>
									<tr>
										<td><h4>Name</h4></td>
										<td><form:input class="form-control input-lg"
												style="width:300px;" path="name" size="30" type="text" /></td>
										<td><form:errors path="name" style="text-align: left; position:relative; float:left" cssClass="alert-danger" /></td>
									</tr>


									<tr>
										<td><h4>Email</h4></td>
										<td><form:input class="form-control input-lg"
												style="width:300px;" path="email" size="30" type="text" /></td>
										<td><form:errors path="email" style="text-align: left; position:relative; float:left"  cssClass="alert-danger" /></td>
									</tr>


									<tr>
										<td><h4>Message</h4></td>
										<td><form:textarea class="form-control input-lg"
												style="width:400px; height:200px;" path="message" size="30" type="text" /></td>
										<td><form:errors path="message" style="text-align: left; position:relative; float:left"  cssClass="alert-danger" /></td>
									</tr>


									<tr>
										<td><h5>
												Or send us a mail at <a href="#">info@jtech.com</a>
											</h5></td>
										<td></td>
									</tr>

								</tbody>
							</table>
							<div class="text-center" role="group" aria-label="...">
								<button type="submit" class="btn btn-primary btn-lg">SEND
									MESSAGE</button>
							</div>
						</div>
					</form:form>

				</div>

			</div>
		</div>
	</div>


	<jsp:include page="wrapper/footer.jsp" />

</body>
</html>