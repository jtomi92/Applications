function registerProduct() {
		var data = {};
		var serialNumber = $("#registrationSerialNumber").val();

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		document.getElementById("register-error-message").style.visibility = "hidden";
		
		if (serialNumber == "" || serialNumber == null){
			document.getElementById("register-error-message").innerHTML = "Invalid serial number.";
			document.getElementById("register-error-message").style.visibility = "visible";
			return;
		}
		var userid = document.getElementById("userid").value;
		var serialNumber = $("#registrationSerialNumber").val();
		data['userId'] = userid;
		data['serialNumber'] = serialNumber;

		var result = '';
		//console.log(data);

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					async : false,
					url : 'register',
					data : JSON.stringify(data),
					dataType : 'json',
					beforeSend : function(xhr) {
						// here it is
						xhr.setRequestHeader(header, token);
					},
					success : function(res, ioArgs) {
						result = res;

						//console.log(res);

						if (res == '0') {
							document.getElementById("register-error-message").innerHTML = "Serial Number does not exist.";
							document.getElementById("register-error-message").style.visibility = "visible";
						} else {
							document.getElementById("register-success-message").innerHTML = "You have successfully registered your product";
							document.getElementById("register-success-message").style.visibility = "visible";
							restartDevice(serialNumber);
							location.reload();
						}

					},
					error : function(e) {
						//console.log("error");
						//console.log(e);
					}

				});

	}

	$(document)
			.ready(
					function() {
						if (localStorage.getItem("showNotificationModal") == "true") {

							document.getElementById("notificationModalTitle").innerHTML = localStorage
									.getItem("notificationModalTitle");
							document.getElementById("notificationModalContent").innerHTML = localStorage
									.getItem("notificationModalContent");

							$('#notificationModal').modal('show');
							localStorage.setItem("showNotificationModal",
									"false");
						}
					});

	function saveProductName(serialNumber, index) {
		var data = {};
		var productName = document.getElementById("registred-product-field-"
				+ index).value;
		var userid = document.getElementById("userid").value;
		
		//console.log("SERIAL=" + serialNumber + " PRODUCTNAME=" + productName + " USERID=" + userid);

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		data['userId'] = userid;
		data['serialNumber'] = serialNumber;
		data['productName'] = productName;

		//console.log(data);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'update',
			data : JSON.stringify(data),
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {

				//console.log(res);

				if (res == '0') {
					
				} else {
					
				}
				location.reload();

			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});

	}

	function addProductUser(serialNumber, index) {

		var userToAdd = document.getElementById("add-product-user-" + index).value;
		
			
		document.getElementById("user-error-message-" + index).style.visibility = "hidden";
		//console.log("SERIALNUMBER=" + serialNumber);
		//console.log("INPUTID=" + index);
		//console.log("USER=" + userToAdd);		
		
		if (userToAdd === null || userToAdd === ""){
			document.getElementById("user-error-message-" + index).innerHTML = "Field can not be empty.";
			document.getElementById("user-error-message-" + index).style.visibility = "visible";
			return;
		}

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		var result = '';

		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'productuser/add/'
					+ serialNumber + '/' + userToAdd,
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
					document.getElementById("user-error-message-" + index).innerHTML = "User does not exist.";
					document.getElementById("user-error-message-" + index).style.visibility = "visible";
				} else if (res == '1'){
					//console.log('1' + res);
					updateDevice(serialNumber);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}
		

			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});

	}

	function removeProductUser(userToRemove, serialNumber, input) {
		
		var currUser = document.getElementById("username").value;
		//console.log("CURRENTUSER=" + currUser);
		if (currUser == userToRemove){
			//console.log("do not delete urself lol");
			return;
		}
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		var result = '';

		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'productuser/remove/'
					+ serialNumber + '/' + userToRemove,
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					updateDevice(serialNumber);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
		
	}

	function updateProductUser(userName, serialNumber, input) {
		var data = {}; 
		var selectedCallAccess = [];
		$("#product-relay-call-picker-" + input + " :selected").each(
				function() {
					selectedCallAccess.push($(this).text());
				});

		var selectedRelayAccess = [];
		$("#product-relay-priv-picker-" + input + " :selected").each(
				function() {
					selectedRelayAccess.push($(this).text());
				});
		var privilige = $("#product-priv-picker-" + input + " :selected").text();
		
		//console.log("PRIVILIGE=" + privilige);
		//console.log("USER=" + userName);
		//console.log("SERIALNUMBER=" + serialNumber);
		//console.log("PRIV-VALUES=" + selectedRelayAccess);
		//console.log("CALL-VALUES=" + selectedCallAccess);
		
		data['relayAccess'] = selectedRelayAccess;
		data['callAccess'] = selectedCallAccess;
		data['privilige'] = privilige;
		
		//console.log(data);
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		$.ajax({
			type : "PUT",
			contentType : "application/json",
			async : false,
			url : 'productuser/update/'
					+ serialNumber + '/' + userName,
			data : JSON.stringify(data),
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					updateDevice(serialNumber);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}		
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
	}
	
	
	function updateProductSettings(serialNumber, position) {
		
		var moduleIds = [];
		var relayIds = [];
		var relayNames = [];
		var delays = [];
		var impulses = [];
		
		var startWeekDays = [];  
		var endWeekDays = [];  
		var startTimers = [];
		var endTimers = [];
		var timerEnabled = [];
 
		$('td[id^="'+serialNumber + '-moduleid-"]').each(function() {
			moduleIds.push($(this).html()); 			
		});
		
		$('td[id^="'+serialNumber + '-relayid-"]').each(function() {
			relayIds.push($(this).html()); 			
		});
		
		//console.log('input[id^="'+serialNumber + '-relayname-"]');
		$('input[id^="'+serialNumber + '-relayname-"]').each(function() {
			
			relayNames.push($(this).val()); 			
		});
		
		$('input[id^="'+serialNumber + '-delay-"]').each(function() {
			delays.push($(this).val()); 			
		});
		
		$('input[id^="'+serialNumber + '-impulse-"]').each(function() {
			impulses.push($(this).is(':checked') ? "1" : "0"); 			
		});
		
		$('select[id^="weekday-picker-start-'+serialNumber +'"]').each(function() {
			startWeekDays.push($(this).val()); 			
		});
		
		$('select[id^="weekday-picker-end-'+serialNumber +'"]').each(function() {
			endWeekDays.push($(this).val()); 			
		});	
		
		$('input[id^="timer-enabled-'+serialNumber +'"]').each(function() {
			timerEnabled.push($(this).is(':checked') ? "1" : "0"); 			
		});
		
		$('input[id^="start-timer-'+serialNumber +'"]').each(function() {
			startTimers.push($(this).val()); 			
		});
		
		$('input[id^="end-timer-'+serialNumber +'"]').each(function() {
			endTimers.push($(this).val()); 			
		});
		
		var tosend = {"moduleIds" : moduleIds,
					"relayIds" : relayIds,
					"relayNames" : relayNames,					
					"delays" : delays,
					"impulses" : impulses,	
					"startWeekDays" : startWeekDays,
					"endWeekDays" : endWeekDays,
					"timerEnabled" : timerEnabled,
					"startTimers" : startTimers,
					"endTimers" : endTimers
					}; 

		//console.log(JSON.stringify(tosend));
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type : "PUT",
			contentType : "application/json",
			async : false,
			url : 'productsetting/update/'
					+ serialNumber,
			data : JSON.stringify(tosend),
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					updateDevice(serialNumber);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}		
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
	 
	}
	
	function switchRelay(serialNumber, moduleId, relayId){
		
		//stompSend();
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var userid = document.getElementById("userid").value;
		var relaystatus;
		if (document.getElementById("relaystatus-" + serialNumber + "-" + moduleId + "-" + relayId).innerHTML == "ON"){
			relaystatus = 0;
		} else {
			relaystatus = 1;
		}
		document.getElementById("relay-progressbar-" + serialNumber + "-" + moduleId + "-" + relayId).style.display = "block";
		document.getElementById("relay-switch-" + serialNumber + "-" + moduleId + "-" + relayId).style.display = "none";
		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'relay/' + userid + '/'
					+ serialNumber + '/' + moduleId + '/' + relayId + '/' + relaystatus,
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}		
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
	}
	
	
	
	function updateDevice(serialNumber){
		
		//stompSend();
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var userid = document.getElementById("userid").value;

		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'device/update/' + userid + '/'
					+ serialNumber,
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}		
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
	}
	
	
	function restartDevice(serialNumber){
		
		//stompSend();
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var userid = document.getElementById("userid").value;
	
		$.ajax({
			type : "POST",
			contentType : "application/json",
			async : false,
			url : 'device/restart/' + userid + '/'
					+ serialNumber,
			dataType : 'json',
			beforeSend : function(xhr) {
				// here it is
				xhr.setRequestHeader(header, token);
			},
			success : function(res, ioArgs) {
				
				if (res == '0'){
					//console.log('0' + res);
				} else if (res == '1'){
					//console.log('1' + res);
					location.reload();
				} else if (res == '-1'){
					//console.log('-1' + res);
				}		
			},
			error : function(e) {
				//console.log("error");
				//console.log(e);
			}

		});
	}
	
	
	