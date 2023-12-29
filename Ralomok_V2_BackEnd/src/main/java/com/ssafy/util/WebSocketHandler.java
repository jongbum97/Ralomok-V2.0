package com.ssafy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.game.model.Message;
import com.ssafy.word.model.WordDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler{
	
	private NaverDictionaryApi dictionary = new NaverDictionaryApi();

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private final Map<String, String> names = new ConcurrentHashMap<>();
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 소켓 연결시 실행
		log.debug("연결"+session.toString());
		sessions.put(session.getId(), session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 데이터 통신시 실행
		Message mm = mapper.readValue(message.getPayload(), Message.class);
		log.debug(mm.toString());
		
		if(mm.getName().equals("config")) {
			names.put(session.getId(), mm.getData());
			log.debug(names.toString());
			sessions.values().forEach( s -> { 
				send(s, new Message("System", names.get(session.getId())+"님이 입장했습니다.")); 
			});
			return;
		}
		sessions.values().forEach( s -> { send(s, mm); });
		
		String response = dictionary.searchDictionary(mm.getData());
		Map<String, Object> map = mapper.readValue(response, Map.class);
		log.debug(map.toString());
		List<Object> list = (ArrayList<Object>) map.get("items");
		log.debug(list.toString());
		List<WordDto> words = new ArrayList<>();
		for (Object object : list) {
			Map<String, String> tmp = (Map<String, String>) object;
			words.add(((WordDto) tmp));
		}
	}

	@Override
	public synchronized void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 연결 종료시 실행
		log.debug("연결종료"+names.get(session.getId()));
		sessions.values().forEach( s -> { 
			if(s.getId()!=session.getId())
			send(s, new Message("System", names.get(session.getId())+"님이 퇴장했습니다."));
		});
		names.remove(session.getId());
		sessions.remove(session.getId());
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 에러 처리
		log.error(exception.getMessage());
	}

	private void send(WebSocketSession s , Message m) {
		try {
			s.sendMessage(new TextMessage(mapper.writeValueAsString(m)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
