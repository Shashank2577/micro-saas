import requests

class ContextClient:
    def __init__(self, api_key: str, base_url: str = "http://localhost:8136/api"):
        self.api_key = api_key
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({"X-App-Id": api_key})

    def get_context(self, customer_id: str):
        response = self.session.get(f"{self.base_url}/customers/{customer_id}/context")
        response.raise_for_status()
        return response.json()

    def update_context(self, customer_id: str, updates: dict):
        response = self.session.put(f"{self.base_url}/customers/{customer_id}/context", json=updates)
        response.raise_for_status()
        return response.json()
