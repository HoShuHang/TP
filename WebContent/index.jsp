<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.ADB" %>
<%@ page import="com.example.AndroidRobotFrameworkExecutor" %>
<%@ page import="com.example.AndroidPythonUiautomatorExecutor" %>

<% List<String> devices = ADB.getDevices(); %>
<% AndroidPythonUiautomatorExecutor builder = new AndroidPythonUiautomatorExecutor(); %>
<%
	String testscript = request.getParameter("build");
	if("POST".equalsIgnoreCase(request.getMethod())){
		System.out.println("here");
		builder.executeTest();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
	<Form Method="Post" Action="index.jsp" Enctype="Multipart/Form-Data">
		<table>
			<tr>
				<td>Serial Number(Mobile):</td>
				<td>
					<select>
						<% for(String device : devices){ %>
						  <option value= <%=device %>><%=device %></option>
					  	<% } %>
					</select>
				</td>
			</tr>
			<tr>
				<td>Serial Number(Wearable):</td>
				<td>
					<select>
						<% for(String device : devices){ %>
						  <option value= <%=device %>><%=device %></option>
					  	<% } %>
					</select>
				</td>
			</tr>
		</table>
  		Test Script：<Input Type="File" Name="testscript"><br>
		<Input Type="Submit" Name="build" Value="Build"><br>
		
		
	</Form>
</body>
</html>