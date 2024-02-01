**Kirana Store Transaction Management Service**
Overview
This backend service, developed using Spring Boot, is designed to empower Kirana stores in managing their transaction registers efficiently.
The primary objective of this service is to facilitate the daily tracking of all credit and debit transactions, providing a comprehensive 
solution for effective financial record-keeping.

Features
Transaction Management: Allows Kirana store owners to record credit and debit transactions on a daily basis.
Comprehensive Reporting: Provides detailed reports on daily, weekly, and monthly transactions for better financial analysis.
Data Integrity: Ensures data integrity and consistency through robust transaction management processes.
Scalability: Built with scalability in mind to handle increasing transaction volumes as the business grows.
RESTful API: Exposes RESTful APIs for seamless integration with other systems or frontend applications.
Technologies Used
Spring Boot: Provides the foundation for building the backend application with ease.
Spring Data JPA: Simplifies database operations and ensures data persistence.
Hibernate: Manages object-relational mapping for database interactions.
MySQL/PostgreSQL: Used as the relational database to store transaction data.
Swagger: Generates interactive API documentation for easy reference.
**Record Transaction
Description
Record a new transaction.**

Endpoint
Method: POST
URL: /transactions/record
Content Type: application/json
**Request Body**
{
  "dateTime": "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
  "amount": 100.00,
  "currency": "INR",
  "type": "CREDIT"
}
**Response**
Status Code: 200 OK
Content Type: application/json
{
  "id": 1,
  "dateTime": "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
  "amount": 100.00,
  "currency": "INR",
  "type": "CREDIT"
}

**Get Daily Reports
Description
Get daily reports of transactions grouped by date.**
Endpoint
Method: GET
URL: /transactions/daily-reports
Query Parameter: targetCurrency
**Response**
Status Code: 200 OK
Content Type: application/json
{
  "2024-01-01": [
    {
      "id": 1,
      "dateTime": "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      "amount": 100.00,
      "currency": "INR",
      "type": "CREDIT"
    },
    {
      "id": 2,
      "dateTime": "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      "amount": 200.00,
      "currency": "USD",
      "type": "DEBIT"
    }
  ],
  "2024-01-02": [
    {
      "id": 3,
      "dateTime": "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      "amount": 150.00,
      "currency": "INR",
      "type": "CREDIT"
    }
  ]
}
