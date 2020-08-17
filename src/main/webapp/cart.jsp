<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>购物车列表</title>


</head>
<body>
<!--引入头部-->
<%@include file="header.jsp"%>
<div class="container">
    <c:if test="${empty cart||cart.total==0}">
    <div class="row" style="margin: 100px 200px;text-align: center">
        购物车内暂时没有商品，登录后将显示您之前加入的商品
    </div>
    </c:if>
    <c:if test="${not empty cart && cart.total>0}">
    <div class="row">
        <div style="margin:0 auto; margin-top:20px">
            <div style="font-weight: bold;font-size: 15px;margin-bottom: 10px">商品数量：${cart.total}</div>
            <table class="table">
                <tbody>
                <tr bgcolor="#f5f5f5" class="table-bordered">
                    <th>图片</th>
                    <th>商品</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>小计</th>
                    <th>操作</th>
                    <th></th>
                </tr>
                <c:forEach items="${cart.cart}" var="commodity" >
                <tr class="table-bordered">
                    <td width="180" width="40%">
                        <input type="hidden" name="id" value="22">
                        <img src="${pageContext.request.contextPath}/${commodity.route.rimage}" width="170" height="100">
                    </td>
                    <td width="30%">
                        <a href="${pageContext.request.contextPath}/routeController/routeDetail?rid=${commodity.rid}" target="_self">${commodity.route.routeIntroduce}</a>
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
                    <td>
                        <a href="javascript:deleteCart();" class="delete">删除</a>
                    </td>
                    <%--删除购物车--%>
                    <script>
                        function deleteCart(){
                            if(${empty currentUser}){
                                $("#loginBtn").click();
                            }else{
                                location.href="${pageContext.request.contextPath}/routeController/deleteCart?rid=${commodity.rid}"
                            }
                        }
                    </script>
                    <td>
                        <input type="checkbox" name="commodity" value="${commodity.rid}" price="${commodity.subCost}" onclick="addOrder(${commodity.rid})">
                    </td>
                </tr >
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div>
        <div style="text-align:right;">
             商品金额: <strong style="color:#ff6600;" id="strong">￥0元</strong>
        </div>
        <div style="text-align:right;margin-top:10px;margin-bottom:10px;">
                <a href="javascript:void(0)">
					<input id="cartSubmit" type="button" width="100" value="结算" name="submit" border="0" style="background-color: #ea4a36;
						height:45px;width:120px;color:white;font-size: 15px">
                </a>
        </div>
    </div>
    </c:if>
    <%--购物车选择--%>
    <script>
        function addOrder(rid) {
            let totalPrice=0;
            let subPrice=0;
            //对购物进行遍历
            $("input[name='commodity']").each(function(index,ele){
                if($(ele).is(":checked")){
                    subPrice=$(ele).attr("price")*1;
                    totalPrice=totalPrice+subPrice;
                }
            });
            $("#strong").text("￥"+totalPrice+"元");
        }
        $(function () {
            let totalPrice=0;
            let subPrice=0;
            //对购物进行遍历
            $("input[name='commodity']").each(function(index,ele){
                if($(ele).is(":checked")){
                    subPrice=$(ele).attr("price")*1;
                    totalPrice=totalPrice+subPrice;
                }
            });
            $("#strong").text("￥"+totalPrice+"元");
        })
    </script>
    <%--提交购物车--%>
    <script>
        $("#cartSubmit").click(function(){
            let rids=new Array();
            let i=0;
            //判断用户登录状态
            if(${empty currentUser}){
                $("#loginBtn").click();
            }else{
            //遍历购物车
                $("input[name='commodity']").each(function(index,ele){
                    //判断被选中的商品
                    if($(ele).is(":checked")){
                        rids[i]=$(ele).val();
                        i++;
                    }
                })
            }
            //判断当前购物车选中的商品
            if(rids.length>0){
                location.href="${pageContext.request.contextPath}/routeController/selectedCart?rids="+rids;
            }
        });
    </script>
</div>
<!--引入尾部-->
<%@include file="footer.jsp"%>
</body>
</html>
