
<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.Report"%>
<%@ page import="main.com.example.entity.Pair"%>
<%@ page import="main.com.example.utility.CoreOptions"%>
<%@ page import="main.com.example.AndroidPythonUiautomatorExecutor"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<<%-- % final String TAG_REPORT = "report"; final String TAG_REPORT_SIZE
= TAG_REPORT + "_size"; final String TAG_REPORT_LIST = "report_list";

List<Report> lstReport = (List<Report>)
application.getAttribute(TAG_REPORT_LIST); %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">








<html>

<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>Report</title>
<link href="css/execute.css" rel="stylesheet" type="text/css">

<link href="css/custom.css" rel="stylesheet" type="text/css">

<!-- Bootstrap Core CSS - Uses Bootswatch Flatly Theme: http://bootswatch.com/flatly/ -->
<link href="css/bootstrap.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/freelancer.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">
<link href="http://fonts.googleapis.com/css?family=Montserrat:400,700"
	rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic"
	rel="stylesheet" type="text/css">


<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
<script type="text/javascript">
	var last_response = '';
	const
	POLLING_INTERVAL = 10000;
	window.onload = function() {
		polling()
		get_pairs = setInterval(polling, POLLING_INTERVAL);
	}

	function polling() {
		$.ajax({
			url : "pairs",
			type : "GET",
			success : function(response) {
				response = $.parseJSON(response)
				last_response = response
				loadTable('table', [ 'phone', 'watch', 'status', 'detail' ],
						response["pairs"]);
				if (response["all_complete"]) {
					clearInterval(get_pairs)
				}
			},
			error : function(data) {
			}
		});
	}

	function loadTable(tableId, fields, jsonArray) {
		//$('#' + tableId).empty(); //not really necessary
		var rows = '';
		var phone = '';
		var wearable = '';
		rows += '<tr><td width="30%" align="center">Phone</td><td width="30%" align="center">Watch</td><td align="center">Status</td><td align="center">Detail</td></tr>';
		$
				.each(
						jsonArray,
						function(index, item) {
							var row = '<tr>';
							$
									.each(
											fields,
											function(index, field) {
												if (index == 0) {
													phone = item[field];
												}
												if (index == 1) {
													wearable = item[field];
												}
												if (index < 3)
													row += '<td align="center">'
															+ item[field]
															+ '</td>';
												else {
													if (item[field])
														row += '<td align="center"><form action="report.jsp"><input type="hidden" name="phone" value="'+phone+'"><input type="hidden" name="wearable" value="'+wearable+'"><input type="hidden" name="detail" value="'+item[field]+'"><input type="submit" value="report"></form></td>';
													else
														row += '<td align="center"><div class="loader"></div></td>';
												}
											});
							rows += row + '</tr>';
						});
		$('#' + tableId).html(rows);
	}
</script>
</head>
<body>
	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header page-scroll">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#page-top">Results</a>
		</div>
	</div>
	</nav>
	<section>
	<div align="center">
		<table id="table" width="60%" align="center" border="1">
			<tr>
				<td width="30%" align="center">Phone</td>
				<td width="30%" align="center">Watch</td>
				<td align="center">Status</td>
				<td align="center">Detail</td>
			</tr>
			<c:forEach var="pair" items="${pairs}">
				<tr>

					<td align="center">${pair.getPhone().getSerialNum()}</td>
					<td align="center">${pair.getWear().getSerialNum()}</td>

					<c:choose>
						<c:when test="${pair.isTestComplete()}">
							<td align="center">complete</td>
						</c:when>
						<c:otherwise>
							<td align="center">waiting for test</td>
						</c:otherwise>
					</c:choose>

					<%-- <td align="center">${pair.getReport().isPassTesting()}</td> --%>

					<td align="center">
						<%-- <a href="report.jsp?id=<%=i %>">detail</a> --%>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	</section>
</body>
</html>
