# CookFree / Rasoi - On-Demand Home-Cooked Meals Web App

**CookFree / Rasoi** is a production-ready, modern responsive web application built with **React, Vite, and CSS/Tailwind**, acting as the web counterpart of the Rapido-for-food-making platform connecting customers with verified neighborhood home cooks and small kitchens.

---

## Features

- **Customer Home Screen**: Location switcher, hero banner, live cook density indicator, regional cuisine filters (Assamese, Bengali, Andhra, Hyderabadi, North Indian), and verified home kitchen cards.
- **Intelligent Meal Builder**: 6-step interactive customizer (Base, Fresh Ingredients, Preparation Style, Spice Level, Portion Size, Special Notes) with automated transparent pricing and net cook earnings.
- **Rapido Live Tracking**: 6-stage live progress tracking (Accepted ➔ Preparing ➔ Cooking ➔ Packing ➔ Out for Delivery ➔ Delivered), cook trust profile, and direct chat/call & review modals.
- **Cook Partner Console**: Toggle Online/Offline, incoming order broadcast with atomic lock accept buttons, stage advancement flow, and meal capacity controls.
- **Tiffin Passes**: Weekly and monthly subscription passes with trial and professional tiers.
- **GitHub Pages Ready**: Configured for static site generation into `dist/`.

---

## Local Development Instructions

### Prerequisites
- Node.js (v18+ recommended)
- npm

### Windows CMD Quickstart

Open your Command Prompt (CMD) or PowerShell and run:

```cmd
cd CookFree
npm install
npm run dev
```

Then open your browser at:
```text
http://localhost:5173
```

---

## Production Build & Deployment

To build the static production bundle into the `dist/` folder:

```cmd
npm run build
```

### Deploying to GitHub Pages

1. In `vite.config.js`, uncomment and configure the `base` property with your GitHub repository name:
   ```javascript
   export default defineConfig({
     plugins: [react()],
     base: '/CookFree/',
   })
   ```
2. Build the project:
   ```cmd
   npm run build
   ```
3. Push the repository to GitHub and publish the `dist` folder to GitHub Pages.

---

## Project Structure

```text
CookFree/
├── src/
│   ├── components/
│   │   ├── Navbar.jsx
│   │   ├── Footer.jsx
│   │   ├── ContactModal.jsx
│   │   └── ReviewModal.jsx
│   ├── pages/
│   │   ├── CustomerHome.jsx
│   │   ├── MealBuilder.jsx
│   │   ├── LiveTracking.jsx
│   │   ├── CookConsole.jsx
│   │   └── Subscriptions.jsx
│   ├── data/
│   │   └── mockData.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── public/
├── index.html
├── package.json
├── vite.config.js
├── .gitignore
├── .env.example
└── README.md
```
