import React from 'react';
import { Utensils, Heart, ShieldCheck, Code } from 'lucide-react';

export function Footer() {
  return (
    <footer className="bg-white border-t border-[#EFECE6] mt-20 py-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          
          <div className="space-y-4 md:col-span-1">
            <div className="flex items-center space-x-2">
              <div className="w-10 h-10 rounded-xl bg-[#D95338] text-white flex items-center justify-center">
                <Utensils className="w-5 h-5" />
              </div>
              <span className="font-serif font-bold text-xl text-[#2D2522]">Rasoi / CookFree</span>
            </div>
            <p className="text-xs text-[#7A6F6B] leading-relaxed">
              Empowering neighborhood home cooks with instant atomic order matching and direct homestyle tiffin delivery.
            </p>
          </div>

          <div>
            <h4 className="font-bold text-sm text-[#2D2522] mb-3">Quick Links</h4>
            <ul className="space-y-2 text-xs text-[#7A6F6B]">
              <li><a href="#home" className="hover:text-[#D95338]">Explore Kitchens</a></li>
              <li><a href="#builder" className="hover:text-[#D95338]">Custom Meal Builder</a></li>
              <li><a href="#subscriptions" className="hover:text-[#D95338]">Weekly Tiffin Passes</a></li>
              <li><a href="#cooks" className="hover:text-[#D95338]">Become a Home Cook</a></li>
            </ul>
          </div>

          <div>
            <h4 className="font-bold text-sm text-[#2D2522] mb-3">Trust & Safety</h4>
            <ul className="space-y-2 text-xs text-[#7A6F6B]">
              <li className="flex items-center space-x-1.5"><ShieldCheck className="w-3.5 h-3.5 text-[#27AE60]" /><span>FSSAI Hygiene Certified</span></li>
              <li><span>Atomic Concurrency Guarantee</span></li>
              <li><span>Zero Artificial Preservatives</span></li>
              <li><span>Direct Cook Earnings Protection</span></li>
            </ul>
          </div>

          <div>
            <h4 className="font-bold text-sm text-[#2D2522] mb-3">GitHub Pages Ready</h4>
            <p className="text-xs text-[#7A6F6B] mb-3">
              Built as a modern React & Vite web app ready for instant deployment to GitHub Pages.
            </p>
            <div className="flex items-center space-x-2 text-xs font-semibold text-[#D95338]">
              <Code className="w-4 h-4" />
              <span>Static dist build configured</span>
            </div>
          </div>

        </div>

        <div className="border-t border-[#EFECE6] pt-8 flex flex-col sm:flex-row items-center justify-between text-xs text-[#7A6F6B]">
          <p>© {new Date().getFullYear()} Rasoi / CookFree Platform. All rights reserved.</p>
          <p className="flex items-center space-x-1 mt-2 sm:mt-0">
            <span>Crafted with</span>
            <Heart className="w-3.5 h-3.5 text-[#D95338] fill-current" />
            <span>for authentic home food lovers</span>
          </p>
        </div>
      </div>
    </footer>
  );
}
