# Regular payments system

## Table of Contents

- [Description](#description)
- [Swagger](#swagger)
- [Quick Start](#quick-start)

## Description {#description}
Regular payment is a payment that is debited according to the regulations (every
specified period)

The system consists of the following parts:
- Data handling service (DAO Rest-API), which is a separate WEB service
    - Core functionality:
      - CRUD payment methods:
        * Create Update Update Delete
        * Method of receiving payment by ID
        * Method of obtaining the list of payments by INN
        * Method of obtaining the list of payments by OKPO
      - CRUD posting methods
        * Create Create Update Delete
        * Method of getting a posting by ID
        * Method of obtaining the list of postings by payment

- Business logic service (Rest-API)
    - Core functionality:
        * Creating a payment
          Validation of incoming data
        * Charging the payment
          * Creation of payment posting
          * Checking whether the debit is necessary
          * Reversing the posting (deleting)
       * Obtaining data
          * Receiving all payments for the Payer
          * Receiving all payments by recipient
          * Obtaining payment debit history

- A regulatory module is a console application that utilizes the Business Logic Service 
  and starts the debit process on startup:
  * Selects all payments
  * For each payment, check the date/time of the last withdrawal, if
  current date/time is more than Date/Time of debit + withdrawal period,
  then create payment withdrawal.


## Swagger
Each service has swagger.
The Swagger UI provides a user-friendly interface for exploring and testing the API endpoints. Navigate through the available resources and endpoints to understand the functionalities provided by the API.
### Accessing Swagger UI
To view the Swagger documentation ensure that the project is up and running.

Next, open your web browser and navigate to the following URL:
```
http://[actual_host]:[actual_port]/swagger-ui.html
```

Replace [actual_host] with the actual host where your application is running, and [actual_port] with the actual port.

For example:
```
http://localhost:8081/swagger-ui/index.html
```

## Quick Start {#quick-start}
### Prerequisites
Ensure the following software is installed on your system:
- Docker
- Java 17
- Maven

### Getting Started

1. Clone repository to your local machine:

2. Start the services using Docker Compose:

    ```bash
    docker-compose up -d
    ```

   This command will launch the defined services in detached mode (`-d`), allowing them to run in the background.

If you want to run one of the services separately you can move under service directory and use such command:
 ```bash
docker build -t payment-business-service .
docker run -d --name payment-business-service -p 8083:8080 payment-business-service
```
Adjust external port as needed. 

3. How to start payment regulation application

- Navigate to the 'payment-regulation' Directory:
  Move to the 'payment-regulation' directory using the following command:
  ```bash 
    cd payment-regulation
   ```
- Build the Application:
  Execute the following Maven command in the console to clean, compile, and package the application:
   ```bash
    mvn clean package
    ```
- Navigate to the 'target' Directory:
  Move to the 'target' directory:
  ```bash
  cd target
   ```
- Run the Application:
  Execute the following command to start the Payment Regulation Application:
     ```bash
    java -jar PaymentRegulation-0.0.1-SNAPSHOT.jar
    ```

Please note that before launching payment regulation, you need to start other services in the system.
### Accessing Services

After running your spring boot app, you can access next services:

- **dao-service:** Access at [http://localhost:8081](http://localhost:8081)
- **business-logic-service:** Access at [http://localhost:8082](http://localhost:8082)
- **postgres:** Access Postgres database on `localhost:5432` (if needed, adjust configurations as per your setup)

### Stopping Services

To stop the services when finished, run:

```bash
docker-compose down