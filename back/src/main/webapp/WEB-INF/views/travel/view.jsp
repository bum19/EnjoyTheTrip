<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Travel View</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
</head>
<body>
<%@ include file="/WEB-INF/views/common/confirm.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>
	<script src="http://code.jquery.com/jquery-3.7.1.min.js"></script>
	<div>
		<form action="${root}/travel">
			<select name="sido" id="search-area" onchange="hideMarkers()">
				<option value="none">=== 시/도 ===</option>
			</select> <select name="gugun" id="search-sigungu" onchange="hideMarkers()">
				<option value="none">=== 구/군 ===</option>
			</select> <select name="category" id="search-content-id"
				onchange="hideMarkers()">
				<option value="none">=== 종류 ===</option>
			</select> <input id="searchBtn" type="hidden" name="sign" value="mapfetch" />
			<input type="submit" value="검색">
		</form>
	</div>
	<!-- kakao map start -->
	<div id="map" class="mt-3" style="width: 100%; height: 500px"></div>
	<!-- kakao map end -->
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0d15b933750818d836154d4224b2d03e&libraries=services,clusterer,drawing"></script>
	<script type="module" src="/assets/js/map.js">
		main();
	</script>
	<table id="resultTable">
		<thead id="tableHead">
			<tr>
				<th>이름</th>
				<th>주소</th>
				<th>위도</th>
				<th>경도</th>
			</tr>
		</thead>
		<tbody id="tableBody">
			<!-- Table rows will be added here -->
		</tbody>
	</table>
	<script>
		document.getElementById("gugunFetchBtn").addEventListener("click", async function() {
			let gugunCode = document.getElementById("gugunCodeInput").value;
			let body = JSON.stringify({
					gugunCode
			});
			console.log(body);
			
			body = await fetch("/travel/search/gugunCode", {method:"post", body, headers: {
                'Content-Type': 'application/json'  // JSON 형식의 데이터를 보낼 때 Content-Type을 설정합니다.
            }});
			console.log(body);
			
			body = await body.json();
			console.log(body);
			
			if(body.travelSites)
				populateTable(body);
		});
		
	        // Function to populate the table with JSON data
	        function populateTable(json) {
	            const tableBody = document.getElementById("resultTable");
				
	            // Clear the existing table rows
	            tableBody.innerHTML = '';
				
	            // Loop through the JSON data and create table rows
	            json.travelSites.forEach(item => {
	                const newRow = tableBody.insertRow(tableBody.rows.length);

	                const cell1 = newRow.insertCell(0);
	                const cell2 = newRow.insertCell(1);
	                const cell3 = newRow.insertCell(2);
	                const cell4 = newRow.insertCell(3);

	                cell1.innerHTML = item.title;
	                cell2.innerHTML = item.addr1;
	                cell3.innerHTML = item.latitude;
	                cell4.innerHTML = item.longitude;
	            });
	        }

	    </script>

	<script>
	$("#search-area").on(
			"change",
			function() {
				var selectedSido = $("#search-area").val();
				console.log(selectedSido);
				if (selectedSido == null) {
					console.log("시/도를 먼저 선택해주세요")
					return;
				}
				 
				 var postData = {
		                    sidoCode: selectedSido,
		                };

		                $.post({
		                    url: "/travel/search/getGugunList",
		                    data: JSON.stringify(postData),
		                    contentType: "application/json; charset=utf-8", // Set content type and encoding
		                    dataType: "json",
		                    success: function (data, status) {
		                        var $select = $("#search-sigungu");
							$select.find("option").remove();
							$.each(data.gugunList, function(index, item) {
								$("<option>").val(item.gugun_code).text(
										item.gugun_name).appendTo($select);
							});
		                    },
		                    error: function (jqXHR, textStatus, errorThrown) {
		                        // This function will be called if the request fails
		                        console.log("Request failed: " + textStatus + ", " + errorThrown);
		                    }
		                });
			});

	$(document).on(
			"change",
			"#typeLoad",
			function() {
				   $.post({
				        url: "/travel/search/getTypeList",
				        success: function (data, status) {
				            var $select = $("#search-content-id");
									$select.find("option").remove();
									
									$.each(data.typeList, function(index, item) {
										$("<option>").val(item.category).text(
												item.name).appendTo($select);
									});
				        },
				        error: function (jqXHR, textStatus, errorThrown) {
				            // This function will be called if the request fails
				            console.log("Request failed: " + textStatus + ", " + errorThrown);
				        }
				    });
			});

	$(window).on(
			"load",
			function() {
				console.log("하고 있음");
				// 카테고리 가져오기
			   $.post({
				        url: "/travel/search/getTypeList",
				        success: function (data, status) {
				            var $select = $("#search-content-id");
									$select.find("option").remove();
									
									$.each(data.typeList, function(index, item) {
										$("<option>").val(item.category).text(
												item.name).appendTo($select);
									});
				        },
				        error: function (jqXHR, textStatus, errorThrown) {
				            // This function will be called if the request fails
				            console.log("Request failed: " + textStatus + ", " + errorThrown);
				        }
				    });

				// 시/도 가져오기
			   $.post({
			        url: "/travel/search/getSidoList",
			        success: function (data, status) {
			   	var $select = $("#search-area");
								$select.find("option").remove();
								$.each(data.sidoList, function(index, item) {
									$("<option>").val(item.sido_code).text(
											item.sido_name).appendTo($select);
								});
			        },
			        error: function (jqXHR, textStatus, errorThrown) {
			            // This function will be called if the request fails
			            console.log("Request failed: " + textStatus + ", " + errorThrown);
			        }
			    });

			});
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
</body>
</html>