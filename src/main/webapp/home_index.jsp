<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-seckillOrder.css">
    <title>个人信息</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>

<div class="container-fluid">
    <!--header-->
    <div id="account">
        <div class="py-container">
            <div class="yui3-g home">
                <!--左侧列表-->
                <%@include file="home_left.jsp"%>
                <!--右侧主内容-->
                <div class="yui3-u-5-6 order-pay">
                    <div class="body userInfo">
                        <ul class="sui-nav nav-tabs nav-large nav-primary ">
                            <li class="active"><a href="#one" data-toggle="tab">基本资料</a></li>
                            <li><a href="#two" data-toggle="tab">头像照片</a></li>
                        </ul>
                        <form id="userInfo" action="${pageContext.request.contextPath}/userServlet">
							<%--回显id到隐藏域--%>
                            <%--<input type="hidden" name="uid" value="">--%>
                            <%--方法隐藏域--%>
                            <%--<input type="hidden" name="action" value="updateInfo">--%>
                            <div class="tab-content ">
                                <div id="one" class="tab-pane active">
                                    <div class="sui-form form-horizontal">
                                        <div class="control-group">
                                            <label for="inputName" class="control-label">昵称：</label>
                                            <div class="controls">
                                                <input type="text" id="inputName" name="nickname" placeholder="昵称" value="${currentUser.nickname}">
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <label class="control-label">性别：</label>
                                            <div class="controls">
                                                <input id="man" type="radio" name="sex" value="1" <c:if test="${currentUser.sex==1}"> checked="checked"</c:if>><b>男</b>
                                                &nbsp;&nbsp;
                                                <input id="woman" type="radio" name="sex" value="0" <c:if test="${currentUser.sex==0}"> checked="checked"</c:if>><b>女</b>
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <label class="control-label">生日：</label>
                                            <div class="controls">
                                                <input id="birthday" type="text" name="birthday" placeholder="生日" value="${currentUser.birthday}">
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <label class="control-label">邮箱：</label>
                                            <div class="controls">
                                                <input id="email" type="text" name="email" placeholder="邮箱" value="${currentUser.email}">
                                            </div>
                                        </div>
                                        <div class="control-group">

                                            <div class="controls">
                                                <button id="userInfoUpdate" type="button" class="sui-btn btn-primary">更新</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%--用户个人信息更新--%>
                                <script>
                                   $("#userInfoUpdate").click(
                                        function () {
                                            //获取表单信息
                                            let formData=new FormData($("#userInfo")[0]);
//                                            console.log(formData.get("nickname"));
                                            $.ajax({
                                                url:"${pageContext.request.contextPath}/userController/userInfo",
                                                type:"post",
                                                data:formData,
                                                async:false,
                                                dataType:"json",
                                                processData:false,
                                                contentType:false,
                                                success:function(response){
                                                    if (!response.success) {
                                                        alert("请重新登陆....");
                                                    }else{
                                                        //回显昵称
                                                        $("#inputName").val(response.object.nickname);
                                                        //回显性别
                                                        if(response.object.sex==1){
                                                            $("#man").attr({"checked":"checked"})
                                                        }else{
                                                            $("#woman").attr({"checked":"checked"})
                                                        }
                                                        //回显生日
                                                        $("#birthday").val(response.object.birthday);
                                                        //回显邮箱
                                                        $("#email").val(response.object.email);
                                                        //回显头像
                                                        $("#imgShow_WU_FILE_0").attr("src","${pageContext.request.contextPath}"+response.object.pic)
                                                    }
                                                },
                                                error:function(){
                                                    alert("服务器繁忙...")
                                                }
                                            });
                                        }
                                    );
                                </script>
                                <div id="two" class="tab-pane">
                                    <div class="new-photo">
                                        <p>当前头像：</p>
                                        <div class="upload">
                                            <c:if test="${empty currentUser.pic}">
                                            <img id="imgShow_WU_FILE_0" width="100" height="100"
                                                 src="${pageContext.request.contextPath}/img/photo_icon.png"
                                                 alt="网络繁忙...">
                                            </c:if>
                                            <c:if test="${not empty currentUser.pic}">
                                                <img id="imgShow_WU_FILE_0" width="100" height="100"
                                                     src="${pageContext.request.contextPath}${currentUser.pic}"
                                                     alt="网络繁忙...">
                                            </c:if>
                                            <input type="file" id="up_img_WU_FILE_0" name="picFile"/>
                                        </div>
                                        <%--<script>--%>
                                            <%--$(function () {--%>
                                                <%--alert(${empty currentUser.pic})--%>
                                            <%--})--%>
                                        <%--</script>--%>
                                    </div>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<!--引入尾部-->
<%@include file="footer.jsp"%>

</body>
</html>
