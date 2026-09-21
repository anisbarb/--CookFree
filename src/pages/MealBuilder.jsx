import React, { useState } from 'react';
import { MEAL_BASES, INGREDIENTS, PREPARATION_STYLES } from '../data/mockData';
import { Check, Flame, Plus, Minus, ArrowRight, ShieldCheck, Sparkles, ChefHat } from 'lucide-react';

export function MealBuilder({ onPlaceOrder, selectedCuisine }) {
  const [selectedBase, setSelectedBase] = useState(MEAL_BASES[0]);
  const [selectedIngredients, setSelectedIngredients] = useState([INGREDIENTS[0], INGREDIENTS[1]]);
  const [selectedPrep, setSelectedPrep] = useState(PREPARATION_STYLES[0]);
  const [spiceLevel, setSpiceLevel] = useState('Medium 🌶️');
  const [portionSize, setPortionSize] = useState('1 Person');
  const [specialNotes, setSpecialNotes] = useState('');
  const [isMatchingRadar, setIsMatchingRadar] = useState(false);
  const [matchedCook, setMatchedCook] = useState(null);

  // Pricing calculation
  const portionMultiplier = portionSize === '2 People' ? 1.8 : portionSize === 'Family (3-4)' ? 3.2 : 1.0;
  const ingredientsCost = Math.round((selectedBase.cost + selectedIngredients.reduce((acc, i) => acc + i.cost, 0)) * portionMultiplier);
  const cookingEffort = Math.round((selectedPrep.effort + selectedIngredients.length * 5) * portionMultiplier);
  const packagingCost = portionSize === 'Family (3-4)' ? 15 : 8;
  const deliveryFee = 18;
  const platformFee = 8;
  const totalCost = ingredientsCost + cookingEffort + packagingCost + deliveryFee + platformFee;
  const cookEarnings = ingredientsCost + cookingEffort;

  const toggleIngredient = (ing) => {
    if (selectedIngredients.find(i => i.id === ing.id)) {
      if (selectedIngredients.length > 1) {
        setSelectedIngredients(selectedIngredients.filter(i => i.id !== ing.id));
      }
    } else {
      setSelectedIngredients([...selectedIngredients, ing]);
    }
  };

  const handleStartMatching = () => {
    setIsMatchingRadar(true);
    setTimeout(() => {
      setIsMatchingRadar(false);
      const orderPayload = {
        id: `ORD-${Math.floor(100000 + Math.random() * 900000)}`,
        baseName: selectedBase.name,
        ingredientsSummary: selectedIngredients.map(i => i.name).join(', '),
        preparationName: selectedPrep.name,
        spiceLevel,
        portion: portionSize,
        notes: specialNotes || 'None',
        totalCost,
        cookEarnings,
        createdAt: Date.now()
      };
      onPlaceOrder(orderPayload);
    }, 3000);
  };

  if (isMatchingRadar) {
    return (
      <div className="max-w-xl mx-auto py-16 text-center space-y-8 bg-white rounded-3xl p-8 border border-[#EFECE6] shadow-xl my-12">
        <div className="relative w-32 h-32 mx-auto flex items-center justify-center">
          <div className="absolute inset-0 rounded-full bg-[#D95338]/20 animate-ping"></div>
          <div className="absolute inset-4 rounded-full bg-[#D95338]/40 animate-pulse"></div>
          <div className="relative z-10 w-16 h-16 rounded-2xl bg-[#D95338] text-white flex items-center justify-center shadow-lg">
            <ChefHat className="w-8 h-8 animate-spin" />
          </div>
        </div>

        <div className="space-y-2">
          <h2 className="font-serif font-bold text-2xl text-[#2D2522]">Scanning Nearby Home Kitchens...</h2>
          <p className="text-xs text-[#7A6F6B]">Broadcasting your custom {selectedCuisine || 'homestyle'} meal to verified cooks within 3 km via atomic locking.</p>
        </div>

        <div className="bg-[#FDFBF7] p-4 rounded-2xl border border-[#EFECE6] text-left space-y-2 text-xs">
          <div className="flex justify-between"><span>Base:</span><span className="font-semibold">{selectedBase.name}</span></div>
          <div className="flex justify-between"><span>Ingredients:</span><span className="font-semibold">{selectedIngredients.map(i => i.name).join(', ')}</span></div>
          <div className="flex justify-between"><span>Estimated Total:</span><span className="font-semibold text-[#D95338]">₹{totalCost}</span></div>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8 pb-16">
      
      {/* Customizer steps */}
      <div className="lg:col-span-2 space-y-8">
        
        <div>
          <span className="text-xs font-bold px-3 py-1 rounded-full bg-[#D95338]/10 text-[#D95338]">Custom Meal Builder</span>
          <h1 className="font-serif text-3xl font-bold text-[#2D2522] mt-2">Craft Your Homestyle Meal</h1>
          <p className="text-xs text-[#7A6F6B]">Configure base, fresh ingredients, preparation style, and spice level.</p>
        </div>

        {/* Step 1: Choose Base */}
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-4 shadow-sm">
          <h3 className="font-bold text-sm text-[#2D2522] flex items-center justify-between">
            <span>1. Choose Your Meal Base</span>
            <span className="text-xs font-normal text-[#7A6F6B]">Required</span>
          </h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            {MEAL_BASES.map((b) => (
              <div
                key={b.id}
                onClick={() => setSelectedBase(b)}
                className={`p-4 rounded-2xl border cursor-pointer transition-all flex items-center justify-between ${
                  selectedBase.id === b.id ? 'border-[#D95338] bg-[#D95338]/5 shadow-sm' : 'border-[#EFECE6] hover:bg-[#FDFBF7]'
                }`}
              >
                <div className="flex items-center space-x-3">
                  <span className="text-2xl">{b.emoji}</span>
                  <div>
                    <h4 className="font-bold text-xs text-[#2D2522]">{b.name}</h4>
                    <p className="text-[10px] text-[#7A6F6B]">Base ₹{b.cost}</p>
                  </div>
                </div>
                {selectedBase.id === b.id && <Check className="w-4 h-4 text-[#D95338]" />}
              </div>
            ))}
          </div>
        </div>

        {/* Step 2: Fresh Ingredients */}
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-4 shadow-sm">
          <h3 className="font-bold text-sm text-[#2D2522] flex items-center justify-between">
            <span>2. Select Fresh Ingredients (Multiple)</span>
            <span className="text-xs font-normal text-[#7A6F6B]">{selectedIngredients.length} selected</span>
          </h3>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
            {INGREDIENTS.map((ing) => {
              const isSelected = selectedIngredients.find(i => i.id === ing.id);
              return (
                <div
                  key={ing.id}
                  onClick={() => toggleIngredient(ing)}
                  className={`p-3.5 rounded-2xl border cursor-pointer transition-all flex flex-col items-center text-center space-y-2 ${
                    isSelected ? 'border-[#D95338] bg-[#D95338]/5 shadow-sm' : 'border-[#EFECE6] hover:bg-[#FDFBF7]'
                  }`}
                >
                  <span className="text-2xl">{ing.emoji}</span>
                  <div>
                    <h4 className="font-bold text-xs text-[#2D2522]">{ing.name}</h4>
                    <p className="text-[10px] text-[#7A6F6B]">₹{ing.cost}</p>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Step 3: Preparation Style */}
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-4 shadow-sm">
          <h3 className="font-bold text-sm text-[#2D2522]">3. Preparation Style</h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            {PREPARATION_STYLES.map((prep) => (
              <div
                key={prep.id}
                onClick={() => setSelectedPrep(prep)}
                className={`p-4 rounded-2xl border cursor-pointer transition-all space-y-1 ${
                  selectedPrep.id === prep.id ? 'border-[#D95338] bg-[#D95338]/5 shadow-sm' : 'border-[#EFECE6] hover:bg-[#FDFBF7]'
                }`}
              >
                <div className="flex items-center justify-between">
                  <h4 className="font-bold text-xs text-[#2D2522]">{prep.name}</h4>
                  {selectedPrep.id === prep.id && <Check className="w-4 h-4 text-[#D95338]" />}
                </div>
                <p className="text-[11px] text-[#7A6F6B]">{prep.description}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Step 4: Taste & Portion */}
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] space-y-6 shadow-sm">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
            
            <div className="space-y-3">
              <h4 className="font-bold text-xs text-[#2D2522]">4. Spice Level</h4>
              <div className="flex space-x-2">
                {['Mild 🌱', 'Medium 🌶️', 'Spicy 🔥'].map((level) => (
                  <button
                    key={level}
                    type="button"
                    onClick={() => setSpiceLevel(level)}
                    className={`flex-1 py-2.5 rounded-xl text-xs font-bold border transition-all ${
                      spiceLevel === level ? 'bg-[#D95338] text-white border-[#D95338]' : 'bg-[#FDFBF7] border-[#EFECE6] text-[#2D2522]'
                    }`}
                  >
                    {level}
                  </button>
                ))}
              </div>
            </div>

            <div className="space-y-3">
              <h4 className="font-bold text-xs text-[#2D2522]">5. Portion Size</h4>
              <div className="flex space-x-2">
                {['1 Person', '2 People', 'Family (3-4)'].map((p) => (
                  <button
                    key={p}
                    type="button"
                    onClick={() => setPortionSize(p)}
                    className={`flex-1 py-2.5 rounded-xl text-[11px] font-bold border transition-all ${
                      portionSize === p ? 'bg-[#D95338] text-white border-[#D95338]' : 'bg-[#FDFBF7] border-[#EFECE6] text-[#2D2522]'
                    }`}
                  >
                    {p}
                  </button>
                ))}
              </div>
            </div>

          </div>

          <div className="space-y-2">
            <h4 className="font-bold text-xs text-[#2D2522]">6. Special Instructions for Cook</h4>
            <input
              type="text"
              value={specialNotes}
              onChange={(e) => setSpecialNotes(e.target.value)}
              placeholder="e.g. Less oil, no garlic, extra coriander..."
              className="w-full bg-[#FDFBF7] border border-[#EFECE6] rounded-2xl px-4 py-3 text-xs focus:outline-none focus:border-[#D95338]"
            />
          </div>
        </div>

      </div>

      {/* Pricing & Order Summary Sidebar */}
      <div className="space-y-6">
        <div className="bg-white rounded-3xl p-6 border border-[#EFECE6] shadow-xl sticky top-28 space-y-6">
          
          <div className="border-b border-[#EFECE6] pb-4">
            <h3 className="font-serif font-bold text-lg text-[#2D2522]">Intelligent Pricing</h3>
            <p className="text-xs text-[#7A6F6B]">Transparent cost breakdown & cook earnings</p>
          </div>

          <div className="space-y-3 text-xs">
            <div className="flex justify-between text-[#7A6F6B]">
              <span>Base ({selectedBase.name}):</span>
              <span className="font-semibold text-[#2D2522]">₹{Math.round(selectedBase.cost * portionMultiplier)}</span>
            </div>
            <div className="flex justify-between text-[#7A6F6B]">
              <span>Ingredients ({selectedIngredients.length}):</span>
              <span className="font-semibold text-[#2D2522]">₹{Math.round(selectedIngredients.reduce((acc, i) => acc + i.cost, 0) * portionMultiplier)}</span>
            </div>
            <div className="flex justify-between text-[#7A6F6B]">
              <span>Cooking Effort ({selectedPrep.name}):</span>
              <span className="font-semibold text-[#2D2522]">₹{cookingEffort}</span>
            </div>
            <div className="flex justify-between text-[#7A6F6B]">
              <span>Eco Packaging:</span>
              <span className="font-semibold text-[#2D2522]">₹{packagingCost}</span>
            </div>
            <div className="flex justify-between text-[#7A6F6B]">
              <span>Delivery & Platform Fee:</span>
              <span className="font-semibold text-[#2D2522]">₹{deliveryFee + platformFee}</span>
            </div>

            <div className="pt-3 border-t border-[#EFECE6] flex justify-between font-bold text-sm text-[#2D2522]">
              <span>Total Customer Price:</span>
              <span className="text-[#D95338] text-base">₹{totalCost}</span>
            </div>

            <div className="bg-[#27AE60]/10 p-3 rounded-2xl border border-[#27AE60]/20 flex items-center justify-between text-[11px] text-[#27AE60] font-bold">
              <span>Direct Cook Earnings:</span>
              <span>₹{cookEarnings} (100% to chef)</span>
            </div>
          </div>

          <button
            onClick={handleStartMatching}
            className="w-full bg-[#D95338] hover:bg-[#B83D24] text-white font-bold py-4 rounded-2xl shadow-lg shadow-[#D95338]/30 transition-all flex items-center justify-center space-x-2 text-sm"
          >
            <span>Find Nearby Cook</span>
            <ArrowRight className="w-4 h-4" />
          </button>

          <div className="flex items-center space-x-2 text-[11px] text-[#7A6F6B] justify-center">
            <ShieldCheck className="w-4 h-4 text-[#27AE60]" />
            <span>Atomic Lock Guarantee • FSSAI Verified</span>
          </div>

        </div>
      </div>

    </div>
  );
}
