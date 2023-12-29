CREATE TABLE urls
(
    id           BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255),
    description  VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_at       TIMESTAMP NULL,
    enabled      BOOLEAN   DEFAULT TRUE,
    click_count  INT       DEFAULT 0,
    short_url    VARCHAR(10),
    full_url     VARCHAR(255)
);

CREATE TABLE users
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    display_name VARCHAR(255),
    username     VARCHAR(50) UNIQUE,
    password     VARCHAR(100),
    token        VARCHAR(255) UNIQUE

    //add users' roles as enum
);

CREATE TABLE users_urls
(
    user_id BIGINT NOT NULL,
    url_id  BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (url_id) REFERENCES urls (id) ON DELETE CASCADE,

    UNIQUE (user_id, url_id)
);