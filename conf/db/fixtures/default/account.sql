#!Ups
drop sequence if exists account_id_seq;
create sequence account_id_seq start with 1;
insert into account (name, mail, hashed_password, created_at, updated_at, version) values ('Alice','alice@example.com', '$2a$10$mIuqPO8aZwOnb0B4WOXhk.le0y.fwidayJN/dKys9CorVzClPZJZO', current_timestamp, current_timestamp, 1);
insert into account (name, mail, hashed_password, created_at, updated_at, version) values ('Bob','bob@example.com', '$2a$10$wENXqciAm6IbL9/ssGfZcunZSBTvjiAdZvTfSDp1ttE4EoVzs2X36', current_timestamp, current_timestamp, 1);

#!Downs
delete from account;
drop sequence account_id_seq;
