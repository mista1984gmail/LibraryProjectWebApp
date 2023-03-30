**Project**: library web application.

**About the project:** the library app is an app for automate the work of the library, accounting for all readers, books, controlling the timing of taking books and returning them to the library.

The project uses the following technologies:
1. **Docker** - for application deployment PostgreSQL, to work with the database, automatic initialization of the database.
   ![img.png](img/img1.png)
2. **Liquibase** - to track, manage, and enforce database schema changes.
3. **Maven** - to automate the assembly of projects.
4. **Spring Boot** - creating a backend part of the application.
5. **HTML** - creating a custom part of the application (frontend).
6. **Thymeleaf** - allows to store html templates on the server side and issue them by calling a specific code.
7. **CSS** - for describing the appearance of a document, it is responsible for how web pages look: the color of the background and decorative elements, the size and style of fonts.
8. **JavaScript** - to write a script to search for books and readers on the front.
9. **Log4j2** - is responsible for writing information to log files based on the specified logging levels. The main task of the logger is not to miss the event that needs to be written to the log file.
   ![img.png](img/img2.png)
10. **Mapstruct** - to generate code for passing data between different entities in the program. It helps to map objects from one entity to another.
11. **Validation** - to check the correctness of entering user data.
    ![img.png](img/img3.png)

    ![img.png](img/img4.png)
12. **Spring Security** - to ensure the safe use of the application. Granting rights and possible actions, taking into account the role that the user has.
    ![img.png](img/img5.png)
13. **Swagger** - for documenting application endpoints.
    ![img.png](img/img6.png)
14. **Localization** - change the application language. Two languages are available: English and Russian.
    ![img.png](img/img7.png)   

    ![img.png](img/img8.png)  
15. **Spring-boot-starter-mail** - work with e-mail, email confirmation during registration and sending an email if the book is overdue.
    ![img.png](img/img9.png)
    ![img.png](img/img10.png)
    ![img.png](img/img11.png)
16. **Testing** - unit tests to test the service layer and integration tests to test the controller layer using Mockito framework.

**Project description:** 

The database schema is shown in the following image:
    ![img.png](img/img12.png)

There are three roles in the application: ADMIN, USER and LIBRARIAN.

**Administrator functionality.**

 - After logging in to the application, the following page is visible under the ADMIN role.
    ![img.png](img/img13.png)
 - Admin can see all users and search by first name, last name or email.
    ![img.png](img/img14.png)
 - The administrator can edit user data, except for the username, email, password and telephone fields. Assign and change roles to users, as well as block the user, after which he will not be able to enter the application.
    ![img.png](img/img15.png)

**Reader functionality.**

 - After logging in to the application, the following page is visible under the USER role.
    ![img.png](img/img16.png)

 - Reader can edit information about himself in his account.
    ![img.png](img/img17.png)
 - Reader can see all free books.
   ![img.png](img/img18.png)
 - Search for a book by title, author, year of publication of the book.
   ![img.png](img/img19.png)
 - Reader can booking the book he needs, if it is free and it will be available in the library within two days. He can go to the library and take it.
 - The reader can see the books that are in "his hands", as well as by the booking status, books that are simply reserved.
   ![img.png](img/img20.png)
    