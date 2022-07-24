# IntelliStart


## About
In this project, I implemented the advanced system requirements of the task, which can be found at the [link](https://drive.google.com/file/d/1X1dHmsvP_Vy9UzqKDLbQ-d8AD3kFRVaO/view).
This is a simple REST API that allows the user to buy products, according to the requirements that were prescribed in the link above.

## Start
In order to fully use the application, you need to specify your username, password and database name (```replace spring.datasource.url``` and ```spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation``` as well as ```spring.jpa.properties.hibernate.dialect``` if you don't have PostgreSQL)
You can also fill the tables with test values, for example, by creating three products and three users.
To do this, you can use the following code:
```
insert into users values(1, 25, 'First', 'Customer');
insert into users values(2, 50, 'Second', 'Customer');
insert into users values(3, 75, 'Third', 'Customer');
insert into products values(1, 'first_product', 10);
insert into products values(2, 'second_product', 75);
insert into products values(3, 'third_product', 100);
```

##### Important: These SQL queries should be used only after the program starts, when Hibernate creates the corresponding tables.











