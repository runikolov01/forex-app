# Forex-app
A foreign exchange application.

# Functionalities

**1. Exchange Rate Endpoint:**
- Input: A pair of currency codes (e.g., USD to EUR).
- Output: The current exchange rate between the two currencies.
- The method "getExchangeRate" in ExchangeRateController returns the current exchange rate between the two currencies.

_Through this example, it was found that at the time of the check, the dollar to euro exchange rate was 0.9549:_
![image](https://github.com/user-attachments/assets/63d9d431-1402-44bc-beeb-b0df922391e6)

**2. Currency Conversion Endpoint:**
- Input: An amount in the source currency, source currency code, and target currency code.
- Output: The converted amount in the target currency and a unique transaction identifier. 
- The method "convertCurrency" in ExchangeRateController returns the converted amount in the target currency and a unique transaction identifier, which is generated in the ConversionResponse class.


In this example the endpoint converts 100 USD to EUR using the exchange rate of 0.9544:
![image](https://github.com/user-attachments/assets/00e4ebea-0404-43a3-ad7d-1943480297f7)




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


