const ws = new WebSocket("ws://chat.local/chat"); // Ingress 도메인 사용

ws.onmessage = (event) => {
  setMessages(prev => [...prev, event.data]); // 메시지 수신 시 출력
};

ws.send(input); // 메시지 전송
