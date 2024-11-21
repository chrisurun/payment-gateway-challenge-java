# Payment Gateway

## Requirements
- JDK 17
- Docker

## Setup
- Run Docker then create a container using the docker-compose file
- Import the API collection in Postman
- Run the application

## API Documentation
For documentation openAPI is included, and it can be found under the following url: **http://localhost:8090/swagger-ui/index.html**

## Usage

### Create payment endpoint
POST http://localhost:8090/payment

**Example request**
```json
{
  "cardNumber": "2222405343248877",
  "cvv": "123",
  "expiryMonth": 4,
  "expiryYear": 2025,
  "currency": "GBP",
  "amount": 100
}

```

**Example response**
```json
{
  "id": "5a5dab4d-b07c-41a9-a1a8-f7c96c609cb5",
  "status": "Authorized",
  "cardNumberLastFour": "8877",
  "expiryMonth": 4,
  "expiryYear": 2025,
  "currency": "GBP",
  "amount": 100
}
```

### Get payment endpoint
POST http://localhost:8090/payment/{{paymentId}}

**Example response**
```json
{
  "id": "19f3c9f1-1384-45c2-ac36-b41f90111446",
  "status": "Authorized",
  "cardNumberLastFour": "8877",
  "expiryMonth": 4,
  "expiryYear": 2025,
  "currency": "GBP",
  "amount": 100
}
```

**Note**:
- The unauthorized scenario cannot be tested as the bank simulator expects an invalid card number.
- The payment gateway will return a 400 status code if the request is invalid.
- The payment gateway will return a 500 status code if the bank simulator cannot handle the request.

## Possible improvements
- Merchant accounts with API authentication
- Persistent storage for payments
- More detailed Swagger documentation with examples
