"use client";

import { useState, useEffect } from 'react';
import api from '../../lib/api';

interface CustomerReview {
  id: string;
  source: string;
  rating: number;
  content: string;
  sentiment: string;
  operationalInsight: string;
}

export default function ReviewIntelligence() {
  const [reviews, setReviews] = useState<CustomerReview[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchReviews();
  }, []);

  const fetchReviews = async () => {
    try {
      const res = await api.get('/api/v1/reviews');
      setReviews(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const analyzeReview = async (id: string) => {
    try {
      await api.post(`/api/v1/reviews/${id}/analyze`);
      fetchReviews();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-5xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Review Intelligence</h1>

      <div className="space-y-4">
        {reviews.length === 0 ? (
          <p className="text-gray-500">No reviews found.</p>
        ) : (
          reviews.map(review => (
            <div key={review.id} className="bg-white p-5 rounded-lg shadow border border-gray-200">
              <div className="flex justify-between items-start mb-2">
                <div className="flex items-center space-x-2">
                  <span className="font-semibold text-lg">{review.source}</span>
                  <span className="text-yellow-500">{"★".repeat(review.rating)}{"☆".repeat(5-review.rating)}</span>
                </div>
                {review.sentiment ? (
                  <span className={`px-2 py-1 rounded text-xs font-medium ${
                    review.sentiment === 'POSITIVE' ? 'bg-green-100 text-green-800' :
                    review.sentiment === 'NEGATIVE' ? 'bg-red-100 text-red-800' :
                    'bg-gray-100 text-gray-800'
                  }`}>
                    {review.sentiment}
                  </span>
                ) : (
                  <button onClick={() => analyzeReview(review.id)} className="text-sm bg-blue-100 text-blue-700 px-2 py-1 rounded hover:bg-blue-200">
                    Analyze
                  </button>
                )}
              </div>
              <p className="text-gray-700 mb-3">"{review.content}"</p>

              {review.operationalInsight && (
                <div className="bg-indigo-50 p-3 rounded text-sm border border-indigo-100">
                  <span className="font-semibold text-indigo-900">AI Insight:</span> <span className="text-indigo-800">{review.operationalInsight}</span>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
