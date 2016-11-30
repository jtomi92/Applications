 
	
	$(document).ready(
		function() {
			
			connect();
			
			var currenttab = localStorage.getItem("currenttab");					
			if (currenttab != null && currenttab != ""){
			 
				var active_tab_selector = $(
				'.nav-tabs > li.active > a').attr('href');
				//find actived navigation and remove 'active' css
				var actived_nav = $('.nav-tabs > li.active');
				actived_nav.removeClass('active');
				
				//add 'active' css into clicked navigation
				//$(this).parents('li').addClass('active');
		
				//hide displaying tab content
				$(active_tab_selector).removeClass('active');
				$(active_tab_selector).addClass('hide');
				
				$('.nav-tabs a[href="' + currenttab + '"]').tab('show');
				$(currenttab).removeClass('hide');
				$(currenttab).addClass('active');
			}
			

		});
		
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
							localStorage.setItem("currenttab", target_tab_selector);
							$(target_tab_selector).removeClass('hide');
							$(target_tab_selector).addClass('active');
						});
			});
	$(document).ready(		
		function loadSelectedDropdownElements(){
			
			var options = $('#product-picker option');

			var serialNumbers = $.map(options ,function(option) {
				return option.id;
			});
			
			for (var i=0; i<serialNumbers.length; i++){
				console.log("SERIAL-"+i+"="+serialNumbers[i]);
				
				var settingPickerId = localStorage.getItem(serialNumbers[i] + "-setting-picker");
				var settingPickerValues = localStorage.getItem(serialNumbers[i] + "-setting-values");
				
				var controlPickerId = localStorage.getItem(serialNumbers[i] + "-control-picker");
				var controlPickerValues = localStorage.getItem(serialNumbers[i] + "-control-values");
				
				if (settingPickerId != null && settingPickerId != ""){								 
					var array = settingPickerValues.split(',');
					for(var j=0; j<array.length; j++){					
						$('#' + settingPickerId + ' option[value=' +array[j] + ']').attr('selected', true);
					}
					showRelaySetting(settingPickerId,'rel-setting-'+i);
				}
				if (controlPickerId != null && controlPickerId != ""){								 
					var array = controlPickerValues.split(',');					
					for(var j=0; j<array.length; j++){
						//console.log("CONTROLS=" + array[j]);
						$('#' + controlPickerId + ' option[value=' + array[j] + ']').attr('selected', true);
					}
					showProductRelayControl(controlPickerId,'rel-control-'+i);
				}
			}									
	});
	
	$(document).ready(	
		function loadSelectedTimerSettings(){
			var currentProductTimer = localStorage.getItem("current-product-timer");
			var currentTimerSetting = localStorage.getItem("current-timer-setting");
			
			if (currentProductTimer != null && currentProductTimer != ""){
					showHideTags(currentProductTimer,"div",currentTimerSetting,"relay");
			}
			
	});

	function showHideTags(container, tag, toShow, toHide) {
		
		localStorage.setItem("current-product-timer",container);
		localStorage.setItem("current-timer-setting",toShow);
		var elements = document.getElementById(container).getElementsByTagName(
				tag);
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id === toShow) {
					elements[i].style.display = "block";
				} else {
					if (id.indexOf(toHide) !== -1) {
						elements[i].style.display = "none";
					}
				}
			}
		}

	}

	function showRelaySetting(picker, section) {

		var selectedValues = $('#' + picker).val();

		var elements = document.getElementById(section).getElementsByTagName(
				'tr');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('relay-setting') !== -1) {
					elements[i].style.display = "none";
				}
			}
		}

		$.each(selectedValues, function(index, value) {

			for (var i = 0; i < elements.length; i++) {
				var id = elements[i].getAttribute("id");
				if (id !== null) {

					if (id === value) {
						elements[i].style.display = "table-row";
					}

				}
			}
		});
	}

	function showProductRelayControl(picker, section) {

		var selectedValues = $('#' + picker).val();
		var elements = document.getElementById(section).getElementsByTagName(
				'tr');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('relay-control') !== -1) {
					elements[i].style.display = "none";
				}
			}
		}

		$.each(selectedValues, function(index, value) {

			for (var i = 0; i < elements.length; i++) {
				var id = elements[i].getAttribute("id");
				if (id !== null) {

					if (id === value) {
						elements[i].style.display = "table-row";
					}

				}
			}
		});
	}

	$(document).ready(function loadProductDetails() {
		var savedView = localStorage.getItem("currentProductValue");
		if (savedView != null && savedView != ""){
			showProductControls(savedView);
		} else {
			showProductControls('0');
		}
	});

	function showProductControls(view) {
		var selectedValue;
		
		if (view == '0'){
			selectedValue = $('#product-picker').val();
		} else {
			selectedValue = view;
			$("#product-picker").find('selected').removeAttr("selected");
			$("#product-picker option[value="+selectedValue+"]").prop("selected", "selected")
		}
		var serialNumber = $('#product-picker').children(":selected")
				.attr("id");

		localStorage.setItem("currentSerialNumber", serialNumber);
		localStorage.setItem("currentProductValue", selectedValue);

		var elements = document.getElementById('product-relay-controls')
				.getElementsByTagName('div');
		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {
				if (id.indexOf('product-relay-control-') !== -1) {
					elements[i].style.display = "none";
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
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
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
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
				}
			}
		}

		for (var i = 0; i < elements.length; i++) {
			var id = elements[i].getAttribute("id");
			if (id !== null) {

				if (id === selectedValue) {
					elements[i].style.display = "block";
				}

			}
		}

	}
	
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
	
	
	
	
	
	
	
	
	
	
	
	// Script for getting selected relay values
	$(document).ready(function() {

	$('#product-dropdown').on('hidden.bs.dropdown', function() {
	});
	$('div[id^="product-rel-control-dropdown-"]').on('hidden.bs.dropdown', function() {
		var element = $(this).attr('id');
		var select = document.getElementById(element).getElementsByTagName("select");	 
		
		$.each( select, function( key, value ) {
			
			var selected = [];
			$("#" + value.id + " :selected").each(
					function() {
						selected.push($(this).val());
					});
			var serialNumber = $('#product-picker').children(":selected").attr("id");
			localStorage.setItem(serialNumber + "-control-picker", value.id);
			localStorage.setItem(serialNumber + "-control-values", selected);
		});	
		
	});		
	$('div[id^="product-relay-setting-dropdown-"]').on('hidden.bs.dropdown', function() {
		var element = $(this).attr('id');
		var select = document.getElementById(element).getElementsByTagName("select");	 
		
		$.each( select, function( key, value ) {
			
			var selected = [];
			$("#" + value.id + " :selected").each(
					function() {
						selected.push($(this).val());
					});
			var serialNumber = $('#product-picker').children(":selected").attr("id");
			localStorage.setItem(serialNumber + "-setting-picker", value.id);
			localStorage.setItem(serialNumber + "-setting-values", selected);
		});	
		
	});
		
	});

	$("#menu-toggle").click(function(e) {
		e.preventDefault();
		$("#wrapper").toggleClass("toggled");
	});
	// Script for getting selected relay values END	
	