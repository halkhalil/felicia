# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  id                            integer auto_increment not null,
  login                         varchar(255) not null,
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

alter table user add constraint fk_user_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);


# --- !Downs

alter table user drop foreign key fk_user_role_id;
drop index ix_user_role_id on user;

drop table if exists user;

drop table if exists user_role;

