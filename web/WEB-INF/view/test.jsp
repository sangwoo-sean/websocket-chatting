<%--
  Created by IntelliJ IDEA.
  User: Woody
  Date: 2021-08-21
  Time: 오전 9:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js"></script>
    <title>Title</title>
</head>
<body>
    <h1>Chat Room</h1>
    <h1>ID : ${user_name}</h1>
    <input type="text" id="msg-input" onKeypress="keyHandler();">
    <button type="button" onclick="sendMessage($('#msg-input').val())">전송</button>

    <div id="chat-box">

    </div>


<script>
    $(document).ready(function () {
        connect();
    });

    function keyHandler() {
        if(event.keyCode==13) {sendMessage($('#msg-input').val())}
    }

    let websocket;

    function connect() {
        // 웹소켓 주소
        var wsUri = "ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/chat.do";
        // 소켓 객체 생성
        websocket = new WebSocket(wsUri);
        //웹 소켓에 이벤트가 발생했을 때 호출될 함수 등록
        websocket.onopen = onOpen;
        websocket.onmessage = onMessage;
    }

    //웹 소켓에 연결되었을 때 호출될 함수
    function onOpen() {
        console.log("연결");
        const data = {
            "roomId" : 1,
            "name" : "${user_name}",
            "message" : "ENTER-CHAT"
        };
        let jsonData = JSON.stringify(data);
        websocket.send(jsonData);
        console.log(websocket);
    }

    // 메시지 전송
    function sendMessage(message){
        const data = {
            "roomId" : 1,
            "name" : "${user_name}",
            "message" : message
        };

        let jsonData = JSON.stringify(data);

        websocket.send(jsonData);
        $('#msg-input').val('');
    }

    // 메세지 수신
    function onMessage(event) {
        let data = JSON.parse(event.data);
        console.log("====");
        console.log(event.data);
        console.log(data);

        let elem = document.createElement('li');
        if (data.message === "ENTER-CHAT") {
            elem.innerHTML = data.name + '님이 채팅방에 입장하셨습니다.';
        } else if (data.message === "LEFT-CHAT") {
            elem.innerHTML = data.name + '님이 채팅방을 나갔습니다.';
        } else {
            elem.innerHTML = data.name + ' : ' + data.message;
        }
        $('#chat-box').append(elem);

    }

</script>
</body>
</html>
