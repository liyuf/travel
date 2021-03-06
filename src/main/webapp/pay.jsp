<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="${pageContext.request.contextPath}/js/qrcode.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-weixinpay.css">
    <title>微信支付</title>

</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>
<div class="container-fluid">
    <div class="cart py-container">
        <!--主内容-->
        <div class="checkout py-container  pay">
            <div class="checkout-tit">
                <h4 class="fl tit-txt"><span class="success-icon"></span><span  class="success-info">订单提交成功，请您及时付款！订单号：${order.object.oid}</span></h4>
                <span class="fr"><em class="sui-lead">应付金额：</em><em  class="orange money">￥${order.object.total}</em>元</span>
                <div class="clearfix"></div>
            </div>
            <div class="checkout-steps">
                <div class="fl weixin">微信支付</div>
                <div class="fl sao">
                    <p class="red" style="padding-bottom: 40px">二维码已过期，刷新页面重新获取二维码。</p>
                    <div class="fl code">
                        <%--<img src="${pageContext.request.contextPath}/img/erweima.png" alt="">--%>
                            <%--支付链接--%>
                            <div id="qrCode"></div>
                            <script>
                                    //二维码对象
                                    let qrCode=new QRCode("qrCode","${url}")
                            </script>
                        <div class="saosao">
                            <p>请使用微信扫一扫</p>
                            <p>扫描二维码支付</p>
                        </div>
                    </div>
                    <div class="fl" style="background:url(${pageContext.request.contextPath}/img/phone-bg.png) no-repeat;width:350px;height:400px;margin-left:40px">

                    </div>

                </div>
                <div class="clearfix"></div>
            </div>
        </div>

    </div>
</div>
<script>
    setInterval(function () {
        $.ajax({
            url:"${pageContext.request.contextPath}/orderController/orderState",
            data:"oid=${order.object.oid}",
            type:"post",
            dataType:"json",
            success:function (resp) {
                if (resp.success) {
                    location.href="${pageContext.request.contextPath}/orderController/paySuccess?oid=${order.object.oid}"
                }
            }
        });
    },5000);
    setTimeout(function () {
        location.href="${pageContext.request.contextPath}/orderController/payfail?oid=${order.object.oid}";
    },600000)
</script>
<!--引入尾部-->
<%@include file="footer.jsp"%>
</body>
</html>
