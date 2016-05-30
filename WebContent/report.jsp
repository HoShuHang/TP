<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.Report" %>
<%
	final String TAG_REPORT_LIST = "report_list";
	String index = request.getParameter("id");
	List<Report> lstReport = (List<Report>) application.getAttribute(TAG_REPORT_LIST);
	Report report = lstReport.get(Integer.parseInt(index));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	<p><%=report.getPhone() %> </p>
</body>
</html>