
<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.Report" %>
<%@page import="main.com.example.utility.CoreOptions"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Report</title>
</head>
<body>
	<%
		for(Report report : lstReport){
	%>
			<p><%=report.getPhone() + " " + report.getWatch() %></p>
			<%
				List<String> content = report.getTestingMessage();
				for(String line : content){	
			%>
				<p><%=line %></p>
			<%} %>
	<%	} %>
</body>
</html>