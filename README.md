Foodpairing application is the REST API, which was created all by my own. It is about making food compositions including chosen dish with something to drink. <br />

Application contains of two layers:
- the first one is the backend layer, which was created with Java 11,
- the second one is the frontend layer performed with the usage of Vaadin framework.
  Frontend part was created only to show main functionality and to support backend testing.
  All endpoints were tested either with using Postman and with the help of frontend layer.
  I constantly try to improve the application, leveraging the knowledge I still acquire from different courses.

Application includes:
- SOLID, DRY, YAGNI principles,
- GET, POST, PUT, DELETE http methods,
- two external APIs were integrated ("Spoonacular", "TheCocktailDB"),
- two design patterns (builder, facade),
- unit tests with coverage 97% (in methods),
- MySQL database used as a storage system,
- scheduler (sending an e-mail information).

Main technologies, which were used:
Java 11, Spring Boot, Hibernate, Spring WEB, JUnit5, Mockito, MySQL, Vaadin.

Short description of project functionality:
- you search dish by key word from first external API and save it in local database,
- you create composition with chosen dish and random drink from second external API,
- if the drink is unacceptable, you can change it to the next random drink,
- after you create your composition, you can update the drink in composition and change its ingredients,
- you can comment compositions and also give feedback to the comments.
  In the future I will develop the API, adding new functionalities to it.

Backend project link: https://github.com/grkrajewski/foodpairing-backend - port: 8080 <br />
Frontend project link: https://github.com/grkrajewski/foodpairing-frontend - port: 8081

Either backend and fronted application should be downloaded and run locally. The project is based on local MySQL database.
After running projects, run your web browser and go to the page:
http://localhost:8081/foodpairing/main, which is the first page of application.