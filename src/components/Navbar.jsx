import React from 'react';
import { Utensils, MapPin, ChefHat, Bell, ShoppingBag, ShieldCheck, User } from 'lucide-react';

export function Navbar({ activeTab, setActiveTab, location, setLocation, activeOrderCount, isCookMode, setIsCookMode }) {
  const locations = ['Gachibowli', 'Madhapur', 'Hitec City', 'Jubilee Hills', 'Banjara Hills', 'Kondapur'];

  return (
    <header className="sticky top-0 z-50 bg-white/95 backdrop-blur-md border-b border-[#EFECE6] shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-20 flex items-center justify-between">
        
        {/* Brand Logo */}
        <div className="flex items-center space-x-3 cursor-pointer" onClick={() => setActiveTab('home')}>
          <div className="w-12 h-12 rounded-2xl bg-[#D95338] text-white flex items-center justify-center shadow-md shadow-[#D95338]/20">
            <Utensils className="w-6 h-6" />
          </div>
          <div>
            <div className="flex items-center space-x-1.5">
              <span className="font-serif font-bold text-2xl tracking-tight text-[#2D2522]">Rasoi</span>
              <span className="text-xs font-bold px-2 py-0.5 rounded-full bg-[#F2994A]/15 text-[#D95338]">CookFree</span>
            </div>
            <p className="text-xs text-[#7A6F6B] font-medium hidden sm:block">Rapido for homemade food & local kitchens</p>
          </div>
        </div>

        {/* Location Selector */}
        <div className="hidden md:flex items-center space-x-2 bg-[#FDFBF7] border border-[#EFECE6] px-3.5 py-2 rounded-xl text-sm">
          <MapPin className="w-4 h-4 text-[#D95338]" />
          <span className="text-[#7A6F6B] text-xs">Delivering to:</span>
          <select 
            value={location} 
            onChange={(e) => setLocation(e.target.value)}
            className="bg-transparent font-semibold text-[#2D2522] focus:outline-none cursor-pointer"
          >
            {locations.map(loc => (
              <option key={loc} value={loc}>{loc}</option>
            ))}
          </select>
        </div>

        {/* Nav Actions */}
        <div className="flex items-center space-x-3">
          
          {/* Toggle Customer / Cook Console */}
          <button 
            onClick={() => setIsCookMode(!isCookMode)}
            className={`hidden sm:flex items-center space-x-2 px-4 py-2 rounded-xl text-xs font-bold transition-all ${
              isCookMode 
                ? 'bg-[#27AE60] text-white shadow-md shadow-[#27AE60]/20' 
                : 'bg-[#FDFBF7] border border-[#EFECE6] text-[#2D2522] hover:bg-[#EFECE6]/50'
            }`}
          >
            <ChefHat className="w-4 h-4" />
            <span>{isCookMode ? 'Cook Mode Active' : 'Switch to Cook Console'}</span>
          </button>

          {/* Subscriptions Button */}
          <button 
            onClick={() => setActiveTab('subscriptions')}
            className={`px-3.5 py-2 rounded-xl text-xs font-bold transition-all ${
              activeTab === 'subscriptions'
                ? 'bg-[#D95338] text-white'
                : 'bg-[#FDFBF7] border border-[#EFECE6] text-[#2D2522] hover:bg-[#EFECE6]/50'
            }`}
          >
            Tiffin Passes
          </button>

          {/* Active Order / Tracking */}
          <button 
            onClick={() => setActiveTab('tracking')}
            className="relative p-2.5 rounded-xl bg-[#FDFBF7] border border-[#EFECE6] text-[#2D2522] hover:bg-[#EFECE6]/50 transition-all"
            title="Active Orders & Live Tracking"
          >
            <ShoppingBag className="w-5 h-5 text-[#D95338]" />
            {activeOrderCount > 0 && (
              <span className="absolute -top-1.5 -right-1.5 bg-[#D95338] text-white text-[10px] font-bold w-5 h-5 rounded-full flex items-center justify-center shadow">
                {activeOrderCount}
              </span>
            )}
          </button>
        </div>

      </div>
    </header>
  );
}
