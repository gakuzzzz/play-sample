#!Ups
drop sequence if exists company_id_seq;
create sequence company_id_seq start with 1;
insert into company (name, url, created_at) values ('Typesafe', 'http://typesafe.com/', current_timestamp);
insert into company (name, url, created_at) values ('Oracle', 'http://www.oracle.com/', current_timestamp);
insert into company (name, url, created_at) values ('Google', 'http://www.google.com/', current_timestamp);
insert into company (name, url, created_at) values ('Microsoft', 'http://www.microsoft.com/', current_timestamp);

#!Downs
delete from company;
drop sequence company_id_seq;
