"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { fetchAppDetails, fetchAppReviews, submitAppReview } from "../../../lib/api";

export default function AppDetailsPage() {
    const params = useParams();
    const router = useRouter();
    const id = params.id as string;

    const [app, setApp] = useState<any>(null);
    const [reviews, setReviews] = useState<any[]>([]);
    const [rating, setRating] = useState(5);
    const [reviewText, setReviewText] = useState("");

    useEffect(() => {
        if (id) {
            loadAppDetails();
            loadReviews();
        }
    }, [id]);

    const loadAppDetails = async () => {
        try {
            const data = await fetchAppDetails(id);
            setApp(data);
        } catch (e) {
            console.error(e);
        }
    };

    const loadReviews = async () => {
        try {
            const data = await fetchAppReviews(id);
            setReviews(data);
        } catch (e) {
            console.error(e);
        }
    };

    const handleReviewSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await submitAppReview(id, { rating, reviewText, userId: "user-123" });
            alert("Review submitted successfully and is pending approval.");
            setReviewText("");
        } catch (e) {
            console.error(e);
        }
    };

    if (!app) return <div>Loading...</div>;

    return (
        <main className="p-8">
            <button onClick={() => router.push("/")} className="mb-4 text-blue-500">Back to Catalog</button>
            <h1 className="text-3xl font-bold mb-2">{app.name}</h1>
            <p className="text-gray-600 mb-4">{app.category}</p>
            <p className="mb-4">{app.description}</p>
            <button 
                onClick={() => router.push(`/apps/${id}/install`)} 
                className="bg-green-500 text-white px-4 py-2 rounded mb-8">
                Install App
            </button>

            <h2 className="text-2xl font-bold mb-4">Reviews</h2>
            <ul className="mb-8">
                {reviews.map(r => (
                    <li key={r.id} className="border-b py-2">
                        <strong>{r.rating} stars</strong> - {r.reviewText}
                    </li>
                ))}
            </ul>

            <h3 className="text-xl font-bold mb-2">Write a Review</h3>
            <form onSubmit={handleReviewSubmit}>
                <div className="mb-2">
                    <label className="block">Rating (1-5)</label>
                    <input 
                        type="number" 
                        min="1" max="5" 
                        value={rating} 
                        onChange={e => setRating(Number(e.target.value))} 
                        className="border p-2 rounded w-20"
                    />
                </div>
                <div className="mb-2">
                    <label className="block">Review</label>
                    <textarea 
                        value={reviewText} 
                        onChange={e => setReviewText(e.target.value)} 
                        className="border p-2 rounded w-full"
                    />
                </div>
                <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">Submit</button>
            </form>
        </main>
    );
}
