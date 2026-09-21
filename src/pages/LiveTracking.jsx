import React, { useState } from 'react';
import { CheckCircle2, Clock, ChefHat, Phone, MessageSquare, Star, ShieldCheck, MapPin, ArrowRight } from 'lucide-react';
import { ContactModal } from '../components/ContactModal';
import { ReviewModal } from '../components/ReviewModal';
import { HOME_COOKS } from '../data/mockData';

export function LiveTracking({ orders, onUpdateOrderStatus, onAddReview }) {
  const [selectedContactCook, setSelectedContactCook] = useState(null);
  const [reviewOrder, setReviewOrder] = useState(null);

  if (!orders || orders.length === 0) {
    return (
      <div className="max-w-md mx-auto py-20 text-center space-y-6 bg-white rounded-3xl p-8 border border-[#EFECE6] shadow-sm my-12">
        <div className="w-16 h-16 bg-[#D95338]/10 text-[#D95338] rounded-2xl flex items-center justify-center mx-auto text-2xl">
          📦
        </div>
        <div className="space-y-2">
          <h2 className="font-serif font-bold text-2xl text-[#2D2522]">No Active Orders</h2>
          <p className="text-xs text-[#7A6F6B]">You have no active orders in progress. Build a custom meal or subscribe to a tiffin pass!</p>
        </div>
      </div>
    );
  }

  const stages = ['ACCEPTED', 'PREPARING', 'COOKING', 'PACKING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
  const stageLabels = {
    'ACCEPTED': 'Order Accepted',
    'PREPARING': 'Gathering Ingredients',
    'COOKING': 'Piping Hot Cooking',
    'PACKING': 'Eco-Friendly Packing',
    'OUT_FOR_DELIVERY': 'Out for Delivery',
    'DELIVERED': 'Delivered Successfully'
  };

  return (
    <div className="max-w-4xl mx-auto space-y-8 pb-16">
      
      <div>
        <span className="text-xs font-bold px-3 py-1 rounded-full bg-[#27AE60]/10 text-[#27AE60]">Live Rapido Tracking</span>
        <h1 className="font-serif text-3xl font-bold text-[#2D2522] mt-2">Active Orders & Kitchen Radar</h1>
      </div>

      {orders.map((order) => {
        const currentStageIndex = stages.indexOf(order.status || 'ACCEPTED');
        const cookObj = HOME_COOKS.find(c => c.id === order.assignedCookId) || HOME_COOKS[0];

        return (
          <div key={order.id} className="bg-white rounded-3xl border border-[#EFECE6] p-6 sm:p-8 shadow-xl space-y-8">
            
            {/* Top Info */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between border-b border-[#EFECE6] pb-6 gap-4">
              <div>
                <div className="flex items-center space-x-2">
                  <span className="font-bold text-sm text-[#D95338]">{order.id}</span>
                  <span className="text-xs px-2.5 py-0.5 rounded-full bg-[#27AE60]/10 text-[#27AE60] font-bold">
                    {order.status === 'DELIVERED' ? 'Delivered' : 'Live Tracking'}
                  </span>
                </div>
                <h2 className="font-serif font-bold text-xl text-[#2D2522] mt-1">{order.baseName} with {order.preparationName}</h2>
                <p className="text-xs text-[#7A6F6B]">Ingredients: {order.ingredientsSummary}</p>
              </div>

              <div className="text-right">
                <span className="text-xs text-[#7A6F6B]">Total Price</span>
                <p className="font-bold text-xl text-[#2D2522]">₹{order.totalCost}</p>
              </div>
            </div>

            {/* Progress Steps */}
            <div className="space-y-4">
              <h3 className="font-bold text-xs text-[#7A6F6B] uppercase tracking-wider">6-Stage Live Progress</h3>
              <div className="grid grid-cols-2 sm:grid-cols-6 gap-2">
                {stages.map((st, idx) => {
                  const isDone = idx <= currentStageIndex;
                  const isCurrent = idx === currentStageIndex;
                  return (
                    <div key={st} className={`p-3 rounded-2xl border text-center space-y-1 ${
                      isCurrent ? 'border-[#D95338] bg-[#D95338]/10 text-[#D95338]' : isDone ? 'border-[#27AE60] bg-[#27AE60]/10 text-[#27AE60]' : 'border-[#EFECE6] text-[#7A6F6B] opacity-50'
                    }`}>
                      <div className="font-bold text-[10px]">{idx + 1}</div>
                      <div className="font-bold text-[11px] leading-tight">{stageLabels[st]}</div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Cook Trust Profile & Contact */}
            <div className="bg-[#FDFBF7] rounded-2xl p-5 border border-[#EFECE6] flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div className="flex items-center space-x-4">
                <img src={cookObj.avatar} alt={cookObj.name} className="w-14 h-14 rounded-full object-cover border-2 border-white shadow" />
                <div>
                  <div className="flex items-center space-x-2">
                    <h4 className="font-bold text-sm text-[#2D2522]">{cookObj.name}</h4>
                    <span className="flex items-center text-xs font-bold text-[#F2994A]">
                      <Star className="w-3.5 h-3.5 fill-current" />
                      <span>{cookObj.rating}</span>
                    </span>
                  </div>
                  <p className="text-xs text-[#D95338] font-medium">{cookObj.kitchen}</p>
                  <p className="text-[11px] text-[#7A6F6B]">FSSAI Hygiene: {cookObj.hygieneRating}</p>
                </div>
              </div>

              <div className="flex items-center space-x-3">
                <button
                  onClick={() => setSelectedContactCook(cookObj)}
                  className="bg-white hover:bg-[#D95338] hover:text-white border border-[#EFECE6] text-[#2D2522] text-xs font-bold px-4 py-3 rounded-xl transition-all flex items-center space-x-2 shadow-sm"
                >
                  <MessageSquare className="w-4 h-4" />
                  <span>Chat / Call Cook</span>
                </button>

                {order.status !== 'DELIVERED' ? (
                  <button
                    onClick={() => onUpdateOrderStatus(order.id, 'DELIVERED')}
                    className="bg-[#27AE60] hover:bg-[#219653] text-white text-xs font-bold px-4 py-3 rounded-xl transition-all shadow-sm"
                  >
                    Simulate Delivery
                  </button>
                ) : !order.rating ? (
                  <button
                    onClick={() => setReviewOrder(order)}
                    className="bg-[#D95338] hover:bg-[#B83D24] text-white text-xs font-bold px-4 py-3 rounded-xl transition-all shadow-sm"
                  >
                    Rate Meal
                  </button>
                ) : (
                  <span className="text-xs font-bold text-[#27AE60] bg-[#27AE60]/10 px-3 py-2 rounded-xl">
                    Rated {order.rating} ⭐
                  </span>
                )}
              </div>
            </div>

          </div>
        );
      })}

      {selectedContactCook && (
        <ContactModal cook={selectedContactCook} onClose={() => setSelectedContactCook(null)} />
      )}

      {reviewOrder && (
        <ReviewModal order={reviewOrder} onClose={() => setReviewOrder(null)} onSubmitReview={onAddReview} />
      )}

    </div>
  );
}
