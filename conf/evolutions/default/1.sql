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

create table invoice (
  id                            integer auto_increment not null,
  public_id                     varchar(255) not null,
  public_id_number              integer not null,
  currency                      varchar(255) not null,
  total                         integer not null,
  seller_name                   varchar(255) not null,
  seller_address                varchar(255) not null,
  seller_zip                    varchar(255) not null,
  seller_city                   varchar(255) not null,
  seller_country                varchar(255) not null,
  seller_tax_id                 varchar(255) not null,
  buyer_is_company              tinyint(1) default 0 not null,
  buyer_name                    varchar(255) not null,
  buyer_address                 varchar(255) not null,
  buyer_zip                     varchar(255) not null,
  buyer_city                    varchar(255) not null,
  buyer_country                 varchar(255) not null,
  buyer_tax_id                  varchar(255),
  buyer_email                   varchar(255),
  buyer_phone                   varchar(255),
  payment_method_id             integer not null,
  place_of_issue                varchar(255) not null,
  issue_date                    date not null,
  order_date                    date not null,
  due_date                      date not null,
  additional_details            TEXT,
  creator_id                    integer not null,
  constraint uq_invoice_public_id unique (public_id),
  constraint pk_invoice primary key (id)
);

create table invoice_part (
  id                            integer auto_increment not null,
  name                          TEXT not null,
  unit                          varchar(255) not null,
  quantity                      float not null,
  unit_price                    integer not null,
  total                         integer not null,
  invoice_id                    integer,
  constraint pk_invoice_part primary key (id)
);

create table payment_method (
  id                            integer auto_increment not null,
  name                          varchar(255) not null,
  symbol                        varchar(255) not null,
  constraint pk_payment_method primary key (id)
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

alter table invoice add constraint fk_invoice_payment_method_id foreign key (payment_method_id) references payment_method (id) on delete restrict on update restrict;
create index ix_invoice_payment_method_id on invoice (payment_method_id);

alter table invoice add constraint fk_invoice_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_invoice_creator_id on invoice (creator_id);

alter table invoice_part add constraint fk_invoice_part_invoice_id foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_invoice_part_invoice_id on invoice_part (invoice_id);

alter table user add constraint fk_user_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);

alter table user_session add constraint fk_user_session_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_session_user_id on user_session (user_id);


# --- !Downs

alter table invoice drop foreign key fk_invoice_payment_method_id;
drop index ix_invoice_payment_method_id on invoice;

alter table invoice drop foreign key fk_invoice_creator_id;
drop index ix_invoice_creator_id on invoice;

alter table invoice_part drop foreign key fk_invoice_part_invoice_id;
drop index ix_invoice_part_invoice_id on invoice_part;

alter table user drop foreign key fk_user_role_id;
drop index ix_user_role_id on user;

alter table user_session drop foreign key fk_user_session_user_id;
drop index ix_user_session_user_id on user_session;

drop table if exists configuration_entry;

drop table if exists invoice;

drop table if exists invoice_part;

drop table if exists payment_method;

drop table if exists user;

drop table if exists user_role;

drop table if exists user_session;

