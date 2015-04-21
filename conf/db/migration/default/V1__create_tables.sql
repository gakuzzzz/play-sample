drop table if exists programmer;
create sequence programmer_id_seq start with 1;
create table programmer (
  id bigint not null default nextval('programmer_id_seq') primary key,
  name varchar(255) not null,
  company_id bigint,
  created_timestamp timestamp not null,
  updated_timestamp timestamp not null,
  deleted_timestamp timestamp,
  version integer not null
);

drop table if exists company;
create table company (
  id bigint not null default nextval('company_id_seq') primary key,
  name varchar(255) not null,
  url varchar(255),
  created_at timestamp not null,
  updated_at timestamp not null,
  deleted_at timestamp,
  version integer not null
);

drop table if exists skill;
create sequence skill_id_seq start with 1;
create table skill (
  id bigint not null default nextval('skill_id_seq') primary key,
  name varchar(255) not null,
  created_at timestamp not null,
  deleted_at timestamp
);

drop table if exists programmer_skill;
create table programmer_skill (
  programmer_id bigint not null,
  skill_id bigint not null,
  primary key(programmer_id, skill_id)
);

drop table if exists account;
create table account (
  id bigint not null default nextval('account_id_seq') primary key,
  name varchar(255) not null,
  mail varchar(255) not null,
  hashed_password varchar(255) not null,
  created_at timestamp not null,
  updated_at timestamp not null,
  deleted_at timestamp,
  version integer not null
);



