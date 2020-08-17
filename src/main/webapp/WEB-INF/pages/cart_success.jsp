<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>添加购物车成功</title>


</head>
<body>
<!--引入头部-->
<%@include file="../../header.jsp"%>
<div class="container">
    <div style="margin:0 auto; margin-top:20px">
        <div style="font-weight: bold;font-size: 15px;margin-bottom: 10px">添加购物车成功...</div>
        <table class="table">
            <tbody>
            <tr bgcolor="#f5f5f5" class="table-bordered">
                <th>图片</th>
                <th>商品</th>
                <th>价格</th>
                <th>数量</th>
                <th>小计</th>
            </tr>
            <tr class="table-bordered">
                <td width="180" width="40%">
                    <input type="hidden" name="id" value="22">
                    <img src="${pageContext.request.contextPath}/${commodity.route.rimage}" width="170" height="100">
                </td>
                <td width="30%">
                    <a href="${pageContext.request.contextPath}/routeController/routeDetail?rid=${commodity.route.rid}">${commodity.route.rname}</a>
                </td>
                <td width="10%">
                    ￥${commodity.route.price}
                </td>
                <td width="14%">
                    ×${commodity.num}
                </td>
                <td width="15%">
                    <span class="subtotal">￥${commodity.subCost}</span>
                </td>
            </tr >

            </tbody>
        </table>
    </div>


</div>
<!--引入尾部-->
<%@include file="../../footer.jsp"%>
</body>
</html>
