create TABLE users(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    username     VARCHAR(50) NOT NULL,
    password     VARCHAR(100) NOT NULL,
    token        VARCHAR(255) UNIQUE
);

create TABLE urls(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255),
    description  VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_at       TIMESTAMP NULL,
    enabled      BOOLEAN   DEFAULT TRUE,
    click_count  INT       DEFAULT 0,
    short_url    VARCHAR(10),
    full_url     VARCHAR(255),
    id_user  BIGINT NOT NULL,
    foreign key (id_user) references users(id)
);

