# Known issues
- Customer can't connect with Products and with Transactions.

## Stept to reproduce
*a. steps to debug*
1. Disable eureka and configserver on product and customer
```.properties
eureka.client.register-with-eureka=false
spring.cloud.config.enabled=false
eureka.client.fetch-registry=false
```
2. Change the uri to product service in `WebClientProductClient` class
```java
    public static final String PRODUCT_SERVICE = "http://localhost:8081";
```
3. Start the services in the following order:
- Product
- Customer

4. create a new [product](http://localhost:8081/swagger-ui.html) and [customer](http://localhost:8080/business/V2/swagger-ui.html)
```json
{
  "id": 0,
  "code": "TA01",
  "name": "Tarjeta Credito"
}
```
```json
{
  "id": 0,
  "code": "CU02",
  "name": "Cuenta corriente"
}
```
```json
{
    "id": 1,
    "code": "CU02",
    "iban": "ES7620770024003102575766",
    "name": "John",
    "surname": "Doe",
    "phone": "+34123456789",
    "address": "123 Main St, Madrid, Spain",
    "products": [
        {
            "id": 1        
        }
    ]
}
```
5. Check the logs of the services to see if there is any error.
a. Customer logs
```java
2024-11-13T11:58:31.473+01:00  WARN 93553 --- [businessDomain-customer] [nio-8080-exec-1] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [BusinessRuleException(id=0, code=1027, httpStatus=412 PRECONDITION_FAILED)]
```
First Found, the CustomerRestcontrollerHelper is calling a method that returning a unknounException , I can't understand I procedwith the following steps.
Change to call teh correct method.
```java
//after
  //String productName = getProductName(customerProduct.getId());
    String productName = getNotFoundProductName(customerProduct.getId());
//before
    String productName = getProductName(customerProduct.getId());
    // String productName = getNotFoundProductName(customerProduct.getId());
```
Then we found the deep error 
```java
2024-11-13T11:20:13.197+01:00  INFO 62720 --- [businessDomain-customer] [io-8080-exec-10] c.p.c.h.product.WebClientProductClient   : WebClient product client
2024-11-13T11:20:23.474+01:00  WARN 62720 --- [businessDomain-customer] [io-8080-exec-10] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [reactor.core.Exceptions$RetryExhaustedException: Retries exhausted: 5/5]
2024-11-13T11:20:23.474+01:00 ERROR 62720 --- [businessDomain-customer] [io-8080-exec-10] c.p.c.h.customer.CustomerHandler         : Error getting products
```

```json
{
  "type": "TECNICO",
  "title": "I/O Error",
  "code": "1024",
  "detail": "Retries exhausted: 5/5",
  "instance": ""
}
```
## Analysis Conclusions

**It's possible that the retry logic isn't reaching the server, resulting in a backlog of failed attempts and a 500 error.**

- **Retry with exponential backoff limited to 3 attempts:** This reduces the number of attempts to a more manageable number, with an exponential wait interval between retries.
- **Filter to exclude non-retriable errors:** Specify exceptions that should not be retried, such as authentication issues.
- **Detailed logging of retries:** Each failed attempt is logged with details, making debugging easier and providing context for each attempt.
- **Detailed error handling:** Log I/O errors specifically, as well as general handling for other possible errors.

Affter this impruvement, the customer return the correct error message:
```json
{
  "type": "TECNICO",
  "title": "I/O Error",
  "code": "1024",
  "detail": "404 Not Found from GET http://localhost:8081/productgetName/1",
  "instance": ""
}
```
## Actualy error
The error is that the customer is trying to get the name of the product, but the product service doesn't have a method to get the name of the product by id. The customer service should store the name of the product when it creates a new customer.

## Solution
Fix the path at variable `PRODUCT_GETNAME_SERVICE` in `WebClientProductClient` class
```java 
   public static final String PRODUCT_GETNAME_SERVICE = "/getName";
```

## Expected result
The customer service should retrive the name of the product and it creates a new customer, 
returning the following response:
```http
http status = 200 OK
```
```json
{
  "id": 1,
  "code": "CU02",
  "iban": "ES7620770024003102575766",
  "name": "John",
  "surname": "Doe",
  "phone": "+34123456789",
  "address": "123 Main St, Madrid, Spain",
  "products": [
    {
      "id": 1,
      "productId": 0,
      "productName": null
    }
  ],
  "transactions": null
}
```

## Additional information
Affter the fix, you should enable the eureka and configserver on product and customer and change the product uri to `http://product-service` in `WebClientProductClient` class
```.properties
eureka.client.register-with-eureka=true
spring.cloud.config.enabled=true
eureka.client.fetch-registry=true
```
```java
    public static final String PRODUCT_SERVICE = "http://BUSINESSDOMAIN-PRODUCT/product";
```

## Load balancer
1. Start the services in the following order:
- Eureka
```shell
# cd to the root of the project
java -jar paymentchainparent/infrastructure/eurekaServer/target/eurekaServer-0.0.1-SNAPSHOT.jar
```
- Config server if apply
```shell
# cd to the root of the project
java -jar paymentchainparent/infrastructure/configServer/target/configServer-0.0.1-SNAPSHOT.jar
```
- Product
```shell
# cd to the root of the project
java -jar paymentchainparent/businessdomain/product/target/product-0.0.1-SNAPSHOT.jar 
``` 
- Customer
```shell
# cd to the root of the project
java -jar paymentchainparent/businessdomain/customer/target/customer-0.0.1-SNAPSHOT.jar
```
2. Browse to [Eureka](http://localhost:8761) to see the services registered
3. Create a new [product](http://localhost:8081/swagger-ui.html) and [customer](http://localhost:8080/business/V2/swagger-ui.html)
```json
{
  "id": 0,
  "code": "TA01",
  "name": "Tarjeta Credito"
}
```
```json
{
  "id": 0,
  "code": "CU02",
  "name": "Cuenta corriente"
}
```
```json
{
    "id": 1,
    "code": "CU02",
    "iban": "ES7620770024003102575766",
    "name": "John",
    "surname": "Doe",
    "phone": "+34123456789",
    "address": "123 Main St, Madrid, Spain",
    "products": [
        {
            "id": 1        
        }
    ]
}
```
4. Check the logs of the services to see if there is any error.
At this case tehre found several erros triying to connect to product across the eureka server, because the webclinet was not configured properly and webClientProductClient was not using the correct instance the load balancer, it was using the instance created when CustomerRestControllerHelper call teh method getProductName.
The Solution was inject WebClient.Builder on CustomerRestControllerHelper, it was created on CustomerConfiguration class as bean.