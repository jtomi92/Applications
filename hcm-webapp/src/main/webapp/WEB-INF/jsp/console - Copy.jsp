<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">

<head>
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

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/bootstrap-multiselect.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/custom.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/simple-sidebar.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/custom/css/bootstrap-clockpicker.min.css">


<script>
	$(document).ready(
			function() {
				$('.nav-tabs > li > a').click(
						function(event) {
							event.preventDefault();//stop browser to take action for clicked anchor

							//get displaying tab content jQuery selector
							var active_tab_selector = $(
									'.nav-tabs > li.active > a').attr('href');

							//find actived navigation and remove 'active' css
							var actived_nav = $('.nav-tabs > li.active');
							actived_nav.removeClass('active');

							//add 'active' css into clicked navigation
							$(this).parents('li').addClass('active');

							//hide displaying tab content
							$(active_tab_selector).removeClass('active');
							$(active_tab_selector).addClass('hide');

							//show target tab content
							var target_tab_selector = $(this).attr('href');
							$(target_tab_selector).removeClass('hide');
							$(target_tab_selector).addClass('active');
						});
			});

	function showHideTags(container, tag, toShow, toHide) {
		var elements = document.getElementById(container).getElementsByTagName(
				tag);
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			console.log('ID ' + id);
			if (id !== null) {
				if (id === toShow) {
					elements[i].style.display = "block";
					console.log('SHOW ' + id);
				} else {
					if (id.indexOf(toHide) !== -1) {
						elements[i].style.display = "none";
						console.log('HIDE ' + id);
					}
				}
			}
		}

	}

	function showRelaySetting(picker, section) {

		var selectedValues = $('#' + picker).val();
		console.log("PICKER=" + picker);
		console.log("SECTION=" + section);
		console.log("VALUE=" + selectedValues);

		var elements = document.getElementById(section).getElementsByTagName(
				'tr');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			console.log('ID ' + id);
			if (id !== null) {
				if (id.indexOf('relay-setting') !== -1) {
					elements[i].style.display = "none";
					console.log('HIDE ' + id);
				}
			}
		}

		$.each(selectedValues, function(index, value) {
			console.log(index + ' ' + value);

			for (var i = 0; i < elements.length; i++) {
				var id = elements[i].getAttribute("id");
				console.log('ID ' + id);
				if (id !== null) {

					if (id === value) {
						elements[i].style.display = "table-row";
						console.log('SHOW ' + id);
					}

				}
			}
		});
	}

	function showProductRelayControl(picker, section) {
		console.log("PICKER= " + picker);
		var selectedValues = $('#' + picker).val();
		console.log("VALUES= " + selectedValues);

		var elements = document.getElementById(section).getElementsByTagName(
				'tr');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			console.log('ID (hide) ' + id);
			if (id !== null) {
				if (id.indexOf('relay-control') !== -1) {
					elements[i].style.display = "none";
					console.log('HIDE ' + id);
				}
			}
		}

		$.each(selectedValues, function(index, value) {
			console.log("ASD" + index + ' ' + value);

			for (var i = 0; i < elements.length; i++) {
				var id = elements[i].getAttribute("id");
				console.log('ID (show)' + id);
				if (id !== null) {

					if (id === value) {
						elements[i].style.display = "table-row";
						console.log('SHOW ' + id);
					}

				}
			}
		});
	}

	$(document).ready(function loadProductDetails() {
		var selectedValue = $('#product-picker').val();
		showProductControls();
	});

	function showProductControls() {
		var selectedValue = $('#product-picker').val();
		var serialNumber = $('#product-picker').children(":selected").attr("id");

		localStorage.setItem("currentSerialNumber", serialNumber);

		var elements = document.getElementById('product-relay-controls')
				.getElementsByTagName('div');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('product-relay-control-') !== -1) {
					elements[i].style.display = "none";
					//console.log('HIDE ' + id);
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			//console.log('ID ' + id);
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
					//console.log('SHOW ' + id);
				}

			}
		}

		var elements = document.getElementById('product-relay-settings')
				.getElementsByTagName('div');

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('product-relay-control-') !== -1) {
					elements[i].style.display = "none";
					//console.log('HIDE ' + id);
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			//console.log('ID ' + id);
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
					//console.log('SHOW ' + id);
				}

			}
		}

		var elements = document.getElementById('product-users')
				.getElementsByTagName('div');

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('product-relay-control-') !== -1) {
					elements[i].style.display = "none";
					console.log('HIDE ' + id);
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			console.log('ID ' + id);
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
					console.log('SHOW ' + id);
				}

			}
		}

	}

	function registerProduct() {
		var data = {};

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
 
		data['userId'] = ${userid}; 
		data['serialNumber'] = $("#registrationSerialNumber").val();

		var result = '';
		console.log(data);

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					async : false,
					url : '${pageContext.request.contextPath}/console/register',
					data : JSON.stringify(data),
					dataType : 'json',
					beforeSend : function(xhr) {
						// here it is
						xhr.setRequestHeader(header, token);
					},
					success : function(res, ioArgs) {
						result = res;

						console.log(res);

						if (res == '0') {
							document.getElementById("register-error-message").innerHTML = "Serial Number does not exist.";
							document.getElementById("register-error-message").style.visibility = "visible";			
							localStorage.setItem("notificationModalTitle", "Product Registration");
							localStorage.setItem("notificationModalContent", "Validation error");
						} else {
							document.getElementById("register-success-message").innerHTML = "You have successfully registered your product";
							document.getElementById("register-success-message").style.visibility = "visible";
							localStorage.setItem("notificationModalTitle", "Product Registration");
							localStorage.setItem("notificationModalContent", "You have successfully registered your product!");
							localStorage.setItem("showNotificationModal", "true");
							location.reload(); 
						}
						
					},
					error : function(e) {
						console.log("error");
						console.log(e);
					}

				});

	}

	$(document).ready(function() {
		if (localStorage.getItem("showNotificationModal") == "true") {
			
			document.getElementById("notificationModalTitle").innerHTML = localStorage.getItem("notificationModalTitle");
			document.getElementById("notificationModalContent").innerHTML = localStorage.getItem("notificationModalContent");
			
			$('#notificationModal').modal('show');
			localStorage.setItem("showNotificationModal", "false");
		}
	});
	
	
	
	function saveProductName(serialNumber, index) {
		var data = {};
		var productName = document.getElementById("registred-product-field-" + index).value;
		console.log("SERIAL=" + serialNumber + " PRODUCTNAME=" + productName );

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
 
		data['userId'] = ${userid};
		data['serialNumber'] = serialNumber;
		data['productName'] = productName;

		var result = '';
		console.log(data);

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					async : false,
					url : '${pageContext.request.contextPath}/console/update',
					data : JSON.stringify(data),
					dataType : 'json',
					beforeSend : function(xhr) {
						// here it is
						xhr.setRequestHeader(header, token);
					},
					success : function(res, ioArgs) {
						result = res;

						console.log(res);
						
						localStorage.setItem("notificationModalTitle", "Product Update");
												
						if (res == '0') {						
							localStorage.setItem("notificationModalContent", "Validation Error");
							localStorage.setItem("showNotificationModal", "true");
							location.reload(); 
						} else {
							localStorage.setItem("notificationModalContent", "Product name successfully updated!");
							localStorage.setItem("showNotificationModal", "true");
							location.reload(); 
						}
						
					},
					error : function(e) {
						console.log("error");
						console.log(e);
					}

				});

	}
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$('[id^="weekday-picker-"]').multiselect();
		$('[id^="product-relay-control-picker-"]').multiselect();
		$('[id^="product-relay-setting-picker-"]').multiselect();
		$('[id^="product-relay-priv-picker"]').multiselect();
		$('[id^="product-relay-user-picker"]').multiselect();
		$('[id^="product-relay-call-picker"]').multiselect();

		$('.clockpicker').clockpicker({
			placement : 'top',
			align : 'left',
			donetext : 'Done'
		});
	});
</script>

<script type="text/javascript">
	$(document).ready(function() {

		$('#product-dropdown').on('hidden.bs.dropdown', function() {
			console.log("Dropdown hidden..");
		});
		$('#tab1-dropdown-1').on('hidden.bs.dropdown', function() {
			console.log("Dropdown hidden..");
		});
		$('#tab1-dropdown-2').on('hidden.bs.dropdown', function() {
			console.log("Dropdown hidden..");
		});
		$('#tab0-dropdown-1').on('hidden.bs.dropdown', function() {
			console.log("Dropdown hidden..");
		});
		$('#tab0-dropdown-2').on('hidden.bs.dropdown', function() {
			console.log("Dropdown hidden..");
		});
	});
</script>

<script type="text/javascript">
	$("#menu-toggle").click(function(e) {
		e.preventDefault();
		$("#wrapper").toggleClass("toggled");
	});
</script>

</head>

<body>

	<input type="hidden" name="userid" value="${userid}">

	<!-- Modal -->
	<div id="notificationModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 id="notificationModalTitle" class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<p>
						<label id="notificationModalContent" class="alert-success" style="text-align: center;"></label>
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>
 

	<div class="collapse navbar-collapse navbar-inverse"
		id="bs-example-navbar-collapse-1">
		<ul class="nav navbar-nav">
			<li class="active"><a href="#">Home <span class="sr-only">(current)</span></a></li>
			<li><a href="#">About</a></li>
			<li><a href="#">Contact Us</a></li>
		</ul>
		<!-- navbar-left will move the search to the left -->
		<form class="navbar-form navbar-right" role="search">
			<button type="submit" class="btn btn-warning">My Console</button>
		</form>
		<div class="navbar-form navbar-right">
			<form action="/logout" method="post">
				<input type="submit" class="btn btn-warning" class="button red big"
					value="Sign Out" /> <input type="hidden"
					name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>
		</div>
		<div class="navbar-form navbar-right">
			<h4>
				<font color="white">Hello <b><c:out value="${firstname}"></c:out></b></font>
			</h4>
		</div>
	</div>
	<br>

	<div class="container-fluid"
		style="padding-right: 0px; padding-left: 0px;">

		<div class="masthead center-block">

			<div class="panel panel-default">
				<div id="product-dropdown" class="dropdown panel-heading">
					<label>Select My <b>Product</b></label> <select
						id="product-picker" onchange="showProductControls();"
						class="form-control" style="width: 250px;">
						<c:forEach items="${userProducts}" var="ups" varStatus="count">
							<option id="${ups.serialNumber}" value="product-relay-control-${count.index}">${ups.name}
								(${ups.serialNumber})</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div id="tabs-container">

				<ul class="nav nav-tabs nav-justified" id="myTabs">
					<li class="active"><a href="#tab0">Control</a></li>
					<li><a href="#tab1">Settings</a></li>
					<li><a href="#tab2">Users</a></li>
					<li><a href="#tab3">Overview</a></li>
					<li><a href="#tab4">Registration</a></li>
				</ul>

			</div>


			<section id="tab0" class="tab-content active">

				<div id="product-relay-controls">
					<c:forEach items="${userProducts}" var="ups" varStatus="count">

						<div class="panel panel-default"
							id="product-relay-control-${count.index}" style="display: none;">

							<br>
							<div class="container-fluid">
								<label>Select Relays</label>
							</div>
							<div id="tab0-dropdown-2" class="container-fluid">
								<select id="product-relay-control-picker-${count.index}"
									multiple="multiple"
									onchange="showProductRelayControl('product-relay-control-picker-${count.index}','rel-control-${count.index}');">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<option value="relay-control-${count2.index}">${relaysetting.relayName}</option>
									</c:forEach>
								</select>
							</div>
							<br>

							<div id="rel-control-${count.index}"
								class="container panel panel-default">
								<table class="table">
									<thead>
										<tr>
											<th>Relay Number</th>
											<th>Relay Name</th>
											<th>Status</th>
											<th>Control</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach
											items="${ups.productSettings.iterator().next().relaySettings}"
											var="relaysetting" varStatus="count">

											<tr id="relay-control-${count.index}" style="display: none;">
												<td>${relaysetting.relayId}</td>
												<td>${relaysetting.relayName}</td>
												<td><span class="label label-danger">OFF</span></td>
												<td><button type="submit" class="btn-primary btn">SWITCH</button></td>
											</tr>
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

							<div id="tab1-dropdown-2" class="container-fluid">
								<label>Select Relays</label> <select
									id="product-relay-setting-picker-${count.index}"
									multiple="multiple"
									onchange="showRelaySetting('product-relay-setting-picker-${count.index}','rel-setting-${count.index}');">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<option value="relay-setting-${count2.index}">${relaysetting.relayName}</option>
									</c:forEach>
								</select>
							</div>

							<br>
							<div class="row">
								<div id="rel-setting-${count.index}" class="col-lg-6 col-md-4">
									<div class="thumbnail">
										<table class="table" id="relaysettingtable">
											<thead>
												<tr>
													<th>Relay Number</th>
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
													<tr id="relay-setting-${count2.index}"
														style="display: none;">
														<td>${relaysetting.relayId}</td>
														<td><input type="text" class="input-sm"
															style="width: 150px"
															placeholder="${relaysetting.relayName}"></td>
														<td><input type="number" min="0" class="input-sm"
															style="width: 100px" placeholder="${relaysetting.delay}"></td>
														<td><input type="checkbox" value="1"></td>
														<td><button type="button" class="btn-primary btn"
																onclick="showHideTags('relay-timers-${count.index}','div','relay-${count2.index}','relay')">EDIT</button></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
								<div id="relay-timers-${count.index}" class="col-lg-6 col-md-4">
									<c:forEach
										items="${ups.productSettings.iterator().next().relaySettings}"
										var="relaysetting" varStatus="count2">
										<div id="relay-${count2.index}" class="thumbnail"
											style="display: none;">
											<table class="table">
												<thead>
													<tr>
														<th>Relay</th>
														<th>Function</th>
														<th>Days of Week</th>
														<th>Timer</th>
														<th>Enabled?</th>
													</tr>
												</thead>
												<tbody>
													<tr style="border-style: hidden;">
														<td><label>${relaysetting.relayId}</label></td>
														<td><label>Start Timer</label></td>
														<td><select id="weekday-picker-start-${count2.index}"
															multiple="multiple">
																<c:set var="weekday" value="${relaysetting.weekDays}" />

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

														</select></td>
														<td>
															<div class="input-group clockpicker">
																<input type="text" class="form-control"
																	value="${relaysetting.startTimer}"> <span
																	class="input-group-addon"> <span
																	class="glyphicon glyphicon-time"></span>
																</span>
															</div>
														</td>
														<td><c:choose>
																<c:when test="${relaysetting.timerEnabled}">
																	<input checked type="checkbox" value="">
																</c:when>
																<c:otherwise>
																	<input type="checkbox" value="">
																</c:otherwise>
															</c:choose></td>
													</tr>

													<tr>
														<td></td>
														<td><label>End Timer</label></td>
														<td><select id="weekday-picker-end-${count2.index}"
															multiple="multiple">

																<c:set var="weekday" value="${relaysetting.weekDays}" />

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
														</select></td>
														<td>
															<div class="input-group clockpicker">
																<input type="text" class="form-control"
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

							</div>
							<div class="row">
								<div class="col-lg-6 col-md-4"></div>
								<div class="col-lg-6 col-md-4">
									<button type="submit" class="btn-primary btn pull-right">SAVE
										SETTING</button>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</section>

			<section id="tab2" class="tab-content hide">
				<div id="product-users">
					<c:forEach items="${userProducts}" var="ups" varStatus="count">
						<div class="panel panel-default"
							id="product-relay-control-${count.index}"
							style="border-style: hidden;">
							<div class="container-fluid">
								<h3>Add New User</h3>


								<div class="input-group">
									<input type="text" class="form-control" style="width: 400px"
										placeholder="Email address"> <span
										class="input-group-btn pull-left">
										<button class="btn btn-primary" type="button">ADD
											USER</button>
									</span>
								</div>
								<!-- /input-group -->
								<div class="page-header" style="text-align: center;">
									<h3>Current Users</h3>
								</div>
							</div>
							<div>
								<table class="table">
									<thead>
										<tr>
											<td><h3>Email Address</h3></td>
											<td><h3>Relay Access</h3></td>
											<td><h3>On Call Relay Access</h3></td>
											<td><h3>Action</h3></td>
										</tr>
									</thead>
									<tbody>

										<c:forEach items="${ups.productUsers}" var="productUsers"
											varStatus="count2">

											<tr>
												<td><label>${productUsers.userName}</label></td>
												<td><select
													id="product-relay-priv-picker-${count.index}-${count2.index}"
													multiple="multiple">
														<c:forEach
															items="${ups.productSettings.iterator().next().relaySettings}"
															var="relaysetting" varStatus="count3">

															<c:forEach items="${relaysetting.productControlSettings}"
																var="productControlSetting" varStatus="count4">

																<c:if
																	test="${(productControlSetting.userId eq productUsers.userId) && productControlSetting.access}">
																	<option value="r-${count4.index}-${count3.index}"
																		selected>${relaysetting.relayName}</option>
																</c:if>
																<c:if
																	test="${(productControlSetting.userId eq productUsers.userId) && (not productControlSetting.access)}">
																	<option value="r-${count4.index}-${count3.index}">${relaysetting.relayName}</option>
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
															<c:forEach items="${relaysetting.productControlSettings}"
																var="productControlSetting" varStatus="count4">

																<c:if
																	test="${(productControlSetting.userId eq productUsers.userId) && productControlSetting.callAccess}">
																	<option value="c-${count4.index}-${count3.index}"
																		selected>${relaysetting.relayName}</option>
																</c:if>
																<c:if
																	test="${(productControlSetting.userId eq productUsers.userId) && (not productControlSetting.callAccess)}">
																	<option value="c-${count4.index}-${count3.index}">${relaysetting.relayName}</option>
																</c:if>

															</c:forEach>
														</c:forEach>
												</select></td>
												<td><label><button type="button"
															class="btn btn-danger ">REMOVE</button></label></td>
											</tr>

										</c:forEach>
									</tbody>
								</table>
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
								<!-- /input-group -->
							</div>
							<!-- /.col-lg-6 -->
						</div>

					</div>
				</div>
				<div class="panel panel-default">
					<!-- Default panel contents -->


					<!-- /.row -->
					<!-- Table -->
					<br>
					<div class="page-header" style="text-align: center;">
						<h3>My Registered Products</h3>
					</div>
					<div class="container panel panel-default">
						<table class="table">
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
												<input id="registred-product-field-${count2.index}" type="text" class="form-control"
													placeholder="${userProduct.name}"> <span
													class="input-group-btn">
													<button class="btn btn-primary" type="button" onClick="saveProductName('${userProduct.serialNumber}','${count2.index}');">SAVE</button>
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
		<!-- FOOTER START -->

		<footer class="footer">
			<div class="container">
				<p class="text-muted">2016 jTech. All rights reserved.</p>
			</div>
		</footer>
	</div>

	<!-- FOOTER END -->
</body>
</html>