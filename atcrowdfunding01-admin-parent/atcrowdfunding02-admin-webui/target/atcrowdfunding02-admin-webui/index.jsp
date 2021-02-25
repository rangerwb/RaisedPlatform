<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>index</title>
    <%-- http://localhost:8080/atcrowdfunding02_admin_webui_war/test/ssm.html --%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <%-- 注意：
            1. 端口号前面的冒号不能省略
            2. contextPath前面不能写"/"
            3. contextPath后面必须写"/"
            4. 页面上参照base标签的标签必须放在base标签后面
            5. 页面上所有参照base标签的标签的路径都不能以"/"开头
    --%>
    <script type="text/javascript" src="static/jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="static/layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {
                $.ajax({
                    "url": "send/array/one.html",               //请求目标资源的地址
                    "type": "post",                             //请求方式
                    "data": {                                   //要发送的请求参数
                        "array": [2, 5, 8]
                    },
                    "dataType": "text",                         //如何对待服务器返回的数据
                    "success": function (response) {            //服务器处理请求成功后调用的回调函数，response是响应体数据
                        alert(response);
                    },
                    "error": function (response) {              //服务器处理请求失败后调用的回调函数，response是响应体数据
                        alert(response);
                    }
                });
            });

            $("#btn2").click(function () {
                $.ajax({
                    "url": "send/array/two.html",               //请求目标资源的地址
                    "type": "post",                             //请求方式
                    "data": {                                   //要发送的请求参数
                        "array[0]": 2,
                        "array[1]": 5,
                        "array[2]": 8
                    },
                    "dataType": "text",                         //如何对待服务器返回的数据
                    "success": function (response) {            //服务器处理请求成功后调用的回调函数，response是响应体数据
                        alert(response);
                    },
                    "error": function (response) {              //服务器处理请求失败后调用的回调函数，response是响应体数据
                        alert(response);
                    }
                });
            });

            $("#btn3").click(function () {
                // 准备数组
                var array = [2, 5, 8];
                // 将json数组转成json字符串
                var requestBody = JSON.stringify(array);

                $.ajax({
                    "url": "send/array/three.html",               //请求目标资源的地址
                    "type": "post",                               //请求方式
                    "data": requestBody,                          //请求体
                    "contentType": "application/json; charset=UTF-8",   //请求体的内容类型，告诉服务器端本次请求的请求体是JSON数据
                    "dataType": "text",                           //如何对待服务器返回的数据
                    "success": function (response) {              //服务器处理请求成功后调用的回调函数，response是响应体数据
                        alert(response);
                    },
                    "error": function (response) {                //服务器处理请求失败后调用的回调函数，response是响应体数据
                        alert(response);
                    }
                });
            });

            $("#btn4").click(function () {
                // 准备数据
                var student = {
                    stuId: 5,
                    stuName: "tom",
                    address: {
                        province: "广东",
                        city: "深圳",
                        street: "后瑞"
                    },
                    subjects: [
                        {
                            subjectName: "JavaSE",
                            subjectScore: 100
                        }, {
                            subjectName: "SSM",
                            subjectScore: 99
                        }
                    ],
                    "map": {
                        k1: "v1",
                        k2: "v2"
                    }
                };
                // 将json数组转成json字符串
                var requestBody = JSON.stringify(student);

                //发送Ajax请求
                $.ajax({
                    "url": "send/compose/object.json",               //请求目标资源的地址
                    "type": "post",                                  //请求方式
                    "data": requestBody,                             //请求体
                    "contentType": "application/json; charset=UTF-8",   //请求体的内容类型，告诉服务器端本次请求的请求体是JSON数据
                    "dataType": "json",                              //如何对待服务器返回的数据
                    "success": function (response) {                 //服务器处理请求成功后调用的回调函数，response是响应体数据
                        console.log(response);
                        alert("成功");
                    },
                    "error": function (response) {                   //服务器处理请求失败后调用的回调函数，response是响应体数据
                        console.log(response);
                        alert("失败");
                    }
                });
            });

            $("#btn5").click(function () {
                layer.msg("layer的弹框");
            })
        })
    </script>
</head>
<body>
    <a href="test/ssm.html">测试ssm整合环境</a>
    <br/>
    <br/>
    <button id="btn1">Send [2,5,8] One</button>
    <br/>
    <br/>
    <button id="btn2">Send [2,5,8] Two</button>
    <br/>
    <br/>
    <button id="btn3">Send [2,5,8] Three</button>
    <br/>
    <br/>
    <button id="btn4">Send 复杂对象</button>
    <br/>
    <br/>
    <button id="btn5">点我弹框</button>
</body>
</html>
