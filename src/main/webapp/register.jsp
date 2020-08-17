<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>注册</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>
<!-- 头部 end -->
<div class="rg_layout">
    <div class="rg_form clearfix">
        <%--左侧--%>
        <div class="rg_form_left">
            <p>新用户注册</p>
            <p>USER REGISTER</p>
        </div>
        <div class="rg_form_center">
            <!--注册表单-->
            <form id="registerForm" onsubmit="return registerSubmit()" action="${pageContext.request.contextPath}/userController/register" method="post">
                <!--提交处理请求的标识符-->
                <input type="hidden" name="action" value="register">
                <table style="margin-top: 25px;width: 558px">
                    <tr>
                        <td class="td_left">
                            <label for="username">用户名</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="username" name="username" placeholder="请输入账号，5-10位字符">
							<span id="userInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <script>
                        var usernameFlag=false;
                        //清除警告信息
                        $("#username").click(function () {
                            $("#userInfo").text("");
                        });

                        $("#username").blur(function(){
                            //获取用户名
                            let username=this.value;
                            //用户名正则校验
                            let regExp=/^.{2,}$/
                            if(!username.match(regExp)){
                                $("#userInfo").text("用户名格式不正确").css("color","red");
                                usernameFlag=false;
                                return;
                            }
                            $.ajax({
                                url:"${pageContext.request.contextPath}/userController/usernameCheck",
                                data:"username="+username,
                                dataType:"json",
                                type:"get",
                                success:function(response){
                                    if (response.success) {
                                        $("#userInfo").text(response.message).css({"color":"green","font-size":"15px"});
                                        usernameFlag=true;
                                    }else{
                                        $("#userInfo").text(response.message).css("color","red");
                                        usernameFlag=false;
                                    }
                                },
                                error:function(){
                                    alert("服务器繁忙...")
                                }
                            });
                        });
                    </script>
                    <tr>
                        <td class="td_left">
                            <label for="telephone">手机号</label>
                        </td>
                        <td class="td_right">
                            <input type="text" id="telephone" name="telephone" placeholder="请输入您的手机号">
                            <span id="teleInfo" style="font-size:10px"></span>
                        </td>
                    </tr>
                    <script>
                        var telFlag=false;//号码是否可用
                        //清除警告信息
                        $("#telephone").click(function () {
                            $("#teleInfo").text("");
                        });

                        $("#telephone").blur(function(){
                            //获取输入值
                            let telephone=$(this).val();
                            //正则校验
                            let regExp=/^[1][358]\d{9}$/;
                            if(!telephone.match(regExp)) {
                                $("#teleInfo").text("手机号码格式不正确").css({"color":"red"});
                                telFlag=false;
                                return;
                            }
                            $.ajax({
                                url:"${pageContext.request.contextPath}/userController/telephoneCheck",
                                data:"telephone="+telephone,
                                dataType:"json",
                                type:"post",
                                success:function(response){
                                    if (response.success) {
                                        $("#teleInfo").text(response.message).css({"color":"green","font-size":"15px"});
                                        telFlag=true;
                                    }else{
                                        $("#teleInfo").text(response.message).css("color","red")
                                        telFlag=false;
                                    }
                                },
                                error:function () {
                                    alert("服务器繁忙....")
                                }
                            });
                        });
                    </script>
                    <tr>
                        <td class="td_left">
                            <label for="password">密码</label>
                        </td>
                        <td class="td_right">
                            <input type="password" id="password" name="password" placeholder="请输入密码">
                        </td>
                    </tr>
                    <tr>
                        <td class="td_left">
                            <label for="smsCode">验证码</label>
                        </td>
                        <td class="td_right check">
                            <input type="text" id="smsCode" name="smsCode" class="check" placeholder="请输入验证码">
                          
                            <input id="sendSmsCode" value="发送手机验证码" class="btn btn-link"/>
                        </td>
                    </tr>
                    <script>
                        $("#sendSmsCode").click(function(){
                            //获取手机号码
                            let telephone= $("#telephone").val()
                            //判断手机号码是否可用
                            if(telFlag==false||telephone==""){
                                return;
                            }
                            $.ajax({
                                url:"${pageContext.request.contextPath}/userController/valideCodeSMS",
                                data:"telephone="+telephone,
                                dataType:"json",
                                type:"get",
                                success:function(response){
                                    if(response.success){
                                        let num=60;
                                        setInterval(function () {
                                            num--;
                                            if(num>0) {
                                                $("#sendSmsCode").val("短信发送成功" + num + "s后可重新发送").attr({"disabled": "true"});
                                            }else{
                                                clearInterval();
                                            }
                                        },1000);
                                    }
                                },
                                error:function () {
                                    alert("服务器繁忙...")
                                }
                            });
                        });
                    </script>
                    <tr>
                        <td class="td_left">
                        </td>
                        <td class="td_right check">
                            <span id="msg" style="color: red;">${valideCodeError}</span>
                            <input type="submit" class="submit" value="注册">
                        </td>
                    </tr>
                    <%--设置表单提交条件--%>
                    <script>
                        function registerSubmit(){
                            if(telFlag&&usernameFlag){
                                return true;
                            }else{
                                return false;
                            }
                        }
                    </script>
                </table>
            </form>
        </div>
        <%--右侧--%>
        <div class="rg_form_right">
            <p>
                已有账号？
                <a href="javascript:$('#loginBtn').click()">立即登录</a>
            </p>
        </div>
    </div>
</div>
<!--引入尾部-->
<%@include file="footer.jsp"%>


</body>
</html>
