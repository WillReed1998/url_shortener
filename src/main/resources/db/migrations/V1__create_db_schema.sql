create table if not exists users (
                                     id int auto_increment primary key ,
                                     username varchar(25) not null unique,
                                     password varchar(500) not null,
                                     token varchar(500) not null,
                                     id_url int,
                                     foreign key (id_url) references urls(id)
);

create table if not exists urls(
                                   id int auto_increment primary key,
                                   title varchar(500) not null,
                                   description varchar(500) not null,
                                   full_url varchar(500) not null,
                                   short_url varchar(8) not null,
                                   create_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   end_at TIMESTAMP not null,
                                   click_count bigint
);