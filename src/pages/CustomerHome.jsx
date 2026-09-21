import React from 'react';
import { CUISINE_CATEGORIES, HOME_COOKS } from '../data/mockData';
import { Sparkles, Star, MapPin, ShieldCheck, Clock, ArrowRight, Flame } from 'lucide-react';

export function CustomerHome({ location, setActiveTab, setSelectedCuisine, setBuilderConfig }) {
  const handleStartCustomMeal = (cuisineName = 'Homestyle Special') => {
    setSelectedCuisine(cuisineName);
    setActiveTab('builder');
  };

  return (
    <div className="space-y-12 pb-16">
      
      {/* Hero Banner */}
      <section className="relative rounded-3xl overflow-hidden bg-gradient-to-r from-[#2D2522] to-[#4A3B37] text-white p-8 sm:p-12 shadow-xl">
        <div className="absolute inset-0 opacity-20 bg-[radial-gradient(#F2994A_1px,transparent_1px)] [background-size:16px_16px]"></div>
        <div className="relative z-10 max-w-2xl space-y-6">
          
          <div className="inline-flex items-center space-x-2 bg-white/10 backdrop-blur-md px-3.5 py-1.5 rounded-full text-xs font-semibold text-[#F2994A] border border-white/15">
            <Flame className="w-4 h-4 text-[#F2994A]" />
            <span>Rapido for Homemade Food in {location}</span>
          </div>

          <h1 className="font-serif text-3xl sm:text-5xl font-bold leading-tight tracking-tight">
            Fresh, Homestyle Tiffins & Custom Meals by Nearby Home Cooks
          </h1>

          <p className="text-sm sm:text-base text-white/80 leading-relaxed font-normal">
            Skip cloud kitchens. Connect directly with verified neighborhood home chefs who cook with pure ghee, zero artificial preservatives, and grandma-tested recipes.
          </p>

          <div className="flex flex-wrap items-center gap-4 pt-2">
            <button
              onClick={() => handleStartCustomMeal('Custom Meal')}
              className="bg-[#D95338] hover:bg-[#B83D24] text-white font-bold px-8 py-4 rounded-2xl shadow-lg shadow-[#D95338]/30 transition-all flex items-center space-x-2 text-sm sm:text-base"
            >
              <span>Build Custom Meal Now</span>
              <ArrowRight className="w-5 h-5" />
            </button>
            <div className="flex items-center space-x-2 text-xs text-white/70 bg-white/5 px-4 py-3 rounded-2xl border border-white/10">
              <ShieldCheck className="w-4 h-4 text-[#27AE60]" />
              <span>14 Verified Kitchens Active Nearby</span>
            </div>
          </div>

        </div>
      </section>

      {/* Cuisine Categories Rail */}
      <section className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="font-serif font-bold text-2xl text-[#2D2522]">Explore Regional Cuisines</h2>
            <p className="text-xs text-[#7A6F6B]">Authentic recipes from Assam, Bengal, Andhra, Hyderabad & more</p>
          </div>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
          {CUISINE_CATEGORIES.map((c) => (
            <div
              key={c.id}
              onClick={() => handleStartCustomMeal(c.name)}
              className="bg-white rounded-2xl p-5 border border-[#EFECE6] card-hover cursor-pointer flex flex-col items-center text-center space-y-3 group"
            >
              <div className="w-14 h-14 rounded-2xl bg-[#FDFBF7] border border-[#EFECE6] flex items-center justify-center text-2xl group-hover:bg-[#D95338]/10 transition-colors">
                {c.emoji}
              </div>
              <div>
                <h3 className="font-bold text-sm text-[#2D2522] group-hover:text-[#D95338] transition-colors">{c.name}</h3>
                <p className="text-[11px] text-[#7A6F6B]">From ₹{c.startingPrice}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Verified Home Kitchens Nearby */}
      <section className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="font-serif font-bold text-2xl text-[#2D2522]">Verified Home Kitchens Online</h2>
            <p className="text-xs text-[#7A6F6B]">Accepting orders right now in {location}</p>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {HOME_COOKS.map((cook) => (
            <div key={cook.id} className="bg-white rounded-3xl border border-[#EFECE6] overflow-hidden card-hover flex flex-col">
              
              {/* Image & Distance Badge */}
              <div className="relative h-48 bg-[#FDFBF7]">
                <img src={cook.avatar} alt={cook.name} className="w-full h-full object-cover" />
                <div className="absolute top-3 left-3 bg-white/90 backdrop-blur-md px-3 py-1 rounded-full text-xs font-bold text-[#2D2522] shadow-sm flex items-center space-x-1">
                  <MapPin className="w-3 h-3 text-[#D95338]" />
                  <span>{cook.distanceKm} km away</span>
                </div>
                <div className="absolute top-3 right-3 bg-[#27AE60] text-white px-2.5 py-1 rounded-full text-[10px] font-bold shadow-sm">
                  Online
                </div>
              </div>

              {/* Content */}
              <div className="p-5 flex-1 flex flex-col justify-between space-y-4">
                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <h3 className="font-serif font-bold text-base text-[#2D2522]">{cook.name}</h3>
                    <div className="flex items-center space-x-1 text-xs font-bold text-[#F2994A]">
                      <Star className="w-3.5 h-3.5 fill-current" />
                      <span>{cook.rating}</span>
                    </div>
                  </div>
                  <p className="text-xs font-medium text-[#D95338]">{cook.kitchen}</p>
                  <p className="text-xs text-[#7A6F6B] pt-1">{cook.specialty}</p>
                </div>

                <div className="pt-2 border-t border-[#EFECE6] flex items-center justify-between text-[11px] text-[#7A6F6B]">
                  <span>{cook.hygieneRating}</span>
                  <span className="font-semibold text-[#27AE60]">{cook.onTimeRate} On-Time</span>
                </div>

                <button
                  onClick={() => handleStartCustomMeal(cook.specialty)}
                  className="w-full bg-[#FDFBF7] hover:bg-[#D95338] hover:text-white border border-[#EFECE6] text-[#2D2522] text-xs font-bold py-3 rounded-xl transition-all"
                >
                  Order From Here
                </button>
              </div>

            </div>
          ))}
        </div>
      </section>

    </div>
  );
}
