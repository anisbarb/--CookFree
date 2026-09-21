import React, { useState } from 'react';
import { SUBSCRIPTION_PLANS } from '../data/mockData';
import { Check, ShieldCheck, Sparkles, Calendar, ArrowRight } from 'lucide-react';

export function Subscriptions() {
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [isSubscribed, setIsSubscribed] = useState(false);

  const handleSubscribe = (plan) => {
    setSelectedPlan(plan);
    setIsSubscribed(true);
    setTimeout(() => {
      setIsSubscribed(false);
      alert(`Successfully subscribed to ${plan.title}! Your tiffin pass is now active.`);
    }, 1500);
  };

  return (
    <div className="max-w-5xl mx-auto space-y-12 pb-16">
      
      <div className="text-center max-w-2xl mx-auto space-y-4">
        <span className="text-xs font-bold px-3 py-1 rounded-full bg-[#D95338]/10 text-[#D95338]">Predictable Homestyle Nutrition</span>
        <h1 className="font-serif text-3xl sm:text-4xl font-bold text-[#2D2522]">Weekly & Monthly Tiffin Passes</h1>
        <p className="text-xs sm:text-sm text-[#7A6F6B]">Subscribe to verified home cooks for hassle-free daily lunch & dinner deliveries with guaranteed discounts.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        {SUBSCRIPTION_PLANS.map((plan, idx) => (
          <div key={plan.id} className={`bg-white rounded-3xl border p-8 flex flex-col justify-between card-hover relative ${
            idx === 1 ? 'border-[#D95338] shadow-xl ring-2 ring-[#D95338]/20' : 'border-[#EFECE6] shadow-sm'
          }`}>
            
            {idx === 1 && (
              <span className="absolute -top-3.5 left-1/2 transform -translate-x-1/2 bg-[#D95338] text-white text-[10px] font-bold px-4 py-1 rounded-full shadow">
                Most Popular
              </span>
            )}

            <div className="space-y-6">
              <div>
                <span className="text-xs font-bold text-[#27AE60] bg-[#27AE60]/10 px-3 py-1 rounded-full">{plan.discount}</span>
                <h3 className="font-serif font-bold text-xl text-[#2D2522] mt-3">{plan.title}</h3>
                <p className="text-xs text-[#7A6F6B] mt-1">{plan.description}</p>
              </div>

              <div className="flex items-baseline space-x-1">
                <span className="font-serif font-bold text-4xl text-[#2D2522]">₹{plan.pricePerMeal}</span>
                <span className="text-xs text-[#7A6F6B]">/ meal</span>
              </div>

              <div className="space-y-3 pt-4 border-t border-[#EFECE6] text-xs text-[#2D2522]">
                <div className="flex items-center space-x-2">
                  <Calendar className="w-4 h-4 text-[#D95338]" />
                  <span>{plan.mealsCount} Meals valid for {plan.durationDays} days</span>
                </div>
                <div className="flex items-center space-x-2">
                  <ShieldCheck className="w-4 h-4 text-[#27AE60]" />
                  <span>Pause or skip meals anytime</span>
                </div>
                <div className="flex items-center space-x-2">
                  <Sparkles className="w-4 h-4 text-[#F2994A]" />
                  <span>Choice of daily rotating home cooks</span>
                </div>
              </div>
            </div>

            <div className="pt-8">
              <button
                onClick={() => handleSubscribe(plan)}
                disabled={isSubscribed}
                className="w-full bg-[#D95338] hover:bg-[#B83D24] text-white font-bold py-4 rounded-2xl shadow-lg shadow-[#D95338]/30 transition-all flex items-center justify-center space-x-2 text-xs"
              >
                <span>{isSubscribed && selectedPlan?.id === plan.id ? 'Activating Pass...' : 'Get Tiffin Pass'}</span>
                <ArrowRight className="w-4 h-4" />
              </button>
            </div>

          </div>
        ))}
      </div>

    </div>
  );
}
