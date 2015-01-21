#!Ups
drop sequence if exists programmer_id_seq;
create sequence programmer_id_seq start with 1;
insert into programmer (name, company_id, created_timestamp, updated_timestamp) values ('Alice', 1, current_timestamp, current_timestamp);
insert into programmer (name, company_id, created_timestamp, updated_timestamp) values ('Bob', 2, current_timestamp, current_timestamp);
insert into programmer (name, company_id, created_timestamp, updated_timestamp) values ('Chris', 1, current_timestamp, current_timestamp);
insert into programmer (name, company_id, created_timestamp, updated_timestamp) values ('David', null, current_timestamp, current_timestamp);

#!Downs
delete from programmer;
drop sequence programmer_id_seq;