# Forex-app
A foreign exchange application.

# Functionalities

**1. Exchange Rate Endpoint:**
- Input: A pair of currency codes (e.g., USD to EUR).

- Output: The current exchange rate between the two currencies.

_The method "getExchangeRate" in ExchangeRateController returns the current exchange rate between the two currencies._
![image](https://github.com/user-attachments/assets/63d9d431-1402-44bc-beeb-b0df922391e6)

**2. Currency Conversion Endpoint:**
- Input: An amount in the source currency, source currency code, and target
currency code.
- Output: The converted amount in the target currency and a unique transaction
identifier. 


3. Conversion History Endpoint:
- Input: A transaction identifier or a transaction date for filtering purposes (at
least one must be provided).
- Output: A paginated list of currency conversions filtered by the provided
criteria.

4. External Exchange Rate Integration:
- The application must utilize an external service provider for fetching exchange
rates and optionally for performing the currency conversion calculations.

5. Error Handling:
- Errors must be handled gracefully, providing meaningful error messages and
specific error codes.
