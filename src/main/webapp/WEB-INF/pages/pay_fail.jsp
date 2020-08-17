<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-payfail.css">
    <title>微信支付</title>

</head>
<body>
<!--引入头部-->
<%@include file="../../header.jsp"%>
<div class="container-fluid" >
    <div class="cart py-container">
        <!--主内容-->
        <div class="payfail" style="height: 500px">
            <div class="fail">
                <h3><img src="${pageContext.request.contextPath}/img/fail.png" width="28" height="28" style="display: inline">　支付失败，请稍后再试</h3>
                <div class="fail-text">
                    <p>失败原因：不能使用金币购买！</p>
                    <p>您可以先去　<a href="${pageContext.request.contextPath}/index.jsp" target="_self">黑马旅游网首页</a>　逛逛</p>
                    <p class="button"><a href="${pageContext.request.contextPath}/orderController/wxPay.jsp?oid=${unpayedOrder.oid}" class="sui-btn btn-xlarge btn-danger">重新支付</a></p>
                </div>
            </div>

        </div>
    </div>
</div>
<!--引入尾部-->
<%@include file="../../footer.jsp"%>
</body>
</html>
