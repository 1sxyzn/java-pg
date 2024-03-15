<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%
	int a = Integer.parseInt(request.getParameter("a"));
	int b = Integer.parseInt(request.getParameter("b"));
	
	long result = 0;
	
	switch(request.getParameter("op")){
		case "+":
			result = a+b;
			break;
		case "-":
			result = a-b;
			break;
		case "*":
			result = a*b;
			break;
		case "/":
			result = a/b;
			break;
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 계산기</title>
</head>
<body>
	<h2>JSP 계산기</h2>
	<hr> 계산 결과: <%=result%>
</body>
</html>