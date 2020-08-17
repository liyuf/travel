<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-seckillOrder.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pages-getOrderInfo.css">
    <title>未完成的订单</title>

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
                    <div class="body">
                        <div class="table-title">
                            <table class="sui-table  order-table">
                                <tr>
                                    <thead>
                                    <th width="18%">图片</th>
                                    <th width="15%">商品</th>
                                    <th width="15%">单价</th>
                                    <th width="10%">数量</th>
                                    <th width="10%">支付金额</th>
                                    <th width="8%"></th>
                                    </thead>
                                </tr>
                            </table>
                        </div>
                        <form action="${pageContext.request.contextPath}/orderController/wxPay" method="post">
                        <div class="order-detail">
                            <div class="orders">
-                                <c:forEach items="${orders.list}" var="order">
                                <!--order1-->
                                <div class="choose-title">
                                    <label>
                                        <span>${order.ordertime}　订单编号：${order.oid}  店铺：哇哈哈 <a>和我联系</a></span>&nbsp;&nbsp;&nbsp;<input type="radio" name="oid" value="${order.oid}" onclick="makePay('${order.oid}')"/>
                                    </label>
                                    <a class="sui-btn btn-info share-btn">分享</a>
                                </div>
                                <table class="sui-table table-bordered order-datatable">
                                    <tbody>
                                    <c:forEach items="${order.orderItems}" var="orderItem">
                                    <tr>
                                        <td width="15%">
                                            <div class="typographic">
                                                <img src="${pageContext.request.contextPath}/${orderItem.route.rimage}" width="150" height="80">
                                            </div>
                                        </td>
                                        <td width="35%">
                                            <div>
                                                <a href="#" class="block-text">${orderItem.route.routeIntroduce}</a>
                                            </div>
                                        </td>
                                        <td width="5%" class="center">
                                            <ul class="unstyled">
                                                <li>¥${orderItem.route.price}</li>
                                            </ul>
                                        </td>
                                        <td width="5%" class="center">${orderItem.num}</td>
                                        <td width="10%" class="center" >
                                            <ul class="unstyled">
                                                <li>¥${orderItem.subtotal}</li>
                                            </ul>
                                        </td>
                                        <td width="8%" class="center">
                                            <input type="hidden" name="unpayedOrderItem${order.oid}" value="${orderItem.itemid}" price="${orderItem.subtotal}" num="${orderItem.num}">
                                        </td>
                                    </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                </c:forEach>
                            </div>
                            <div class="clearfix"></div>
                        </div>

                        <div style="margin-top: 50px">
                            <div class="page_num_inf">
                                <i></i> 共
                                <span>${orders.totalPages}</span>页<span>${orders.total}</span>条
                            </div>
                            <div class="pageNum">
                                <ul>
                                    <c:if test="${orders.currentPage!=1}">
                                        <li><a href="${pageContext.request.contextPath}/orderController/showOrders?currentPage=1&count=4&uid=${currentUser.uid}">首页</a></li>
                                        <li class="threeword"><a href="${pageContext.request.contextPath}/orderController/showOrders?currentPage=${orders.currentPage-1}&count=4&uid=${currentUser.uid}">上一页</a></li>
                                    </c:if>
                                    <c:forEach begin="${orders.start}" end="${orders.end}" var="i" step="1">
                                    <li class="curPage"><a href="${pageContext.request.contextPath}/orderController/showOrders?currentPage=${i}&count=4&uid=${currentUser.uid}">${i}</a></li>
                                    </c:forEach>
                                    <c:if test="${orders.currentPage!=orders.totalPages and orders.totalPages>0 }">
                                    <li class="threeword"><a href="${pageContext.request.contextPath}/orderController/showOrders?currentPage=${orders.currentPage+1}&count=4&uid=${currentUser.uid}">下一页</a></li>
                                    <li class="threeword"><a href="${pageContext.request.contextPath}/orderController/showOrders?currentPage=${orders.totalPages}&count=4&uid=${currentUser.uid}">末页</a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                            <div class="clearfix trade">
                                <div class="fc-price">
                                    <span id="commodityNum" class="number">0</span>件商品，应付金额:　<span id="commodityPrice" class="price">¥0</span>
                                </div>
                            </div>
                            <%--商品数量--%>
                            <script>
                                let total=0;
                                let num=0;
                                function makePay(oid) {
                                    total=$("input[name='unpayedOrderItem"+oid+"']").attr("price")*1;
                                    num=$("input[name='unpayedOrderItem"+oid+"']").attr("num")*1;
                                    $("#commodityNum").text(num);
                                    $("#commodityPrice").text("￥"+total);
                                }
                            </script>
                            <div class="submit">
                                <button class="sui-btn btn-danger btn-xlarge" >提交订单</button>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/citypicker/distpicker.data.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/citypicker/distpicker.js"></script>
</body>
</html>
