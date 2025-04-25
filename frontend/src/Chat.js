import React, { useEffect, useState, useRef } from 'react';

export default function Chat() {
  const [connected, setConnected] = useState(false);
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([]);
  const wsRef = useRef(null);

  useEffect(() => {
    const ws = new WebSocket('ws://211.183.3.11:8080/ws');
    wsRef.current = ws;

    ws.onopen = () => {
      console.log('WebSocket connected');
      setConnected(true);
    };

    ws.onmessage = (event) => {
      setMessages(prev => [...prev, event.data]);
      console.log("받은 메시지:", event.data);
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
      setConnected(false);
      // 자동 재연결 예시
      setTimeout(() => {
        setMessages([]);
        setInput('');
      }, 1000);
    };

    ws.onerror = (err) => {
      console.error('WebSocket error', err);
    };

    return () => ws.close();
  }, []);

  const sendMessage = () => {
    if (!connected || input.trim() === "") return;
    wsRef.current.send(input);
    setInput("");
  };
  

  return (
    <div style={{ maxWidth: 400, margin: '0 auto' }}>
      <h2>Chat Room</h2>
      <div
        style={{
          border: '1px solid #ccc',
          height: 300,
          overflowY: 'auto',
          padding: 8,
          marginBottom: 8
        }}
      >
        {messages.map((msg, i) => (
          <div key={i} style={{ margin: '4px 0' }}>{msg}</div>
        ))}
      </div>
      <input
        type="text"
        value={input}
        onChange={e => setInput(e.target.value)}
        onKeyDown={e => e.key === 'Enter' && sendMessage()}
        style={{ width: '80%', padding: 4 }}
        placeholder="Type a message..."
      />
      <button onClick={sendMessage} disabled={!connected}>Send</button>
    </div>
  );
}
