CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       firstname VARCHAR(255) not null ,
                       lastname VARCHAR(255) not null ,
                       username VARCHAR(255) unique not null,
                       password VARCHAR(255) not null ,
                       role VARCHAR(255)
);
