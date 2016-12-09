<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- HEADER START -->
<div class="collapse navbar-collapse navbar-inverse"
	id="bs-example-navbar-collapse-1">

	<div style="height: 52px " class="container-fluid">
		<div class="container-fluid" style="padding-top: 10px;">
				
			<span class="navbar-form navbar-left"><a
				style="color: white;" href="${pageContext.request.contextPath}/console"><span
					class="glyphicon glyphicon-home"></span> HOME</a> </span> <span
				class="navbar-form navbar-left"> <a style="color: white;"
				href="${pageContext.request.contextPath}/console"><span
					class="glyphicon glyphicon-th"></span> PRODUCTS</a>
			</span> <span class="navbar-form navbar-left"> <a
				style="color: white;" href="${pageContext.request.contextPath}/console"><span
					class="glyphicon glyphicon-earphone"></span> CONTACT US</a>
			</span> <span class="navbar-form navbar-right"> <a
				style="color: white;"
				href="${pageContext.request.contextPath}/console"><span
					class="glyphicon glyphicon-phone"></span><b> MY</b> CONSOLE</a>
			</span> <span class="navbar-form navbar-right"> <font color="white">|</font>

			</span> <span class="navbar-form navbar-right"> <a
				style="color: white;"
				href="${pageContext.request.contextPath}/myaccount"><span
					class="glyphicon glyphicon-user"></span><b> MY</b> ACCOUNT</a>
			</span>


			<c:if test="${not empty param.firstname}">

				<span class="navbar-form navbar-right"> <font color="white">|</font></span>

				<span class="navbar-form navbar-right"> <a
					style="color: white;"
					href="${pageContext.request.contextPath}/logout"> <span
						class="glyphicon glyphicon-log-out"></span> Sign <b>out</b></a>

				</span>

				<!-- 					<span class="navbar-form navbar-right"> <font color="white">|</font></span> -->

				<!-- 					<span class="navbar-form navbar-right"> <font color="white">Hello<b> -->
				<%-- 								${param.firstname}</b></font></span> --%>

			</c:if>
		</div>
	</div>
</div>
<!-- HEADER END -->