<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#asyncBtn").click(function () {
            console.log("Ajax准备执行");
            $.ajax({
                "url": "test/ajax/async.html",
                "type": "post",
                "dataType": "text",
                "async": false,  // 关闭异步工作模型，使用同步方式工作。此时，所有操作在同一线程内顺序执行
                "success": function (response) {
                    // success是接收到服务端响应后执行
                    console.log("Ajax执行中" + response);
                }
            });
            // 在$.ajax()执行完成后执行，不等待success()函数
            console.log("ajax执行完毕");
        })
    });
</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <button id="asyncBtn">发送Ajax请求</button>
        </div>
    </div>
</div>

</body>
</html>

