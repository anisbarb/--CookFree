import React, { useState } from 'react';
import { Phone, MessageSquare, X, Send } from 'lucide-react';

export function ContactModal({ cook, onClose }) {
  const [messages, setMessages] = useState([
    { sender: 'cook', text: `Namaste! I have started preparing your order with fresh organic ingredients. It will be piping hot!` }
  ]);
  const [inputText, setInputText] = useState('');

  const handleSend = (e) => {
    e.preventDefault();
    if (!inputText.trim()) return;
    const newMsg = { sender: 'user', text: inputText };
    setMessages(prev => [...prev, newMsg]);
    setInputText('');
    setTimeout(() => {
      setMessages(prev => [...prev, { sender: 'cook', text: 'Got it! I will make sure it is packed securely.' }]);
    }, 1000);
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl max-w-md w-full shadow-2xl overflow-hidden border border-[#EFECE6] flex flex-col h-[500px]">
        
        {/* Header */}
        <div className="bg-[#D95338] text-white p-4 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <img src={cook.avatar} alt={cook.name} className="w-10 h-10 rounded-full object-cover border-2 border-white/80" />
            <div>
              <h3 className="font-bold text-sm">{cook.name}</h3>
              <p className="text-[11px] text-white/80">{cook.kitchen}</p>
            </div>
          </div>
          <div className="flex items-center space-x-2">
            <a href={`tel:+919876543210`} className="p-2 rounded-xl bg-white/20 hover:bg-white/30 transition-all" title="Call Cook">
              <Phone className="w-4 h-4" />
            </a>
            <button onClick={onClose} className="p-2 rounded-xl bg-white/20 hover:bg-white/30 transition-all">
              <X className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Chat Body */}
        <div className="flex-1 p-4 overflow-y-auto space-y-3 bg-[#FDFBF7]">
          {messages.map((m, idx) => (
            <div key={idx} className={`flex ${m.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
              <div className={`max-w-[80%] rounded-2xl px-4 py-2.5 text-xs leading-relaxed ${
                m.sender === 'user' ? 'bg-[#D95338] text-white rounded-br-none' : 'bg-white border border-[#EFECE6] text-[#2D2522] rounded-bl-none shadow-sm'
              }`}>
                {m.text}
              </div>
            </div>
          ))}
        </div>

        {/* Input */}
        <form onSubmit={handleSend} className="p-3 bg-white border-t border-[#EFECE6] flex items-center space-x-2">
          <input 
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            placeholder="Ask for less oil, extra spice..."
            className="flex-1 bg-[#FDFBF7] border border-[#EFECE6] rounded-xl px-4 py-2.5 text-xs focus:outline-none focus:border-[#D95338]"
          />
          <button type="submit" className="bg-[#D95338] text-white p-2.5 rounded-xl hover:bg-[#B83D24] transition-all">
            <Send className="w-4 h-4" />
          </button>
        </form>

      </div>
    </div>
  );
}
