create table users
(
    UserID       bigint unsigned auto_increment
        primary key,
    Username     varchar(20)                            not null,
    Password     varchar(100)                           not null,
    FirstName    varchar(20)                            not null,
    LastName     varchar(20)                            not null,
    Email        varchar(50)                            not null,
    PhoneNumber  char(10)                               not null,
    ProfilePhoto text                                   null,
    IsBlocked    tinyint(1) default 0                   null,
    CreatedAt    timestamp  default current_timestamp() null,
    constraint Email
        unique (Email),
    constraint PhoneNumber
        unique (PhoneNumber),
    constraint Username
        unique (Username)
);

create table travels
(
    TravelID      int auto_increment
        primary key,
    OrganizerID   bigint unsigned                        not null,
    StartPoint    varchar(100)                           not null,
    EndPoint      varchar(100)                           not null,
    DepartureTime timestamp                              not null,
    FreeSpots     int                                    not null,
    Comments      varchar(255)                           null,
    IsCompleted   tinyint(1) default 0                   null,
    CreatedAt     timestamp  default current_timestamp() null,
    constraint travels_ibfk_1
        foreign key (OrganizerID) references users (UserID)
            on update cascade on delete cascade
);

create table applications
(
    ApplicationID int auto_increment
        primary key,
    TravelID      int                                      not null,
    UserID        bigint unsigned                          not null,
    Status        enum ('Pending', 'Approved', 'Rejected') not null,
    AppliedAt     timestamp default current_timestamp()    null,
    constraint applications_ibfk_1
        foreign key (TravelID) references travels (TravelID)
            on update cascade on delete cascade,
    constraint applications_ibfk_2
        foreign key (UserID) references users (UserID)
            on update cascade on delete cascade
);

create index TravelID
    on applications (TravelID);

create index UserID
    on applications (UserID);

create table feedback
(
    FeedbackID int auto_increment
        primary key,
    TravelID   int                                   not null,
    FromUserID bigint unsigned                       not null,
    ToUserID   bigint unsigned                       not null,
    Rating     enum ('1', '2', '3', '4', '5')        not null,
    Comment    varchar(255)                          null,
    CreatedAt  timestamp default current_timestamp() null,
    constraint feedback_ibfk_1
        foreign key (TravelID) references travels (TravelID)
            on update cascade on delete cascade,
    constraint feedback_ibfk_2
        foreign key (FromUserID) references users (UserID)
            on update cascade on delete cascade,
    constraint feedback_ibfk_3
        foreign key (ToUserID) references users (UserID)
            on update cascade on delete cascade
);

create index FromUserID
    on feedback (FromUserID);

create index ToUserID
    on feedback (ToUserID);

create index TravelID
    on feedback (TravelID);

create index OrganizerID
    on travels (OrganizerID);


