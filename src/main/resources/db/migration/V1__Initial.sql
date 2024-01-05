create TABLE urls(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255),
    description  VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_at       TIMESTAMP NULL,
    enabled      BOOLEAN   DEFAULT TRUE,
    click_count  INT       DEFAULT 0,
    short_url    VARCHAR(10),
    full_url     VARCHAR(255)
);

create TABLE users(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    username     VARCHAR(50) NOT NULL,
    password     VARCHAR(100) NOT NULL,
    token        VARCHAR(255) UNIQUE
);

create TABLE users_urls(
    user_id BIGINT NOT NULL,
    url_id  BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id) ON delete CASCADE,
    FOREIGN KEY (url_id) REFERENCES urls (id) ON delete CASCADE,

    UNIQUE (user_id, url_id)
);


