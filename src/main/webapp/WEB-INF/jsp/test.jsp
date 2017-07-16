<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>

<html lang="zh-CN">
<head>
<title>Hello WebSocket</title>
<script type="text/javascript" src="../javascripts/jquery-2.1.4.min.js"></script>

</head>
<body>

	<div>
		<div>
		<button type="button" class="btn btn-info" id="add">添加</button>
		</div>
	</div>
	
	    <script>
        $(function(){
            $("#add").click(function(){       
                    var value = { "cardId":"123454",
                                 "password":"123435",
                                 "name":"efdsfsd",
                                 "sex":0,
                                 "tel":"",
                                 "count":0                               
                    };
					$.ajax({
						url : '/ace/home/test',
						type : 'post',
						contentType : "application/json; charset=utf-8",
						dataType : "json",
						data : JSON.stringify(value),
						success : function(data) {
							console.log(data);
							if (data.card_no == cardno) {
							
							}
						},
						error : function(data, err) {
							console.log("haha");
							console.log(err);
							console.log(data);				
						}
					});          
            });
        });

    </script>
</body>
</html>