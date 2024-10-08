
CREATE TABLE users (
                         id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         username VARCHAR(255) UNIQUE NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(255)
);


CREATE TABLE projects (
                            id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            intro VARCHAR(255),
                            owner_id INT UNSIGNED NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            start_date DATE,
                            end_date DATE,
                            FOREIGN KEY (owner_id) REFERENCES users(id)
);


CREATE TABLE project_members (
                                   id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                   project_id INT UNSIGNED,
                                   user_id INT UNSIGNED,
                                   FOREIGN KEY (project_id) REFERENCES projects(id),
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);