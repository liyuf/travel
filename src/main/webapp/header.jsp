<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--bootstrap--%>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
<script src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/getParameter.js"></script>

<!-- 头部 start -->
<header id="header">
    <%--广告--%>
    <div class="top_banner">
        <img src="${pageContext.request.contextPath}/images/top_banner.jpg" alt="">
    </div>
    <%--右侧按钮--%>
    <div class="shortcut">
        <!-- 未登录状态 -->
        <c:if test="${empty currentUser}">
		<div class="login_out">
			<a id="loginBtn" data-toggle="modal" data-target="#loginModel" style="cursor: pointer;">登录</a>
			<a href="register.jsp" style="cursor: pointer;">注册</a>
		</div>
        </c:if>
        <!-- 登录状态 -->
        <c:if test="${not empty currentUser}">
		<div class="login">
			<span>欢迎回来，${currentUser.username}</span>
			<a href="${pageContext.request.contextPath}/home_index.jsp" class="collection">个人中心</a>
			<a href="javascript:showCart()" class="collection">购物车</a>
			<a href="${pageContext.request.contextPath}/userController/logOff">退出</a>
		</div>
        </c:if>
    </div>
        <%--购物车--%>
        <script>
            function showCart(){
                if(${empty currentUser}){
                    $("#loginBtn").click();
                }else{
                    location.href="${pageContext.request.contextPath}/routeController/showCart";
                }
            }
        </script>
    <%--搜索框--%>
    <div class="header_wrap">
        <div class="topbar">
            <div class="logo">
                <a href="/"><img src="${pageContext.request.contextPath}/images/logo.jpg" alt=""></a>
            </div>
            <div class="search">
                <input id="rname" name="rname" type="text" placeholder="请输入路线名称" class="search_input" value="${keyword}"
                       autocomplete="off">
                <a href="javascript:void(0);" onclick="searchClick()" class="search-button">搜索</a>
            </div>
            <%--搜索功能--%>
            <script>
                function searchClick() {
                    //获取关键字
                    let keyword=$("#rname").val();
                    //后台发送
                    location.href="${pageContext.request.contextPath}/routeController/routeSearch?currentPage=1&count=10&keyword="+keyword;
                }
            </script>
            <div class="hottel">
                <div class="hot_pic">
                    <img src="${pageContext.request.contextPath}/images/hot_tel.jpg" alt="">
                </div>
                <div class="hot_tel">
                    <p class="hot_time">客服热线(9:00-6:00)</p>
                    <p class="hot_num">400-618-9090</p>
                </div>
            </div>
        </div>
    </div>
</header>
<!-- 头部 end -->
<!-- 首页导航 -->
<div class="navitem">
    <ul class="nav" id="categoryUI">

    </ul>
</div>
<%--导航栏加载--%>
<script>
    $(function(){
        $.ajax({
            url:"${pageContext.request.contextPath}/routeController/categoryShow",
            dataType:"json",
            success:function(resp){
                $("#categoryUI").append("<li class=\"nav-active\"><a href=\"${pageContext.request.contextPath}/index.jsp\">首页</a></li>");
                for(var i=0;i<resp.length;i++){
                    $("#categoryUI").append("<li><a href=\"${pageContext.request.contextPath}/routeController/routeInfo?currentPage=1&count=10&cid="+resp[i].cid+"\">"+resp[i].cname+"</a></li>");
                }
                $("#categoryUI").append("<li><a href=\"favoriterank.jsp\">收藏排行榜</a></li>")
            },
            error:function () {
                alert("网络繁忙...")
            }
        });
    });
</script>
<!-- 登录模态框 -->
<div class="modal fade" id="loginModel" tabindex="-1" role="dialog" aria-labelledby="loginModelLable">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <%--头部--%>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="loginModelLable">
                    <ul id="myTab" class="nav nav-tabs" style="width: auto">
                        <li class="active">
                            <a href="#pwdReg" data-toggle="tab">
                                密码登录
                            </a>
                        </li>
                        <li><a href="#telReg" data-toggle="tab">短信登录</a></li>
                    </ul>
                    <span id="loginErrorMsg" style="color: red;"></span>
                </h4>

            </div>
            <%--内容--%>
            <div id="myTabContent" class="tab-content">
                <%--密码登录--%>
                <div class="tab-pane fade in active" id="pwdReg">
                    <form id="pwdLoginForm" action="${pageContext.request.contextPath}/userController/login4PWD" method="post">
                        <%--<input type="hidden" name="action" value="pwdLogin">--%>
                        <div class="modal-body">
                            <div class="form-group">
                                <label>用户名</label>
                                <input type="text" class="form-control" id="login_username" name="username"
                                       placeholder="请输入用户名">
                            </div>
                            <div class="form-group">
                                <label>密码</label>
                                <input type="password" class="form-control" id="login_password" name="password"
                                       placeholder="请输入密码">
                            </div>
                        </div>
                        <div class="modal-footer">
							<span id="pwdLoginSpan" style="color:red"></span>
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" id="pwdLogin" class="btn btn-primary" value="登录"/>
                        </div>
                    </form>
                </div>
                    <script>
                        $("#pwdLogin").click(function () {
                            let loginInfo=$("#pwdLoginForm").serialize();
                            $.ajax({
                                url:"${pageContext.request.contextPath}/userController/login4PWD",
                                data:loginInfo,
                                dataType:"json",
                                success:function(response){
                                    if(response.success){
                                        location.reload();
                                    }else{
                                        $("#pwdLoginSpan").text(response.message)
                                    }
                                },
                                error:function () {
                                    alert("服务器繁忙.....")
                                }
                            })
                        });
                    </script>
                <%--短信登录--%>
                <div class="tab-pane fade" id="telReg">
                    <form id="telLoginForm" method="post" action="#">
                        <input type="hidden" name="action" value="telLogin">
                        <div class="modal-body">
                            <div class="form-group">
                                <label>手机号</label>
                                <input type="text" class="form-control" name="telephone" id="login_telephone"
                                       placeholder="请输入手机号">
                            </div>
                            <div class="form-group">
                                <label>手机验证码</label>
                                <input type="text" class="form-control" id="login_check" name="valideCode"
                                       placeholder="请输入手机验证码">
                            </div>
                            <%--<a href="javaScript:void(0)" id="login_sendSmsCode">发送手机验证码</a>--%>
                            <input type="button" id="login_sendSmsCode" value="发送手机验证码">
                        </div>
                        <%--发送短信--%>
                        <script>
                            <%--点击事件--%>
                            $("#login_sendSmsCode").click(function(){
                                //获取手机号码
                                let tel=$("#login_telephone").val();
                                //向后台发送请求
                                $.ajax({
                                    url:"${pageContext.request.contextPath}/userController/valideCodeSMS",
                                    data:$("#login_telephone").attr("name")+"="+tel,
                                    dataType:"json",
                                    type:"get",
                                    success:function(response){
                                        let num=60;
                                        //短信发送成功
                                        if(response.success){
                                            //调用计时函数
                                            setInterval(function(){
                                                num--;
                                                if(num>0){
                                                    $("#login_sendSmsCode").attr("disabled",true).val("短信发送成功"+num+"s后可重新发送");
                                                }else{
                                                    $("#login_sendSmsCode").attr("disabled",false).val("发送手机验证码");
                                                    clearInterval();
                                                }
                                            },1000)
                                        }
                                    },
                                    error:function(){
                                        alert("服务器繁忙....")
                                    }
                                });
                            });
                        </script>
                        <div class="modal-footer">
							<span id="telLoginSpan" class="color:red"></span>
                            <input type="reset" class="btn btn-primary" value="重置">
                            <input type="button" class="btn btn-primary" id="telLogin" value="登录"/>
                        </div>
                    </form>
                </div>
                    <script>
                        <%--登录--%>
                        <%--点击事件--%>
                        $("#telLogin").click(function(){
                            //获取表单提交的参数
                            let smsLogin=$("#telLoginForm").serialize();
                            //ajax请求
                            $.ajax({
                                url:"${pageContext.request.contextPath}/userController/login4SMS",
                                data:smsLogin,
                                dataType:"json",
                                type:"post",
                                success:function(response){
                                    if(response.success){
                                        location.href="${pageContext.request.contextPath}/index.jsp";
                                    }else{
                                        $("#telLoginSpan").val(response.message);
                                    }
                                },
                                error:function(){
                                    alert("服务器繁忙....");
                                }
                            });
                        });
                    </script>
            </div>
        </div>
    </div>
</div>
