import React, { useState, useEffect } from 'react';
import { Navbar } from './components/Navbar';
import { Footer } from './components/Footer';
import { CustomerHome } from './pages/CustomerHome';
import { MealBuilder } from './pages/MealBuilder';
import { LiveTracking } from './pages/LiveTracking';
import { CookConsole } from './pages/CookConsole';
import { Subscriptions } from './pages/Subscriptions';

export default function App() {
  const [activeTab, setActiveTab] = useState('home'); // home | builder | tracking | cook | subscriptions
  const [location, setLocation] = useState('Gachibowli');
  const [selectedCuisine, setSelectedCuisine] = useState('Homestyle Special');
  const [isCookMode, setIsCookMode] = useState(false);
  
  // Orders state persisted in localStorage
  const [orders, setOrders] = useState(() => {
    const saved = localStorage.getItem('cookfree_orders');
    return saved ? JSON.parse(saved) : [
      {
        id: 'ORD-892410',
        baseName: 'Steamed Basmati Rice',
        ingredientsSummary: 'Country Potato, Ripe Tomato, Farm Fresh Egg',
        preparationName: 'Homestyle Thin Curry',
        spiceLevel: 'Medium 🌶️',
        portion: '1 Person',
        notes: 'Less oil please',
        totalCost: 145,
        cookEarnings: 97,
        status: 'COOKING',
        assignedCookId: 'cook_1',
        assignedCookName: 'Aunty Rina Ghosh',
        assignedCookKitchen: "Rina's Bengal Rasoi",
        createdAt: Date.now() - 300000,
        rating: null
      }
    ];
  });

  useEffect(() => {
    localStorage.setItem('cookfree_orders', JSON.stringify(orders));
  }, [orders]);

  const handlePlaceOrder = (newOrder) => {
    const orderWithStatus = {
      ...newOrder,
      status: 'ACCEPTED',
      assignedCookId: 'cook_1',
      assignedCookName: 'Aunty Rina Ghosh',
      assignedCookKitchen: "Rina's Bengal Rasoi"
    };
    setOrders(prev => [orderWithStatus, ...prev]);
    setActiveTab('tracking');
  };

  const handleUpdateOrderStatus = (orderId, newStatus) => {
    setOrders(prev => prev.map(o => o.id === orderId ? { ...o, status: newStatus } : o));
  };

  const handleAddReview = (orderId, rating, reviewText) => {
    setOrders(prev => prev.map(o => o.id === orderId ? { ...o, rating, review: reviewText } : o));
  };

  const handleAcceptOrder = (orderId, cookId, cookKitchen) => {
    setOrders(prev => prev.map(o => o.id === orderId ? { ...o, assignedCookId: cookId, assignedCookKitchen: cookKitchen, status: 'PREPARING' } : o));
  };

  const handleAdvanceOrderStatus = (orderId) => {
    const stages = ['ACCEPTED', 'PREPARING', 'COOKING', 'PACKING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
    setOrders(prev => prev.map(o => {
      const idx = stages.indexOf(o.status || 'ACCEPTED');
      const nextStatus = idx < stages.length - 1 ? stages[idx + 1] : 'DELIVERED';
      return o.id === orderId ? { ...o, status: nextStatus } : o;
    }));
  };

  return (
    <div className="min-h-screen flex flex-col bg-[#FDFBF7]">
      <Navbar 
        activeTab={activeTab} 
        setActiveTab={setActiveTab}
        location={location}
        setLocation={setLocation}
        activeOrderCount={orders.filter(o => o.status !== 'DELIVERED').length}
        isCookMode={isCookMode}
        setIsCookMode={setIsCookMode}
      />

      <main className="flex-1 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-8 w-full">
        {isCookMode ? (
          <CookConsole 
            incomingOrders={orders}
            onAcceptOrder={handleAcceptOrder}
            onAdvanceOrderStatus={handleAdvanceOrderStatus}
          />
        ) : (
          <>
            {activeTab === 'home' && (
              <CustomerHome 
                location={location}
                setActiveTab={setActiveTab}
                setSelectedCuisine={setSelectedCuisine}
              />
            )}
            {activeTab === 'builder' && (
              <MealBuilder 
                selectedCuisine={selectedCuisine}
                onPlaceOrder={handlePlaceOrder}
              />
            )}
            {activeTab === 'tracking' && (
              <LiveTracking 
                orders={orders}
                onUpdateOrderStatus={handleUpdateOrderStatus}
                onAddReview={handleAddReview}
              />
            )}
            {activeTab === 'subscriptions' && (
              <Subscriptions />
            )}
          </>
        )}
      </main>

      <Footer />
    </div>
  );
}
