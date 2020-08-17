<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/webbase.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages-seckillOrder.css">
    <title>地址管理</title>

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
                    <div class="body userAddress">
                        <div class="address-title">
                            <span class="title">地址管理</span>
                            <a data-toggle="modal" data-target="#addAddressModel" data-keyboard="false"   class="sui-btn  btn-info add-new">添加新地址</a>
                            <span class="clearfix"></span>
                        </div>
                        <div class="address-detail">
                            <table id="addressTable" class="sui-table table-bordered">
                                <thead>
                                <tr>
                                    <th>姓名</th>
                                    <th>地址</th>
                                    <th>联系电话</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody id="addressList">
                                <c:forEach items="${addressList}" var="address" varStatus="status">
                                <tr>
                                    <td>${address.contact}</td>
                                    <td>${address.address}</td>
                                    <td>${address.telephone}</td>
                                    <td>
                                        <a data-toggle="modal" data-target="#editAddressModel" data-keyboard="false"   class="sui-btn  btn-info add-new" data-orderId="${status.count}" data-aid="${address.aid}">编辑</a>
                                        <a href="#" onclick="deleteAddress(${address.aid})">删除</a>
                                        <c:if test="${address.isdefault==1}">
                                            <input type="button" onclick="defaultAddress(${address.aid})" disabled="disabled" value="设为默认" style='font-size: 15px'/>
                                        </c:if>
                                        <c:if test="${address.isdefault==0}">
                                            <input type="button" onclick="defaultAddress(${address.aid})" value="设为默认" style='font-size: 15px;color: #337ab7'/>
                                        </c:if>
                                    </td>
                                </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <%--定义回显函数--%>
                        <script>
                            function showBackAddress(resp) {
                                    //清空标签
                                    $("#addressList").empty();
                                    for(var i=0;i<resp.length;i++){
                                        //判断默认
                                        if(resp[i].isdefault==1){
                                            //添加标签
                                            $("#addressList").append(" <tr>\n" +
                                                "<td>"+resp[i].contact+"</td>\n" +
                                                "<td>"+resp[i].address+"</td>\n" +
                                                "<td>"+resp[i].telephone+"</td>\n" +
                                                "<td>\n" +
                                                "<a data-toggle=\"modal\" data-target=\"#editAddressModel\" data-keyboard=\"false\" class=\"sui-btn  btn-info add-new\" data-orderId=\""+i+"\" data-aid=\""+resp[i].aid+"\">编辑</a>\n" +
                                                "<a href=\"#\" onclick=\"deleteAddress("+resp[i].aid+")\">删除</a>\n" +
                                                "<input  type='button' onclick=\"defaultAddress("+resp[i].aid+")\" disabled='disabled' value='设为默认' style='font-size: 15px'/>\n" +
                                                "</td>\n" +
                                                "</tr>");
                                        }else{
                                            //添加标签
                                            $("#addressList").append(" <tr>\n" +
                                                "<td>"+resp[i].contact+"</td>\n" +
                                                "<td>"+resp[i].address+"</td>\n" +
                                                "<td>"+resp[i].telephone+"</td>\n" +
                                                "<td>\n" +
                                                "<a data-toggle=\"modal\" data-target=\"#editAddressModel\" data-keyboard=\"false\" class=\"sui-btn  btn-info add-new\" data-orderId=\""+i+"\" data-aid=\""+resp[i].aid+"\">编辑</a>\n" +
                                                "<a href=\"#\" onclick=\"deleteAddress("+resp[i].aid+")\">删除</a>\n" +
                                                "<input type='button' onclick=\"defaultAddress("+resp[i].aid+")\" value='设为默认' style='font-size: 15px;color: #337ab7'/>\n" +
                                                "</td>\n" +
                                                "</tr>");
                                        }
                                }
                            }
                        </script>
                        <%--删除--%>
                        <script>
                            function deleteAddress(aid) {
                                //获取aid
                                $.ajax({
                                    url:"${pageContext.request.contextPath}/userController/deleteAddress",
                                    data:"aid="+aid,
                                    dataType:"json",
                                    type:"get",
                                    success:showBackAddress,
                                    error:function () {
                                        alert("服务器繁忙...")
                                    }
                                });
                            }
                        </script>
                        <%--设置默认--%>
                        <script>
                            function defaultAddress(aid){
                                //ajax
                                $.ajax({
                                    url:"${pageContext.request.contextPath}/userController/setDefault",
                                    data:"aid="+aid,
                                    type:"get",
                                    success:showBackAddress,
                                    error:function () {
                                        alert("服务器繁忙...")
                                    }
                                });
                            }
                        </script>

                        <!-- 地址模态框 -->
                        <div class="modal fade" id="addAddressModel" tabindex="-1" role="dialog" aria-labelledby="loginModelLable">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <%-- 新增地址--%>
                                    <div class="tab-pane fade in active" >
                                        <form id="addAddress" action="${pageContext.request.contextPath}/userController/addAddress" method="post">
                                            <input type="hidden" name="action" value="xxx">
                                            <div class="modal-body">
                                                <div class="form-group">
                                                    <label>姓名1</label>
                                                    <input type="text" class="form-control" name="contact"
                                                           placeholder="姓名">
                                                </div>
                                                <div class="form-group">
                                                    <label>地址</label>
                                                    <input type="text" class="form-control" name="address"
                                                           placeholder="请输入地址">
                                                </div>
                                                <div class="form-group">
                                                    <label>联系电话</label>
                                                    <input type="text" class="form-control" name="telephone"
                                                           placeholder="联系电话">
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="button" class="btn btn-default" data-dismiss="modal"  value="关闭">
                                                <input id="addressSubmit" type="submit" class="btn btn-primary" value="保存"/>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--编辑地址框模态--%>
                        <div class="modal fade" id="editAddressModel" tabindex="-1" role="dialog" aria-labelledby="loginModelLable">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <%-- 新增地址--%>
                                    <div class="tab-pane fade in active" >
                                        <form id="editAddress" action="${pageContext.request.contextPath}/userController/updateAddress" method="post">
                                            <input type="hidden" id="addressId" name="aid" value="xxx">
                                            <div class="modal-body">
                                                <div class="form-group">
                                                    <label>姓名</label>
                                                    <input id="contact" type="text" class="form-control" name="contact"
                                                           placeholder="姓名">
                                                </div>
                                                <div class="form-group">
                                                    <label>地址</label>
                                                    <input id="address" type="text" class="form-control" name="address"
                                                           placeholder="请输入地址">
                                                </div>
                                                <div class="form-group">
                                                    <label>联系电话</label>
                                                    <input id="telephone" type="text" class="form-control" name="telephone"
                                                           placeholder="联系电话">
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="button" class="btn btn-default" data-dismiss="modal"  value="关闭">
                                                <input type="submit" class="btn btn-primary" value="保存"/>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <script>
                            $('#editAddressModel').on('show.bs.modal', function (event) {
                                //获取按钮
                                let button=$(event.relatedTarget);
                                let rowId=button.data('orderid');//获取a标签的所要传递的值
                                let addressId=button.data('aid');
//                                alert(addressId)
//                                alert(rowId);
//                                alert(document.getElementById("addressTable").rows[rowId].cells[2].innerText)
                                $("#contact").val(document.getElementById("addressTable").rows[rowId].cells[0].innerText);
                                $("#address").val(document.getElementById("addressTable").rows[rowId].cells[1].innerText);
                                $("#telephone").val(document.getElementById("addressTable").rows[rowId].cells[2].innerText);
                                $("#addressId").val(addressId);
                                });
                        </script>
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
