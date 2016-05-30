
<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.Report" %>
<%@ page import="main.com.example.utility.CoreOptions"%>
<%@ page import="main.com.example.AndroidPythonUiautomatorExecutor"%>
<%
	final String TAG_REPORT = "report";
	final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
	final String TAG_REPORT_LIST = "report_list";
	
	List<Report> lstReport = (List<Report>) application.getAttribute(TAG_REPORT_LIST);
%>
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
</head>
<body>
	
	<div align="center">
		<table width="80%" align="center">
			<tr>
				<td width="60%" align="center">Pair Device</td>
				<td align="center">Status</td>
				<td align="center">Detail</td>
			</tr>
			<% for(int i=0 ; i<lstReport.size() ; i++){ 
				Report report = lstReport.get(i);
			%>
			<tr>
				<td align="center"><%=report.getPhone() + " " + report.getWatch() %></td>
				<td align="center"><%=report.isPassTesting() %></td>
				<td align="center">
					<a href="report.jsp?id=<%=i %>">detail</a>
				</td>
			</tr>
			<% } %>
		</table>
	</div>
</body>
</html>