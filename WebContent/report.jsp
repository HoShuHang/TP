<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.AndroidPythonUiautomatorExecutor"%>
<%
	final String TAG_REPORT = "report";
	final String TAG_REPORT_SIZE = TAG_REPORT + "_size";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Report</title>
</head>
<body>
	<%
		Integer line_size = (Integer) request.getAttribute(TAG_REPORT_SIZE);

		for (int i = 1; i <= line_size; i++) {
	%>
	<p><%=request.getAttribute(TAG_REPORT + "_" + i)%></p>
	<%
		}
	%>
</body>
</html>