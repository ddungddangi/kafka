import React, { useEffect, useState, useRef } from 'react';

export default function Chat() {
  const [connected, setConnected] = useState(false);
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);
  const wsRef = useRef(null);
  
  useEffect(() => {
    const ws = new WebSocket('ws://52.78.250.173:2222/ws');
    wsRef.current = ws;

    ws.onopen = () => {
      console.log("[Frontend] [1] [연결 완료] WebSocket 연결 성공 (Client → Backend)");
      setConnected(true);
    };

    ws.onmessage = (event) => {
      console.log("[Frontend] [2] [메시지 수신] 서버로부터 메시지 수신 (Backend → Client) | message=", event.data);
      setMessages(prev => [...prev, event.data]);
    };

    ws.onclose = () => {
      console.warn("[Frontend] [3] [연결 종료] WebSocket 연결 끊김 (Client → Backend)");
      setConnected(false);
      setTimeout(() => {
        console.log("[Frontend] [4] [재연결 준비] WebSocket 재연결 대기 중...");
        setMessages([]);
        setInput('');
      }, 1000);
    };

    ws.onerror = (err) => {
      console.error("[Frontend] [Error] WebSocket 오류 발생 | error=", err);
    };

    return () => {
      ws.close();
      console.warn("[Frontend] [5] [연결 종료] 컴포넌트 언마운트 - WebSocket 연결 해제");
    };
  }, []);

  const sendMessage = () => {
    if (!connected || input.trim() === "") return;
    wsRef.current.send(input);
    console.log("[Frontend] [6] [메시지 전송] 클라이언트에서 서버로 메시지 전송 (Client → Backend) | message=", input);
    setInput("");
  };

  return (
    <div style={{
      maxWidth: 500,
      margin: '50px auto',
      background: '#fff',
      borderRadius: 10,
      boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
      padding: 20,
      display: 'flex',
      flexDirection: 'column',
      height: '80vh'
    }}>
      <h2 style={{ textAlign: 'center', marginBottom: 20 }}>Fine Chat</h2>
      <div
        style={{
          flex: 1,
          border: '1px solid #ddd',
          borderRadius: 10,
          padding: 10,
          overflowY: 'auto',
          marginBottom: 10,
          backgroundColor: '#f9f9f9'
        }}
      >
        {messages.map((msg, i) => (
          <div
            key={i}
            style={{
              display: 'flex',
              justifyContent: msg.startsWith('User:') ? 'flex-start' : 'flex-end',
              marginBottom: 8,
            }}
          >
            <div style={{
              background: msg.startsWith('User:') ? '#4F8EF7' : '#e5e5ea',
              color: msg.startsWith('User:') ? 'white' : '#000',
              padding: '8px 12px',
              borderRadius: 20,
              fontSize: '14px', // 글씨 크기 30% 줄임
              maxWidth: '70%',
              wordBreak: 'break-word',
              display: 'inline-block',
            }}>
              {msg}
            </div>
          </div>
        ))}
      </div>
      <div style={{ display: 'flex' }}>
        <input
          type="text"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && sendMessage()}
          style={{
            flex: 1,
            padding: '10px',
            borderRadius: 20,
            border: '1px solid #ccc',
            marginRight: 10,
            outline: 'none',
            fontSize: '14px'
          }}
          placeholder="Type a message..."
        />
        <button
          onClick={sendMessage}
          disabled={!connected}
          style={{
            backgroundColor: connected ? '#4F8EF7' : '#ccc',
            color: 'white',
            border: 'none',
            borderRadius: 20,
            padding: '10px 20px',
            cursor: connected ? 'pointer' : 'not-allowed',
            fontSize: '14px'
          }}
        >
          Send
        </button>
      </div>
    </div>
  );
}
