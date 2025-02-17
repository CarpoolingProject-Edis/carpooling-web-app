create table cars
(
    id           int auto_increment
        primary key,
    model        varchar(32)                                                                    not null,
    year         int                                                                            not null,
    vehicle_type enum ('NONE', 'SEDAN', 'SUV', 'ELECTRIC', 'COMBI', 'MINIVAN', 'JEEP', 'OTHER') not null
);

create table photos
(
    id        int auto_increment
        primary key,
    photo_url varchar(200) not null
);

create table users
(
    id           int auto_increment
        primary key,
    firstName    varchar(20)                     not null,
    lastName     varchar(20)                     not null,
    Email        varchar(255)                    not null,
    Username     varchar(20)                     not null,
    Password     varchar(20)                     not null,
    phoneNumber  varchar(10)                     not null,
    car_id       int                             null,
    photo_url_id int                             null,
    rating       double                          null,
    role         enum ('ADMIN', 'USER', 'GUEST') not null,
    status       enum ('ACTIVE', 'BLOCKED')      not null,
    constraint users_pk
        unique (Email),
    constraint users_pk2
        unique (Username),
    constraint users_pk3
        unique (phoneNumber),
    constraint users_cars_id_fk
        foreign key (car_id) references cars (id),
    constraint users_photos_id_fk
        foreign key (photo_url_id) references photos (id)
);

create table travels
(
    id             int auto_increment
        primary key,
    startPoint     varchar(100)                                              not null,
    endPoint       varchar(100)                                              not null,
    free_spots     int                                                       not null,
    driver_note    varchar(200)                                              null,
    driver_id      int                                                       not null,
    distance       varchar(32)                                               not null,
    departure_time datetime                                                  not null,
    status         enum ('OPEN', 'CANCELLED', 'FULL', 'ONGOING', 'FINISHED') not null,
    constraint travel_users_id_fk
        foreign key (driver_id) references users (id)
);

create table feedbacks
(
    id          int auto_increment
        primary key,
    travel_id   int    not null,
    giver_id    int    not null,
    receiver_id int    not null,
    rating      double not null,
    constraint feedbacks_travels_id_fk
        foreign key (travel_id) references travels (id),
    constraint feedbacks_users_id_fk
        foreign key (giver_id) references users (id),
    constraint feedbacks_users_id_fk2
        foreign key (receiver_id) references users (id)
);

create table feedback_comments
(
    id          int auto_increment
        primary key,
    feedback_id int          not null,
    content     varchar(255) not null,
    constraint feedback_comments_feedbacks_id_fk
        foreign key (feedback_id) references feedbacks (id)
);

create index travel_destinations_id_fk
    on travels (endPoint);

create index travel_driver_comments_id_fk
    on travels (driver_note);

create table travels_passengers
(
    id           int auto_increment
        primary key,
    travel_id    int not null,
    passenger_id int not null,
    constraint travels_passengers_travels_id_fk
        foreign key (travel_id) references travels (id),
    constraint travels_passengers_users_id_fk
        foreign key (passenger_id) references users (id)
);

create table user_applications
(
    id        int auto_increment
        primary key,
    user_id   int                                      not null,
    travel_id int                                      not null,
    status    enum ('PENDING', 'DECLINED', 'APPROVED') null,
    constraint user_applications_travels_id_fk
        foreign key (travel_id) references travels (id),
    constraint user_applications_users_id_fk
        foreign key (user_id) references users (id)
);

create index user_applications_application_statuses_id_fk
    on user_applications (status);

