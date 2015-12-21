<%-- 
    Document   : timer
    Created on : 2015/12/11, 下午 02:37:37
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <span id="messageGoesHere"></span>
        <script>
            var ws = new WebSocket("ws://localhost:8081/WSVersion2/clock");

            ws.onmessage = function (event) {
                var mySpan = document.getElementById("messageGoesHere");
                mySpan.innerHTML = event.data;
            };
            ws.onerror = function (event) {
                console.log("Error ", event);
            };
        </script>
    </body>
</html>
