<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
<title>jTech Console</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/custom/js/bootstrap-multiselect.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/custom/js/bootstrap-clockpicker.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/custom/js/stomp.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/custom/js/ajax-functions.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/custom/js/navigation.js"></script>
	
	
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.1/sockjs.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/custom/js/jquery.growl.js"
	type="text/javascript"></script>
<link
	href="${pageContext.request.contextPath}/resources/custom/css/jquery.growl.css"
	rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/bootstrap-multiselect.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/custom.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/simple-sidebar.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/bootstrap-clockpicker.min.css">
<link href="https://fonts.googleapis.com/css?family=Exo"
	rel="stylesheet">



</head>

<body>

	<input type="hidden" id="userid" name="userid" value="${userid}">
	<input type="hidden" id="username" name="username"
		value="${pageContext.request.userPrincipal.name}">

	<jsp:include page="wrapper/header.jsp">
		<jsp:param name="firstname" value="${firstname}" />
	</jsp:include>
	<br>

	<div class="container-fluid"
		style="padding-right: 0px; padding-left: 0px;">

		<div class="masthead center-block">

			<div class="panel panel-default">
				<div id="product-dropdown" class="dropdown panel-heading">
					<div style="display: inline-block;">
						<label>Select My <b>Product</b></label> <select
							id="product-picker" onchange="showProductControls('0');"
							class="form-control" style="width: 250px;">
							<c:forEach items="${userProducts}" var="ups" varStatus="count">
								<option id="${ups.serialNumber}"
									value="product-relay-control-${count.index}">${ups.name}
									(${ups.serialNumber})</option>
							</c:forEach>
						</select>
					</div>
				</div>

			</div>

			<div id="tabs-container">

				<ul class="nav nav-tabs nav-justified" id="myTabs">
					<li class="active"><a href="#tab0">Controls</a></li>
					<li><a href="#tab1">Settings</a></li>
					<li><a href="#tab2">Users</a></li>
<!-- 					<li><a href="#tab3">Overview</a></li> -->
					<li><a href="#tab4">Registration</a></li>
				</ul>

			</div>


			<section id="tab0" class="tab-content active">

				<div id="product-relay-controls">
					<c:forEach items="${userProducts}" var="ups" varStatus="count">

						<div class="panel panel-default"
							id="product-relay-control-${count.index}" style="display: none;">


							<c:choose>
								<c:when test="${ups.isConnected()}">
									<div id="${ups.serialNumber}-connection-status"
										style="text-align: center;"
										class="container-fluid alert-success">Device is ONLINE</div>
								</c:when>
								<c:otherwise>
									<div id="${ups.serialNumber}-connection-status"
										style="text-align: center;"
										class="container-fluid alert-danger">Device is OFFLINE</div>
								</c:otherwise>
							</c:choose>


							<div class="page-header" style="text-align: center;">
								<h3>Relay Controls</h3>
							</div>

							<div class="container-fluid" style="text-align: center;">
								<label>Select Relays</label>
							</div>
							<div id="product-rel-control-dropdown-${count.index}"
								style="text-align: center;" class="container-fluid">
								<select id="product-relay-control-picker-${count.index}"
									multiple="multiple"
									onchange="showProductRelayControl('product-relay-control-picker-${count.index}','rel-control-${count.index}');">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<c:forEach items="${relaysetting.productControlSettings}"
											var="pcs">
											<c:if test="${(pcs.userId eq userid) && pcs.isAccess()}">
												<option value="relay-control-${count2.index}">${relaysetting.relayName}
													(${relaysetting.moduleId}/${relaysetting.relayId})</option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</select>
							</div>
							<br>

							<div id="rel-control-${count.index}"
								class="container panel panel-default">
								<table class="table table-striped" style="margin-bottom: 10px;">
									<thead>
										<tr>
											<th>Module ID</th>
											<th>Relay ID</th>
											<th>Relay Name</th>
											<th>Status</th>
											<th>Control</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach
											items="${ups.productSettings.iterator().next().relaySettings}"
											var="relaysetting" varStatus="count">
											<c:forEach items="${relaysetting.productControlSettings}"
												var="pcs">
												<c:if test="${(pcs.userId eq userid) && pcs.isAccess()}">
													<tr id="relay-control-${count.index}"
														style="display: none;">
														<td>${relaysetting.moduleId}</td>
														<td>${relaysetting.relayId}</td>
														<td>${relaysetting.relayName}</td>
														<td><span
															id="relaystatus-${ups.serialNumber}-${relaysetting.moduleId}-${relaysetting.relayId}"
															class="label label-danger">OFF</span></td>
														<td>
															<div id="relay-progressbar-${ups.serialNumber}-${relaysetting.moduleId}-${relaysetting.relayId}" class="loader" style="display:none;" id="timex"></div>
															<button id="relay-switch-${ups.serialNumber}-${relaysetting.moduleId}-${relaysetting.relayId}" style="display:block;" type="button" class="btn-primary btn"
																onClick="switchRelay('${ups.serialNumber}','${relaysetting.moduleId}', '${relaysetting.relayId}');">SWITCH</button>
														</td>
													</tr>
												</c:if>
											</c:forEach>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>


			<section id="tab1" class="tab-content hide">

				<div id="product-relay-settings">
					<!-- Default panel contents -->
					<c:forEach items="${userProducts}" var="ups" varStatus="count">

						<div class="panel panel-default"
							id="product-relay-control-${count.index}" style="display: none;">

							<div class="page-header" style="text-align: center;">
								<h3>Relay Settings</h3>
							</div>

							<div class="container-fluid" style="text-align: center;">
								<label>Select Relays</label>
							</div>
							<div id="product-relay-setting-dropdown-${count.index}"
								style="text-align: center;" class="container-fluid">
								<select id="product-relay-setting-picker-${count.index}"
									name="product-relay-setting-picker" multiple="multiple"
									onchange="showRelaySetting('product-relay-setting-picker-${count.index}','rel-setting-${count.index}');">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<c:forEach items="${relaysetting.productControlSettings}"
											var="pcs">
											<c:if test="${(pcs.userId eq userid) && pcs.isAccess()}">
												<option value="relay-setting-${count2.index}">${relaysetting.relayName}
													(${relaysetting.moduleId}/${relaysetting.relayId})</option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</select>
							</div>

							<br>
							<div class="table-responsive container panel panel-default">
								<div id="rel-setting-${count.index}">
									<div class="thumbnail">
										<table class="table table-striped" id="relaysettingtable">
											<thead>
												<tr>
													<th>Module ID</th>
													<th>Relay ID</th>
													<th>Relay Name</th>
													<th>Delay (sec)</th>
													<th>Impulse Mode</th>
													<th>Timers</th>
												</tr>
											</thead>
											<tbody>

												<c:forEach
													items="${ups.productSettings.iterator().next().relaySettings}"
													var="relaysetting" varStatus="count2">
													<c:forEach items="${relaysetting.productControlSettings}"
														var="pcs">
														<c:if test="${(pcs.userId eq userid) && pcs.isAccess()}">
															<tr class="${ups.serialNumber}-relay-setting"
																id="relay-setting-${count2.index}"
																style="display: none;">
																<td id="${ups.serialNumber}-moduleid-${count2.index}">${relaysetting.moduleId}</td>
																<td id="${ups.serialNumber}-relayid-${count2.index}">${relaysetting.relayId}</td>
																<td><input
																	id="${ups.serialNumber}-relayname-${count2.index}"
																	type="text" class="input-sm" style="width: 150px"
																	placeholder="${relaysetting.relayName}"></td>
																<td><input
																	id="${ups.serialNumber}-delay-${count2.index}"
																	type="number" min="0" class="input-sm"
																	style="width: 100px"
																	placeholder="${relaysetting.delay}"></td>
																<td><c:choose>
																		<c:when test="${relaysetting.impulseMode}">
																			<input
																				id="${ups.serialNumber}-impulse-${count2.index}"
																				checked type="checkbox" value="">
																		</c:when>
																		<c:otherwise>
																			<input
																				id="${ups.serialNumber}-impulse-${count2.index}"
																				type="checkbox" value="">
																		</c:otherwise>
																	</c:choose></td>
																<td><button type="button" class="btn-primary btn"
																		onclick="showHideTags('relay-timers-${count.index}','div','relay-tmr-${count2.index}','relay')">EDIT</button></td>
															</tr>
														</c:if>
													</c:forEach>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
								<div id="relay-timers-${count.index}">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<div class="${ups.serialNumber}-relay-timer thumbnail"
											id="relay-tmr-${count2.index}" style="display: none;">
											<table class="table">
												<thead>
													<tr>
														<th>Module ID</th>
														<th>Relay ID</th>
														<th>Function</th>
														<th>Days of Week</th>
														<th>Timer</th>
														<th>Enabled?</th>
													</tr>
												</thead>
												<tbody>
													<tr style="border-style: hidden;">
														<td><label>${relaysetting.moduleId}</label></td>
														<td><label>${relaysetting.relayId}</label></td>
														<td><label>Start Timer</label></td>
														<td><div class="dropup">
																<select
																	id="weekday-picker-start-${ups.serialNumber}-${count2.index}"
																	multiple="multiple">
																	<c:set var="weekday"
																		value="${relaysetting.startWeekDays}" />

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'mon')}">
																			<option selected value="mon">Monday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="mon">Monday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'tue')}">
																			<option selected value="tue">Tuesday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="tue">Tuesday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'wed')}">
																			<option selected value="wed">Wednesday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="wed">Wednesday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'thu')}">
																			<option selected value="thu">Thursday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="thu">Thursday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'fri')}">
																			<option selected value="fri">Friday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="fri">Friday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'sat')}">
																			<option selected value="sat">Saturday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="sat">Saturday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'sun')}">
																			<option selected value="sun">Sunday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="sun">Sunday</option>
																		</c:otherwise>
																	</c:choose>

																</select>
															</div></td>
														<td>
															<div class="input-group clockpicker">
																<input
																	id="start-timer-${ups.serialNumber}-${count2.index}"
																	type="text" class="form-control"
																	value="${relaysetting.startTimer}"> <span
																	class="input-group-addon"> <span
																	class="glyphicon glyphicon-time"></span>
																</span>
															</div>
														</td>
														<td><c:choose>
																<c:when test="${relaysetting.timerEnabled}">
																	<input
																		id="timer-enabled-${ups.serialNumber}-${count2.index}"
																		checked type="checkbox" value="">
																</c:when>
																<c:otherwise>
																	<input
																		id="timer-enabled-${ups.serialNumber}-${count2.index}"
																		type="checkbox" value="">
																</c:otherwise>
															</c:choose></td>
													</tr>

													<tr>
														<td></td>
														<td></td>
														<td><label>End Timer</label></td>
														<td><div class="dropup">
																<select
																	id="weekday-picker-end-${ups.serialNumber}-${count2.index}"
																	multiple="multiple">

																	<c:set var="weekday"
																		value="${relaysetting.endWeekDays}" />

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'MO')}">
																			<option selected value="mon">Monday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="mon">Monday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'TU')}">
																			<option selected value="tue">Tuesday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="tue">Tuesday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'WE')}">
																			<option selected value="wed">Wednesday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="wed">Wednesday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'TH')}">
																			<option selected value="thu">Thursday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="thu">Thursday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'FR')}">
																			<option selected value="fri">Friday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="fri">Friday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'SA')}">
																			<option selected value="sat">Saturday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="sat">Saturday</option>
																		</c:otherwise>
																	</c:choose>

																	<c:choose>
																		<c:when test="${fn:containsIgnoreCase(weekday,'SU')}">
																			<option selected value="sun">Sunday</option>
																		</c:when>
																		<c:otherwise>
																			<option value="sun">Sunday</option>
																		</c:otherwise>
																	</c:choose>
																</select>
															</div></td>
														<td>
															<div class="input-group clockpicker">
																<input
																	id="end-timer-${ups.serialNumber}-${count2.index}"
																	type="text" class="form-control"
																	value="${relaysetting.endTimer}"> <span
																	class="input-group-addon"> <span
																	class="glyphicon glyphicon-time"></span>
																</span>
															</div>
														</td>
														<td></td>
													</tr>

												</tbody>
											</table>
										</div>
									</c:forEach>
								</div>
								<div class="container-fluid" style="float: right;">

									<c:choose>
										<c:when test="${privilige eq 'ADMIN'}">
											<button
												onClick="updateProductSettings('${ups.serialNumber}','${count.index}');"
												type="submit" class="btn-primary btn pull-right">SAVE
												SETTING</button>
										</c:when>
										<c:otherwise>
											<button disabled type="submit"
												class="btn-primary btn pull-right">SAVE SETTING</button>
										</c:otherwise>
									</c:choose>
								</div>
							</div>


						</div>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</c:forEach>
				</div>
			</section>

			<section id="tab2" class="tab-content hide">
				<div id="product-users">
					<c:forEach items="${userProducts}" var="ups" varStatus="count">
						<div class="panel panel-default"
							id="product-relay-control-${count.index}"
							style="border-style: hidden;">
							<div class="panel panel-default">
								<!-- Default panel contents -->
								<div class="dropdown panel-heading">
									<div class="container-fluid">
										<div class="col-lg-6">
											<h4>Add New User</h4>
											<div class="input-group">



												<input id="add-product-user-${count.index}" type="text"
													class="form-control" placeholder="Email address"> <span
													class="input-group-btn "> <c:choose>
														<c:when test="${privilige eq 'ADMIN'}">
															<button class="btn btn-primary" type="button"
																onClick="addProductUser('${ups.serialNumber}','${count.index}');">ADD
																USER</button>
														</c:when>
														<c:otherwise>
															<button disabled class="btn btn-primary" type="button">ADD
																USER</button>
														</c:otherwise>
													</c:choose>


												</span>
											</div>
											<div class="container-fluid alert-danger"
												id="user-error-message-${count.index}"></div>
											<div class="container-fluid alert-success"
												id="user-success-message-${count.index}"></div>
											<!-- /input-group -->
										</div>
										<!-- /.col-lg-6 -->
									</div>

								</div>
							</div>



							<div class="container-fluid panel panel-default">

								<!-- /input-group -->
								<div class="page-header" style="text-align: center;">
									<h3>Current Users</h3>
								</div>

								<div>
									<table class="table table-striped">
										<thead>
											<tr>
												<td><h3>Privilige</h3></td>
												<td><h3>Email Address</h3></td>
												<td><h3>Relay Access</h3></td>
												<td><h3>Call Access</h3></td>
												<td><h3>Action</h3></td>
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${ups.productUsers}" var="productUsers"
												varStatus="count2">

												<tr>
													<td><c:choose>
															<c:when
																test="${productUsers.userName eq pageContext.request.userPrincipal.name || privilige eq 'USER'}">
																<label>${productUsers.privilige}</label>
															</c:when>
															<c:otherwise>
																<label><select
																	id="product-priv-picker-${count.index}-${count2.index}"
																	class="form-control" style="width:100px;">
																	<c:if test="${productUsers.privilige eq 'ADMIN'}">
																		<option value="priv-${count2.index}-${count3.index}"
																			selected>ADMIN</option>
																		<option value="priv-${count2.index}-${count3.index}">USER</option>
																	</c:if>
																	<c:if test="${productUsers.privilige eq 'USER'}">
																		<option value="priv-${count2.index}-${count3.index}">ADMIN</option>
																		<option selected
																			value="priv-${count2.index}-${count3.index}">USER</option>
																	</c:if>
																</select></label>
															</c:otherwise>
														</c:choose></td>


													<td><label>${productUsers.userName}</label></td>
													<td><select
														id="product-relay-priv-picker-${count.index}-${count2.index}"
														multiple="multiple">
															<c:forEach
																items="${ups.productSettings.iterator().next().relaySettings}"
																var="relaysetting" varStatus="count3">

																<c:forEach
																	items="${relaysetting.productControlSettings}"
																	var="productControlSetting" varStatus="count4">

																	<c:if
																		test="${(productControlSetting.userId eq productUsers.userId) && productControlSetting.access}">
																		<option value="r-${count2.index}-${count3.index}"
																			selected>${relaysetting.relayName}</option>
																	</c:if>
																	<c:if
																		test="${(productControlSetting.userId eq productUsers.userId) && (not productControlSetting.access)}">
																		<option value="r-${count2.index}-${count3.index}">${relaysetting.relayName}</option>
																	</c:if>

																</c:forEach>
															</c:forEach>
													</select></td>
													<td><select
														id="product-relay-call-picker-${count.index}-${count2.index}"
														multiple="multiple">
															<c:forEach
																items="${ups.productSettings.iterator().next().relaySettings}"
																var="relaysetting" varStatus="count3">
																<c:forEach
																	items="${relaysetting.productControlSettings}"
																	var="productControlSetting" varStatus="count4">

																	<c:if
																		test="${(productControlSetting.userId eq productUsers.userId) && productControlSetting.callAccess}">
																		<option value="c-${count2.index}-${count3.index}"
																			selected>${relaysetting.relayName}</option>
																	</c:if>
																	<c:if
																		test="${(productControlSetting.userId eq productUsers.userId) && (not productControlSetting.callAccess)}">
																		<option value="c-${count2.index}-${count3.index}">${relaysetting.relayName}</option>
																	</c:if>

																</c:forEach>
															</c:forEach>
													</select></td>
													<td>
														<div class="btn-group">

															<c:choose>
																<c:when test="${privilige eq 'ADMIN'}">
																	<button type="button" class="btn btn-primary"
																		onClick="updateProductUser('${productUsers.userName}','${ups.serialNumber}','${count.index}-${count2.index}');">SAVE</button>
																	<c:if test="${not (productUsers.userName eq pageContext.request.userPrincipal.name)}">
																	<button type="button" class="btn btn-danger"
																		onClick="removeProductUser('${productUsers.userName}','${ups.serialNumber}','${count.index}-${count2.index}');">REMOVE</button>
																	</c:if>
																</c:when>
																<c:otherwise>
																	<button type="button" disabled class="btn btn-primary">SAVE</button>
																	<button type="button" disabled class="btn btn-danger">REMOVE</button>
																</c:otherwise>
															</c:choose>

														</div>
													</td>
												</tr>

											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>


			<section id="tab4" class="tab-content hide">
				<div class="panel panel-default">
					<!-- Default panel contents -->

					<div class="dropdown panel-heading">
						<div class="container-fluid">
							<div class="col-lg-6">
								<h4>Register My Product</h4>
								<div class="input-group">
									<input id="registrationSerialNumber" type="text"
										class="form-control" placeholder="Serial Number" /> <span
										class="input-group-btn">
										<button class="btn btn-primary" type="button"
											onClick="registerProduct()">REGISTER</button>
									</span>
								</div>
								<div class="container-fluid alert-danger"
									id="register-error-message"></div>
								<div class="container-fluid alert-success"
									id="register-success-message"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="panel panel-default">

					<br>
					<div class="page-header" style="text-align: center;">
						<h3>My Registered Products</h3>
					</div>
					<div class="container panel panel-default">
						<table class="table table-striped">
							<thead>
								<tr>
									<th><h4>Serial Number</h4></th>
									<th><h4>Product Name</h4></th>
									<th><h4>Date of Registration</h4></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${userProducts}" var="userProduct"
									varStatus="count2">
									<tr>
										<td><h4>${userProduct.serialNumber}</h4></td>
										<td>
											<div class="input-group">
												<input id="registred-product-field-${count2.index}"
													type="text" class="form-control"
													placeholder="${userProduct.name}"> <span
													class="input-group-btn"> <c:choose>
														<c:when test="${privilige eq 'ADMIN'}">
															<button class="btn btn-primary" type="button"
																onClick="saveProductName('${userProduct.serialNumber}','${count2.index}');">SAVE</button>
														</c:when>
														<c:otherwise>
															<button disabled class="btn btn-primary" type="button">SAVE</button>
														</c:otherwise>
													</c:choose>

												</span>
											</div>
										</td>
										<td><h4>${userProduct.creationDate}</h4></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</section>


		</div>

		<jsp:include page="wrapper/footer.jsp" />

	</div>



</body>
</html>