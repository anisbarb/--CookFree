import React, { useState } from 'react';
import { Star, X, CheckCircle } from 'lucide-react';

export function ReviewModal({ order, onClose, onSubmitReview }) {
  const [rating, setRating] = useState(5);
  const [reviewText, setReviewText] = useState('');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
    setTimeout(() => {
      onSubmitReview(order.id, rating, reviewText);
      onClose();
    }, 1200);
  };

  return (
    <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl max-w-md w-full shadow-2xl overflow-hidden border border-[#EFECE6] p-6 text-center">
        
        {submitted ? (
          <div className="py-8 space-y-4">
            <CheckCircle className="w-16 h-16 text-[#27AE60] mx-auto animate-bounce" />
            <h3 className="font-serif font-bold text-xl text-[#2D2522]">Thank You!</h3>
            <p className="text-xs text-[#7A6F6B]">Your rating has been shared with {order.assignedCookName}.</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="flex items-center justify-between">
              <h3 className="font-serif font-bold text-lg text-[#2D2522]">Rate Your Meal</h3>
              <button type="button" onClick={onClose} className="p-2 rounded-xl bg-[#FDFBF7] hover:bg-[#EFECE6]">
                <X className="w-4 h-4 text-[#7A6F6B]" />
              </button>
            </div>

            <p className="text-xs text-[#7A6F6B]">How was your homestyle {order.baseName} & {order.preparationName} from {order.assignedCookKitchen}?</p>

            {/* Stars */}
            <div className="flex items-center justify-center space-x-2">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  type="button"
                  key={star}
                  onClick={() => setRating(star)}
                  className="p-1 focus:outline-none transition-transform hover:scale-110"
                >
                  <Star className={`w-8 h-8 ${star <= rating ? 'text-[#F2994A] fill-current' : 'text-[#EFECE6]'}`} />
                </button>
              ))}
            </div>

            <textarea
              value={reviewText}
              onChange={(e) => setReviewText(e.target.value)}
              placeholder="Write a compliment (e.g., Exactly like grandma's cooking! Piping hot and less oil...)"
              className="w-full bg-[#FDFBF7] border border-[#EFECE6] rounded-2xl p-4 text-xs focus:outline-none focus:border-[#D95338] h-28 resize-none"
            />

            <button
              type="submit"
              className="w-full bg-[#D95338] text-white font-bold py-3.5 rounded-2xl shadow-lg shadow-[#D95338]/20 hover:bg-[#B83D24] transition-all"
            >
              Submit Review & Complete
            </button>
          </form>
        )}

      </div>
    </div>
  );
}
