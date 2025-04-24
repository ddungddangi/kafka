import React, { useState, useEffect, useRef } from 'react';

function ChatRoom() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const ws = useRef(null);

  useEffect(() => {
    ws.current = new WebSocket("ws://chat-backend:8080/chat"); // 백엔드 주소로 수정
    ws.current.onmessage = (event) => {
      setMessages((prev) => [...prev, event.data]);
    };
    return () => {
      ws.current.close();
    };
  }, []);

  const sendMessage = () => {
    if (input.trim() !== '') {
      ws.current.send(input);
      setInput('');
    }
  };

  return (
    <div>
      <h2>실시간 채팅방</h2>
      <div style={{ border: '1px solid gray', height: '300px', overflowY: 'scroll', marginBottom: '10px' }}>
        {messages.map((msg, idx) => (
          <div key={idx}>{msg}</div>
        ))}
      </div>
      <input
        type="text"
        value={input}
        placeholder="메시지를 입력하세요"
        onChange={(e) => setInput(e.target.value)}
        onKeyDown={(e) => { if (e.key === 'Enter') sendMessage(); }}
      />
      <button onClick={sendMessage}>전송</button>
    </div>
  );
}

export default ChatRoom;

