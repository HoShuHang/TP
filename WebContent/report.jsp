<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.*"%>
<%@ page import="main.com.example.entity.*"%>
<%
	String phone = request.getParameter("phone");
	String watch = request.getParameter("wearable");
	String detail = request.getParameter("detail");
	System.out.println("jsp detail = " + detail);
	String failCount = String.valueOf(detail.charAt(detail.indexOf("fail=")+5));
	String totalCount = String.valueOf(detail.charAt(detail.indexOf("total=") + 6));
	System.out.println("detail.indexOf(\"]\") = " + detail.indexOf("]"));
	String message = detail.substring(detail.indexOf("message=[")+9, detail.indexOf("]"));
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
		<%=phone%></p>
	<p>
		Watch:
		<%=watch%></p>
	<table>
		<tr>
			<td>
				<div class="infoBox" id="tests">
					<div class="counter"><%=totalCount%></div>
					<p>tests</p>
				</div>
			</td>
			<td>
				<div class="infoBox" id="failures">
					<div class="counter"><%=failCount%></div>
					<p>failures</p>
				</div>
			</td>
		</tr>
		<tr>
			<td align="left"><%=message%></td>
		</tr>
	</table>
</body>
</html>