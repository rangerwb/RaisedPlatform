<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>尚筹网</title>
</head>
<body>
<div id="editModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <form action="admin/do/login.html" method="post" class="form-signin" role="form">
                    <%--<h2 class="form-signin-heading">--%>
                    <%--    <i class="glyphicon glyphicon-log-in"></i> 管理员登录--%>
                    <%--</h2>--%>
                    <div class="form-group has-success has-feedback">
                        <input type="text" name="roleName" class="form-control" placeholder="请输入角色名称"
                               autofocus>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="updateRoleBtn" type="button" class="btn btn-success">更新</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</body>
</html>
