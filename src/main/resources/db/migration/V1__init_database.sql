CREATE TABLE roles (

                       id BIGSERIAL PRIMARY KEY,

                       name VARCHAR(50) NOT NULL UNIQUE

);



CREATE TABLE users (

                       id BIGSERIAL PRIMARY KEY,

                       first_name VARCHAR(100) NOT NULL,

                       last_name VARCHAR(100) NOT NULL,

                       email VARCHAR(150) NOT NULL UNIQUE,

                       password VARCHAR(255) NOT NULL,

                       phone_number VARCHAR(20),

                       enabled BOOLEAN NOT NULL DEFAULT FALSE,

                       created_at TIMESTAMP NOT NULL,

                       updated_at TIMESTAMP NOT NULL

);



CREATE TABLE user_roles (

                            user_id BIGINT NOT NULL,

                            role_id BIGINT NOT NULL,


                            PRIMARY KEY(user_id, role_id),


                            CONSTRAINT fk_user_roles_user

                                FOREIGN KEY(user_id)

                                    REFERENCES users(id)

                                    ON DELETE CASCADE,


                            CONSTRAINT fk_user_roles_role

                                FOREIGN KEY(role_id)

                                    REFERENCES roles(id)

                                    ON DELETE CASCADE

);



CREATE TABLE refresh_tokens (

                                id BIGSERIAL PRIMARY KEY,

                                token VARCHAR(500) NOT NULL UNIQUE,

                                expiry_date TIMESTAMP NOT NULL,

                                user_id BIGINT NOT NULL,


                                CONSTRAINT fk_refresh_token_user

                                    FOREIGN KEY(user_id)

                                        REFERENCES users(id)

                                        ON DELETE CASCADE

);



CREATE TABLE audit_logs (

                            id BIGSERIAL PRIMARY KEY,

                            user_id BIGINT NOT NULL,

                            action VARCHAR(100) NOT NULL,

                            timestamp TIMESTAMP NOT NULL,


                            CONSTRAINT fk_audit_user

                                FOREIGN KEY(user_id)

                                    REFERENCES users(id)

                                    ON DELETE CASCADE

);