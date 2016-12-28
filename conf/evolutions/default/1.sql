# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table configuration_entry (
  id                            integer auto_increment not null,
  section_name                  varchar(255) not null,
  name                          varchar(255) not null,
  symbol                        varchar(255) not null,
  type_id                       varchar(255) not null,
  type_configuration            TEXT,
  value                         TEXT,
  constraint pk_configuration_entry primary key (id)
);

create table user (
  id                            integer auto_increment not null,
  login                         varchar(255) not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  role_id                       integer,
  constraint pk_user primary key (id)
);

create table user_role (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  symbol                        varchar(255) not null,
  constraint pk_user_role primary key (id)
);

create table user_session (
  id                            integer auto_increment not null,
  session_id                    varchar(255) not null,
  ip                            varchar(255) not null,
  added                         datetime(6) not null,
  user_id                       integer,
  constraint pk_user_session primary key (id)
);

alter table user add constraint fk_user_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);

alter table user_session add constraint fk_user_session_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_session_user_id on user_session (user_id);


# --- !Downs

alter table user drop foreign key fk_user_role_id;
drop index ix_user_role_id on user;

alter table user_session drop foreign key fk_user_session_user_id;
drop index ix_user_session_user_id on user_session;

drop table if exists configuration_entry;

drop table if exists user;

drop table if exists user_role;

drop table if exists user_session;

