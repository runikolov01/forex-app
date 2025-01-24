# Forex-app _(foreign exchange application)_
This API provides currency conversion services, allowing users to convert amounts from one currency to another using the latest exchange rates. The service also allows users to retrieve conversion histories and get details about specific transactions.

# Installation and Setup

To run this project locally, follow these steps:

1. Clone the repository:
- git clone https://github.com/runikolov01/forex-app
- cd forex-app

2. Set up the dependencies:
- Make sure you have Java (JDK 17 or higher) installed.
- Use Maven to install dependencies: mvn clean install

3. Run the application:
- mvn spring-boot:run


# Functionalities

**1. Exchange Rate Endpoint:**
- **Input**: A pair of currency codes (e.g., USD to EUR).
- **Output**: The current exchange rate between the two currencies.
- The method "getExchangeRate" in ExchangeRateController returns the current exchange rate between the two currencies.
- GET http://localhost:8080/api/exchange-rate?fromCurrency=USD&toCurrency=EUR

_Through this example, it was found that at the time of the check, the dollar to euro exchange rate was 0.9529:_
![image](https://github.com/user-attachments/assets/4b26143b-aaa3-4716-9c35-3a002797ccc6)

**2. Currency Conversion Endpoint:**
- **Input**: An amount in the source currency, source currency code, and target currency code.
- **Output**: The converted amount in the target currency and a unique transaction identifier. 
- The method "convertCurrency" in ExchangeRateController returns the converted amount in the target currency and a unique transaction identifier, which is generated in the ConversionResponse class.
- POST http://localhost:8080/api/currency-conversion/convert?fromCurrency=USD&toCurrency=EUR&amount=200

_In this example the endpoint converts 200 USD to EUR using the exchange rate of 0.9529:_
![image](https://github.com/user-attachments/assets/e5bcb9ec-62e0-48ea-a4c7-ff21e71bb0d0)

_And the result is saved in the database:_
![image](https://github.com/user-attachments/assets/c2fe14f9-738e-4301-83fb-a0e5dbf9b454)

**3. Conversion History Endpoint:**
- **Input**: A transaction identifier or a transaction date for filtering purposes (at least one must be provided).
- **Output**: A paginated list of currency conversions filtered by the provided
criteria.
- GET http://localhost:8080/api/currency-conversion/history?transactionDate=2025-01-24&page=0&size=5

_This will return the first page (page=0) with 5 conversions (size=5) that occurred on 2025-01-24:_
![image](https://github.com/user-attachments/assets/5c3403a4-cc90-4215-a190-cb8e9d4d8845)
![image](https://github.com/user-attachments/assets/18c19aff-0d9e-4491-b849-79e03fdb51de)
_The result is visible in the database:_
![image](https://github.com/user-attachments/assets/eb9000e3-e934-4dfd-b51d-cb2d61d67337)

**4. External Exchange Rate Integration:**
- The external exchange rate integration is implemented with the ExternalAPIClient class. This class fetches exchange rates from the external Fixer API and performs the necessary calculations for currency conversion:
![image](https://github.com/user-attachments/assets/6285b4ab-3a1c-43f4-88b2-78982a49bfcc)


**5. Error Handling:**
- Errors are handled gracefully, providing meaningful error messages and specific error codes.

**6. Unit Testing:**
- Unit tests are created to ensure the reliability and robustness of the application:
![image](https://github.com/user-attachments/assets/2e6d0936-af37-4e58-88e3-38a79970b5e4)


