// JavaScript Document
//支持Enter键登录
		document.onkeydown = function(e){
			if($(".bac").length==0)
			{
				if(!e) e = window.event;
				if((e.keyCode || e.which) == 13){
					var obtnLogin=document.getElementById("submit_btn")
					obtnLogin.focus();
				}
			}
		}

    	$(function(){
			//提交表单
			$('#submit_btn').click(function(){
				show_loading();
				var username = $("#username").val();
				var password = $("#password").val();
				if(username == ''){
					show_err_msg('账号还没填呢！');	
					$('#username').focus();
				}
				else if(password == ''){
					show_err_msg('密码还没填呢！');
					$('#password').focus();
				}else{
					//ajax提交表单，#login_form为表单的ID。 如：$('#login_form').ajaxSubmit(function(data) { ... });
					var data = {"username":username,"password":password};
					$.ajax({
						url: '/login',
						type: 'post',
						data: data,
						success: function(data,status){
							if(status == 'success'){
								location.href = 'home';
								//show_msg('登录成功咯！  正在为您跳转...','/home');
							}
						},
						error: function(data,err){
							$("#check-block").text("用户名不存在或密码错误！");
							show_err_msg('账号或密码错误！');
							/*location.href = 'yzc';*/
						}
					});

				}
			});
		});