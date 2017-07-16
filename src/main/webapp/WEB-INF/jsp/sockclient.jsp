<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>web socket client</title>
<script type="text/javascript" src="../javascripts/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="../javascripts/sockjs-1.1.1.js"></script>
<script type="text/javascript" src="../javascripts/stomp.js"></script>
<script type="text/javascript">
 	$(document).ready(function() {
		var socket = new SockJS("http:localhost:8080/ace/hello");
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);

			stompClient.subscribe('/app/macro', function(greeting) {
				alert(JSON.parse(greeting.body).content);
				console.debug(JSON.parse(greeting.body).content);
			});

			stompClient.subscribe('/topic/greetings', function(greeting) {
				alert(JSON.parse(greeting.body).content);
				console.debug("greeting:" + JSON.parse(greeting.body).content);
			});
		});
	}); 
</script>
</head>
<body>
	<div>
		<div>
			<button id="receive" > 接收消息</button>
		</div>
		<div id="conversationDiv">
			<label>What is your name?</label><input type="text" id="content" />
			<button id="send">发送</button>
			<p id="response"></p>
		</div>
	</div>
	<script>
		$(function() {
			$("#send").click(function() {
				var content = $("#content").val();
				stompClient.send("/app/hello", {}, JSON.stringify({
					'name' : content
				}));
			});
			
			$("#receive").click(function(){				
				var socket = new SockJS("<c:url value='/hello'/>");
				stompClient = Stomp.over(socket);
				stompClient.connect({}, function(frame) {
					stompClient.subscribe('/topic/feed', function(greeting) {
						console.log("2311111111111");
						alert(JSON.parse(greeting.body).content);
					});
				});
				
				$.ajax({
					url : '/ace/feed?greeting=yzc',
					type : 'get',
					contentType : "application/json; charset=utf-8",
					success : function(data) {
						console.log(data);
					},
					error : function(data) {
						console.debug(data);
					}
				});
			});
			
		});
	</script>
</body>
</html>