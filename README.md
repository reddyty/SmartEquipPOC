# SmartEquipPOC
It is microservice application, deloved using spring-boot. 
It is developed using following API's
1.jdk1.8
2.Spring-boot-starter-web -version - 2.4.2
3.springfox-swagger2 -version -2.4.0
4.springfox-swagger-ui -version -2.4.0
5.jjwt(json-webtoken) - versioin - 0.9.1 

Application provides following services.
1. /api/clientMsg, it is get call
2. /api/generateMsg, it is a post call, which generates random 5 numbers along with token
3. /api/finalMsg, it is a post call to validate the total of the given 5 random numbers

Used JSWtoken to validate the request, generating the token in /api/generateMsg flow and adding the token in response header.
The client has to send back the token as request header("token") while calling /finalMsg request.

The token is generated using following propeties.
# JWT Token properties
1.subject    : Questions to send
2.Algorithm  : SignatureAlgorithm.HS512
3.Secret key : M@r1@b$

# Test cases
Covered all the test scenarios and added in src/test/java/com/addnumbers/AdditionControllerTest.java, all the test cases are working fine.

# Utilities
Have added swagger api documentation, http://localhost:8888/swagger-ui.html is the link to access the swagger console.

# Postman collection
Plese find postman collection details following location.
https://github.com/reddyty/SmartEquipPOC/blob/main/addition_smartequip.json

