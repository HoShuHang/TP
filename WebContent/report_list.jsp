<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	ArrayList<String> lstPairedDeviceFolder = (ArrayList<String>) request.getAttribute("folder");
	request.setAttribute("lstPairedDeviceFolder", lstPairedDeviceFolder);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Compatibility Testing Report</title>
</head>
<body>
	<c:forEach var="pairedDevicesFolder" items="${lstPairedDeviceFolder}">
		<a href="reports/${pairedDevicesFolder}/report.html">${pairedDevicesFolder}</a>
	</c:forEach>
</body>
</html>