#!Ups
drop sequence if exists skill_id_seq;
create sequence skill_id_seq start with 1;
insert into skill (name, created_at) values ('Scala', current_timestamp);
insert into skill (name, created_at) values ('Java', current_timestamp);
insert into skill (name, created_at) values ('Ruby', current_timestamp);
insert into skill (name, created_at) values ('MySQL', current_timestamp);
insert into skill (name, created_at) values ('PostgreSQL', current_timestamp);

#!Downs
delete from skill;
drop sequence skill_id_seq;
