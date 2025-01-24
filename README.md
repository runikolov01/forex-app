# Forex-app
A foreign exchange application.

# Functionalities

**1. Exchange Rate Endpoint:**
- Input: A pair of currency codes (e.g., USD to EUR).
- Output: The current exchange rate between the two currencies.
- The method "getExchangeRate" in ExchangeRateController returns the current exchange rate between the two currencies.

_Through this example, it was found that at the time of the check, the dollar to euro exchange rate was 0.9529:_
![image](https://github.com/user-attachments/assets/4b26143b-aaa3-4716-9c35-3a002797ccc6)

**2. Currency Conversion Endpoint:**
- Input: An amount in the source currency, source currency code, and target currency code.
- Output: The converted amount in the target currency and a unique transaction identifier. 
- The method "convertCurrency" in ExchangeRateController returns the converted amount in the target currency and a unique transaction identifier, which is generated in the ConversionResponse class.

_In this example the endpoint converts 200 USD to EUR using the exchange rate of 0.9529:_
![image](https://github.com/user-attachments/assets/e5bcb9ec-62e0-48ea-a4c7-ff21e71bb0d0)

_And the result is saved in the database:_
![image](https://github.com/user-attachments/assets/c2fe14f9-738e-4301-83fb-a0e5dbf9b454)



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


