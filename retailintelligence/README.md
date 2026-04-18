# RetailIntelligence

AI retail operations platform. Demand forecasting at SKU level with seasonality and trend modeling. Dynamic pricing recommendations with margin guardrails. Inventory optimization for reorder points and quantities.

## Run Locally

```sh
# Start platform (postgres, litellm)
docker-compose up -d

# Start backend
cd backend
mvn clean install
mvn spring-boot:run

# Start frontend
cd frontend
npm start &
```
