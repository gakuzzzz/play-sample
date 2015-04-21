#!Ups
drop sequence if exists company_id_seq;
create sequence company_id_seq start with 1;
insert into company (name, url, created_at, updated_at, version) values ('Typesafe', 'http://typesafe.com/', current_timestamp, current_timestamp, 1);
insert into company (name, url, created_at, updated_at, version) values ('Oracle', 'http://www.oracle.com/', current_timestamp, current_timestamp, 1);
insert into company (name, url, created_at, updated_at, version) values ('Google', 'http://www.google.com/', current_timestamp, current_timestamp, 1);
insert into company (name, url, created_at, updated_at, version) values ('Microsoft', 'http://www.microsoft.com/', current_timestamp, current_timestamp, 1);

#!Downs
delete from company;
drop sequence company_id_seq;
