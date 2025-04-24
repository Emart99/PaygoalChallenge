# Paygoal Challenge
[![codecov](https://codecov.io/gh/Emart99/PaygoalChallenge/branch/main/graph/badge.svg)](https://codecov.io/gh/Emart99/PaygoalChallenge)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
### Requerimientos 
* jdk 11
* Maven

### Como correrlo sin ide
     git clone https://github.com/Emart99/PaygoalChallenge.git
     cd PaygoalChallenge
     mvn clean install
     mvn spring-boot:run

### Documentacion
Mientras el proyecto esta ejecutandose,  
ingresar al siguiente enlace,  
donde podra ver todos los posibles endpoints  
http://localhost:8080/swagger-ui/index.html


    
### Como correr los tests
     En el path del proyecto
     mvn clean test jacoco:report
     Luego para ver el reporte debe dirigirse a
     /target/site/jacoco/index.html

### Algunos comentarios
Hay tests en el path src -> tests -> java -> ...  
Como base de datos use H2.  
Para documentar Swagger.  
Para el manejo de excepciones hice un GlobalExceptionHandler.  
Jacoco para el coverage.    
