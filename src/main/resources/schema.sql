DROP TABLE IF EXISTS testmysqldb.employee;
DROP TABLE IF EXISTS testmysqldb.authorities;
DROP TABLE IF EXISTS testmysqldb.users;

CREATE TABLE testmysqldb.employee (
  empId VARCHAR(10) NOT NULL,
  empName VARCHAR(100) NOT NULL
);

create table testmysqldb.users (
    username varchar(50) not null primary key,
    password varchar(120) not null,
    enabled boolean not null
);

create table testmysqldb.authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    foreign key (username) references users (username)
);


insert into testmysqldb.users(username, password, enabled)values('javainuse','javainuse',true);
insert into testmysqldb.authorities(username,authority)values('javainuse','ROLE_ADMIN');
 
insert into testmysqldb.users(username, password, enabled)values('employee','employee',true);
insert into testmysqldb.authorities(username,authority)values('javainuse','ROLE_USER');