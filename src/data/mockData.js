export const CUISINE_CATEGORIES = [
  { id: 'c1', name: 'Assamese', emoji: '🍛', startingPrice: 129, description: 'Fish Tenga & Traditional Khar' },
  { id: 'c2', name: 'Bengali', emoji: '🐟', startingPrice: 139, description: 'Shorshe Mach & Dal' },
  { id: 'c3', name: 'Andhra', emoji: '🌶️', startingPrice: 119, description: 'Gongura Mutton & Pappu' },
  { id: 'c4', name: 'Hyderabadi', emoji: '🍲', startingPrice: 149, description: 'Dum Biryani & Mirchi ka Salan' },
  { id: 'c5', name: 'North Indian', emoji: '🫓', startingPrice: 109, description: 'Dal Makhani & Soft Phulkas' },
  { id: 'c6', name: 'South Indian', emoji: '🥘', startingPrice: '99', description: 'Sambar Rice & Poriyal' }
];

export const HOME_COOKS = [
  {
    id: 'cook_1',
    name: 'Aunty Rina Ghosh',
    kitchen: "Rina's Bengal Rasoi",
    rating: 4.9,
    reviewsCount: 142,
    distanceKm: 1.2,
    specialty: 'Authentic Bengali & Fish Curries',
    hygieneRating: '5.0 FSSAI Verified',
    onTimeRate: '98%',
    repeatCustomers: '84%',
    avatar: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=200',
    isOnline: true
  },
  {
    id: 'cook_2',
    name: 'Suneeta Reddy',
    kitchen: 'Telangana Home Kitchen',
    rating: 4.8,
    reviewsCount: 218,
    distanceKm: 1.8,
    specialty: 'Andhra Spicy Meals & Gongura Pachadi',
    hygieneRating: '4.9 FSSAI Verified',
    onTimeRate: '97%',
    repeatCustomers: '91%',
    avatar: 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&q=80&w=200',
    isOnline: true
  },
  {
    id: 'cook_3',
    name: 'Bonti Bora',
    kitchen: 'Brahmaputra Homestyle',
    rating: 4.95,
    reviewsCount: 96,
    distanceKm: 2.4,
    specialty: 'Assamese Masor Tenga & Duck Curry',
    hygieneRating: '5.0 FSSAI Verified',
    onTimeRate: '99%',
    repeatCustomers: '88%',
    avatar: 'https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&q=80&w=200',
    isOnline: true
  },
  {
    id: 'cook_4',
    name: 'Fatima Begum',
    kitchen: 'Old City Nawab Khana',
    rating: 4.88,
    reviewsCount: 340,
    distanceKm: 3.1,
    specialty: 'Hyderabadi Haleem & Patthar ka Gosht',
    hygieneRating: '4.9 FSSAI Verified',
    onTimeRate: '96%',
    repeatCustomers: '93%',
    avatar: 'https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?auto=format&fit=crop&q=80&w=200',
    isOnline: true
  }
];

export const MEAL_BASES = [
  { id: 'b1', name: 'Steamed Basmati Rice', emoji: '🍚', cost: 25 },
  { id: 'b2', name: 'Soft Wheat Phulkas (4 pcs)', emoji: '🫓', cost: 30 },
  { id: 'b3', name: 'Jeera Ghee Rice', emoji: '🥘', cost: 35 },
  { id: 'b4', name: 'Malabar Parotta (2 pcs)', emoji: '🥠', cost: 40 },
  { id: 'b5', name: 'Brown Rice & Quinoa Mix', emoji: '🌾', cost: 45 }
];

export const INGREDIENTS = [
  { id: 'i1', name: 'Country Potato', emoji: '🥔', cost: 12, isVeg: true },
  { id: 'i2', name: 'Ripe Tomato', emoji: '🍅', cost: 10, isVeg: true },
  { id: 'i3', name: 'Farm Fresh Egg', emoji: '🥚', cost: 20, isVeg: false },
  { id: 'i4', name: 'Tender Chicken Breast', emoji: '🍗', cost: 45, isVeg: false },
  { id: 'i5', name: 'Malai Paneer Cubes', emoji: '🧀', cost: 40, isVeg: true },
  { id: 'i6', name: 'River Rohu Fish Piece', emoji: '🐟', cost: 50, isVeg: false },
  { id: 'i7', name: 'Seasonal Greens & Palak', emoji: '🥬', cost: 15, isVeg: true },
  { id: 'i8', name: 'Yellow Toor Dal', emoji: '🥣', cost: 18, isVeg: true }
];

export const PREPARATION_STYLES = [
  { id: 'p1', name: 'Homestyle Thin Curry', effort: 15, description: 'Soothing grandma-style broth with mild spices' },
  { id: 'p2', name: 'Thick Masala Gravy', effort: 20, description: 'Rich onion-tomato masala coated to perfection' },
  { id: 'p3', name: 'Tawa Stir-Fry', effort: 12, description: 'Dry tossed with roasted cumin and coriander' },
  { id: 'p4', name: 'Traditional Khar / Boiled', effort: 10, description: 'Digestive, low spice preparation with raw papaya/dal' }
];

export const SUBSCRIPTION_PLANS = [
  {
    id: 'sub_1',
    title: 'Trial Tiffin Pass',
    mealsCount: 5,
    durationDays: 7,
    pricePerMeal: 129,
    discount: '10% OFF',
    description: 'Perfect for trying out homestyle lunches for a work week.'
  },
  {
    id: 'sub_2',
    title: 'Monthly Homestyle Lunch & Dinner',
    mealsCount: 40,
    durationDays: 30,
    pricePerMeal: 109,
    discount: '25% OFF',
    description: 'Complete home-cooked meal subscription with Sunday special dessert.'
  },
  {
    id: 'sub_3',
    title: 'Working Professional Pack',
    mealsCount: 20,
    durationDays: 30,
    pricePerMeal: 119,
    discount: '18% OFF',
    description: 'Mon-Fri fresh lunch delivered right to your office or desk.'
  }
];
