import React, { useState } from 'react';
import { ChefHat, Power, Check, Clock, DollarSign, Settings, Bell, ShieldCheck, MapPin } from 'lucide-react';

export function CookConsole({ incomingOrders, onAcceptOrder, onAdvanceOrderStatus }) {
  const [isOnline, setIsOnline] = useState(true);
  const [mealCapacity, setMealCapacity] = useState(12);
  const [serviceRadius, setServiceRadius] = useState(4);

  return (
    <div className="max-w-5xl mx-auto space-y-8 pb-16">
      
      {/* Top Bar with Online Toggle */}
      <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] shadow-sm flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <span className="text-xs font-bold px-3 py-1 rounded-full bg-[#27AE60]/10 text-[#27AE60]">Cook Partner Portal</span>
          <h1 className="font-serif text-3xl font-bold text-[#2D2522] mt-1">Aunty Rina's Kitchen Dashboard</h1>
          <p className="text-xs text-[#7A6F6B]">Accept incoming atomic orders, advance prep stages & manage daily meal capacity.</p>
        </div>

        <button
          onClick={() => setIsOnline(!isOnline)}
          className={`px-6 py-3.5 rounded-2xl font-bold text-sm flex items-center space-x-2 transition-all shadow-md ${
            isOnline ? 'bg-[#27AE60] text-white shadow-[#27AE60]/20' : 'bg-[#D95338] text-white shadow-[#D95338]/20'
          }`}
        >
          <Power className="w-4 h-4" />
          <span>{isOnline ? 'You Are ONLINE & Receiving Orders' : 'You Are OFFLINE'}</span>
        </button>
      </div>

      {/* Stats & Controls */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-2 shadow-sm">
          <span className="text-xs text-[#7A6F6B]">Today's Cook Earnings</span>
          <div className="font-serif text-3xl font-bold text-[#2D2522]">₹1,840</div>
          <p className="text-[11px] text-[#27AE60] font-semibold">100% credited directly via instant UPI</p>
        </div>

        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-3 shadow-sm">
          <div className="flex justify-between text-xs font-bold text-[#2D2522]">
            <span>Meal Capacity</span>
            <span className="text-[#D95338]">{mealCapacity} meals</span>
          </div>
          <input
            type="range"
            min="5"
            max="30"
            value={mealCapacity}
            onChange={(e) => setMealCapacity(e.target.value)}
            className="w-full accent-[#D95338] cursor-pointer"
          />
          <p className="text-[10px] text-[#7A6F6B]">Max meals you can prepare for this lunch slot.</p>
        </div>

        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-3 shadow-sm">
          <div className="flex justify-between text-xs font-bold text-[#2D2522]">
            <span>Service Radius</span>
            <span className="text-[#D95338]">{serviceRadius} km</span>
          </div>
          <input
            type="range"
            min="1"
            max="8"
            value={serviceRadius}
            onChange={(e) => setServiceRadius(e.target.value)}
            className="w-full accent-[#D95338] cursor-pointer"
          />
          <p className="text-[10px] text-[#7A6F6B]">Delivery radius for rapido food partners.</p>
        </div>

      </div>

      {/* Incoming / Active Orders for Cook */}
      <div className="space-y-6">
        <h2 className="font-serif font-bold text-xl text-[#2D2522]">Incoming & Active Orders</h2>

        {!incomingOrders || incomingOrders.length === 0 ? (
          <div className="bg-white rounded-3xl p-12 text-center border border-[#EFECE6] space-y-3">
            <Bell className="w-10 h-10 text-[#7A6F6B] mx-auto animate-bounce" />
            <h3 className="font-bold text-base text-[#2D2522]">Waiting for New Orders...</h3>
            <p className="text-xs text-[#7A6F6B]">Keep your status online. When customers customize meals nearby, they appear here instantly.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-6">
            {incomingOrders.map((order) => (
              <div key={order.id} className="bg-white rounded-3xl border border-[#EFECE6] p-6 sm:p-8 shadow-lg space-y-6">
                
                <div className="flex flex-col sm:flex-row sm:items-center justify-between border-b border-[#EFECE6] pb-4 gap-4">
                  <div>
                    <span className="text-xs font-bold text-[#D95338]">{order.id}</span>
                    <h3 className="font-serif font-bold text-xl text-[#2D2522] mt-1">{order.baseName} with {order.preparationName}</h3>
                    <p className="text-xs text-[#7A6F6B]">Ingredients: {order.ingredientsSummary} • Spice: {order.spiceLevel}</p>
                  </div>

                  <div className="text-right">
                    <span className="text-xs text-[#7A6F6B]">Your Net Earnings</span>
                    <p className="font-bold text-2xl text-[#27AE60]">₹{order.cookEarnings}</p>
                  </div>
                </div>

                <div className="bg-[#FDFBF7] p-4 rounded-2xl border border-[#EFECE6] flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                  <div className="space-y-1 text-xs">
                    <p className="text-[#7A6F6B]">Portion: <span className="font-bold text-[#2D2522]">{order.portion}</span></p>
                    <p className="text-[#7A6F6B]">Special Notes: <span className="font-bold text-[#D95338]">{order.notes}</span></p>
                  </div>

                  <div className="flex items-center space-x-3">
                    {!order.assignedCookId ? (
                      <button
                        onClick={() => onAcceptOrder(order.id, 'cook_1', "Rina's Bengal Rasoi")}
                        className="bg-[#27AE60] hover:bg-[#219653] text-white font-bold px-6 py-3 rounded-xl transition-all shadow-md text-xs"
                      >
                        Accept & Lock Order (Atomic)
                      </button>
                    ) : (
                      <div className="flex items-center space-x-2">
                        <span className="text-xs font-bold px-3 py-2 rounded-xl bg-[#27AE60]/10 text-[#27AE60]">
                          Status: {order.status}
                        </span>
                        <button
                          onClick={() => onAdvanceOrderStatus(order.id)}
                          className="bg-[#D95338] hover:bg-[#B83D24] text-white font-bold px-5 py-3 rounded-xl transition-all shadow-md text-xs"
                        >
                          Advance Stage ➔
                        </button>
                      </div>
                    )}
                  </div>
                </div>

              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}
