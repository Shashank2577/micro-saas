export const BASE_URL = "http://localhost:8108/api/v1";

export async function fetchApps(category?: string, searchText?: string) {
    const queryParams = new URLSearchParams();
    if (category) queryParams.append("category", category);
    if (searchText) queryParams.append("searchText", searchText);
    
    const res = await fetch(`${BASE_URL}/apps?${queryParams.toString()}`);
    if (!res.ok) throw new Error("Failed to fetch apps");
    return res.json();
}

export async function fetchTrendingApps() {
    const res = await fetch(`${BASE_URL}/apps/trending`);
    if (!res.ok) throw new Error("Failed to fetch trending apps");
    return res.json();
}

export async function fetchAppDetails(id: string) {
    const res = await fetch(`${BASE_URL}/apps/${id}`);
    if (!res.ok) throw new Error("Failed to fetch app details");
    return res.json();
}

export async function fetchAppReviews(id: string) {
    const res = await fetch(`${BASE_URL}/apps/${id}/reviews`);
    if (!res.ok) throw new Error("Failed to fetch app reviews");
    return res.json();
}

export async function installApp(id: string, workspaceId: string, permissions: string[]) {
    const res = await fetch(`${BASE_URL}/apps/${id}/install`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ workspaceId, permissions })
    });
    if (!res.ok) throw new Error("Failed to install app");
    return res.json();
}

export async function submitAppReview(id: string, review: { rating: number, reviewText: string, userId: string }) {
    const res = await fetch(`${BASE_URL}/apps/${id}/reviews`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(review)
    });
    if (!res.ok) throw new Error("Failed to submit review");
    return res.json();
}
