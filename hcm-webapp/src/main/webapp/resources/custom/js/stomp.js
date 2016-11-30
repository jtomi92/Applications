var stompClient = null;

		var stompFailureCallback = function (error) {
			console.log('STOMP: ' + error);
			setTimeout(connect, 5000);
			console.log('STOMP: Reconecting in 10 seconds');
		};
        
        function connect() {
			
			var userid = document.getElementById("userid").value;
			console.log("connecting");
            var socket = new SockJS('/messaging');
			stompClient = Stomp.over(socket);
			stompClient.connect({}, function(frame) {
				console.log('Connected: ' + frame);
			}, stompFailureCallback);
 
			 
			socket.onmessage = function(e) {
				if (e.data.includes("Hello")){
					console.log("asd=" + e.data);
					stompClient.send("/messaging", {}, 'USER#' + userid + '#');
				} else {
					var fields = e.data.split(" ");
					
					if (fields.length > 3 && fields[0] == "SWITCH"){
						var serialNumber = fields[1];
						var relayId = fields[2];
						var stat = fields[3];
							
						if (stat == "1"){
							document.getElementById("relaystatus-" + serialNumber + "-" + relayId).innerHTML = "ON";
							document.getElementById("relaystatus-" + serialNumber + "-" + relayId).className = "label label-success";
						} else if (stat == "0"){
							document.getElementById("relaystatus-" + serialNumber + "-" + relayId).innerHTML = "OFF";
							document.getElementById("relaystatus-" + serialNumber + "-" + relayId).className = "label label-danger";
						}
									
						console.log(serialNumber + " "  + relayId + " " + stat);
					} else if (fields.length > 2 && fields[0] == "CONNECTION"){
						var serialNumber = fields[1];
						var stat = fields[2];
						
						if (stat == "ONLINE"){
							
							$("#"+serialNumber+"-connection-status").removeClass("alert-danger");
							$("#"+serialNumber+"-connection-status").addClass("alert-success");
							$("#"+serialNumber+"-connection-status").text("Device is ONLINE");
							
							$.growl.notice({ message: serialNumber + " is now ONLINE" });
						} else if (stat == "OFFLINE"){
							
							$("#"+serialNumber+"-connection-status").removeClass("alert-success");
							$("#"+serialNumber+"-connection-status").addClass("alert-danger");
							$("#"+serialNumber+"-connection-status").text("Device is OFFLINE");
							
							$.growl.error({ message: serialNumber + " is now OFFLINE" });
						}
 
						 
					}
				}
				console.log("STOMP RECEIVE=" + e.data);
			}
        }
		
		window.onbeforeunload = function(event)
		{
			disconnect();
		};
		
        function disconnect() {
            stompClient.disconnect();
            console.log("Disconnected");
        }
        function stompSend() {
           
            stompClient.send("/messaging", {}, 'asd');
        }
        function showResult(message) {
            
			console.log("STOMP=" + message);
        }