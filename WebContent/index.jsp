<%@ page language="java" contentType="text/html; charset=BIG5"
	pageEncoding="BIG5"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.List"%>
<%@ page import="com.example.ADB"%>
<%@ page import="com.example.AndroidRobotFrameworkExecutor"%>
<%@ page import="com.example.AndroidPythonUiautomatorExecutor"%>

<%
	List<String> devices = ADB.getDevices();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Setting</title>
</head>
<body>
	<Form Method="GET" Action="PythonUiAutomatorServlet"
		Enctype="Multipart/Form-Data">
		<table>
			<tr>
				<td>Serial Number(Mobile):</td>
				<td><select name="mobile_serial_number">
						<%
							for (String device : devices) {
						%>
						<option value=<%=device%>><%=device%></option>
						<%
							}
						%>
				</select></td>
			</tr>
			<tr>
				<td>Serial Number(Wearable):</td>
				<td><select name="wear_serial_number">
						<%
							for (String device : devices) {
						%>
						<option value=<%=device%>><%=device%></option>
						<%
							}
						%>
				</select></td>
			</tr>
		</table>
		Test Script：<Input Type="file" multiple="multiple" id="testscript"
			Name="testscript"></br> <Input Type="Submit" Name="build"
			Value="Build"><br>


	</Form>
</body>
</html>