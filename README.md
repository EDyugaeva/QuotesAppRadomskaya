# Overview

This project consists of 3 microservices and gateway.
1. Userservice is made to interact with user model:
* POST: ../user  - in user body should be name, email (unique) and password 
* GET: /user/{userId}

2. Quoteservice is made to interact with quotes. 

With adding quotes there are changes in userservice

* POST: ../quote - in request body should be content and user id
* PATCH: ../quote/{quoteId} - in request body should be content and user id
* DELETE: ../quote?quoteId={quoteId}&userId={userId}
* GET:../quote/{quoteId}
* GET:../quote/random - get random quote
* GET:../quote/best - top 10 best quotes
* GET:../quote/worst- top 10 worst quotes

3. Voteservice is made to interact with votes. 

Every vote is saved in the database with the id, date, grade and user (author of vote). 
Creating votes makes some changes in user and quote services

* POST: ../vote/up - up voting with body: userId, quoteId
* POST: ../vote/down - down voting with body: userId, quoteId
* GET: ../vote/result/{quoteId} - return result vote to quote
* GET: ../vote/vote/{quoteId} - return all votes from quote
* GET: ../vote/graph/{quoteId} - return map with date and result vote - to make a graph

4. Getaway connects all services and is an entry point in the program.

Database H2 is used here. Test with mockito to UserController. ControllerAdvice to handle exceptions inside microservices.

The project has docker-compose.yaml for local deploy. There is also Postman collection to check all endpoints.

# How to run

`docker-compose up`