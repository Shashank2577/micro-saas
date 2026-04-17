export const apiClient = async (url: string, options: RequestInit = {}) => {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8150";
  return fetch(`${baseUrl}${url}`, options);
};

export interface EcosystemApp {
    app: string;
    displayName: string;
    version: string;
    baseUrl: string;
}
