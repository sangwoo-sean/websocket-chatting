package main.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/chat.do")
@Controller
public class ChatHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        System.out.println("afterConnectionEstablished");
        sessionList.add(webSocketSession);
        System.out.println(webSocketSession.getId() + "님이 입장하셨습니다."); // 세션으로 유저 아이디 넣어주면 됨
        // 이전채팅을 보여주고싶으면 DB에 저장되어있는 채팅을 불러오기
    }

    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
        System.out.println("handleMessage");
        Map<String, Object> mapping = messageBinding(message);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", mapping.get("name"));
        jsonObject.put("message", mapping.get("message"));

        // 이전채팅 보여주고싶으면 DB에 채팅을 저장
        System.out.println("현재 채팅방 인원 : " + sessionList.size());
        for (WebSocketSession s : sessionList) { // 접속되어있는 모든사람에게 메세지 전송
            s.sendMessage(new TextMessage(jsonObject.toString()));
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("afterConnectionClosed");
        sessionList.remove(webSocketSession);
        System.out.println(webSocketSession.getId() + "님이 퇴장하셨습니다.");
        System.out.println("현재 채팅방 인원 : " + sessionList.size());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", webSocketSession.getId());
        jsonObject.put("message", "LEFT-CHAT");
        for (WebSocketSession s : sessionList) {
            s.sendMessage(new TextMessage(jsonObject.toString()));
        }
    }

    private Map<String, Object> messageBinding(TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> mapping = new ObjectMapper().readValue(payload, typeRef);
        return mapping;
    }

}
