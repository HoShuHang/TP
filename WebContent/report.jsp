<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.*"%>
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
<title>Test Summary</title>
</head>
<body>
	<h1>Test Summary</h1>
	<p>
		Phone:
		<%=report.getPhone().getSerialNum()%></p>
	<p>
		Watch:
		<%=report.getWatch().getSerialNum()%></p>
	<table>
		<tr>
			<td>
				<div class="infoBox" id="tests">
					<div class="counter"><%=report.getTotalTestCase()%></div>
					<p>tests</p>
				</div>
			</td>
			<td>
				<div class="infoBox" id="failures">
					<div class="counter"><%=report.getFailTestCaseNumber()%></div>
					<p>failures</p>
				</div>
			</td>
		</tr>
		<tr>
			<td align="left" colspan="2"><%=report.getTestingMessage()%></td>
		</tr>
	</table>
</body>
</html>