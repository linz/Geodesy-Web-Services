--liquibase formatted sql
--changeset HongJin:1476923463


drop table cors_site_network_relation;
drop table cors_site_network;

create table cors_site_network (id integer not null,
name text,
description text);

create table cors_site_in_network (id integer not null,
cors_site_id integer not null,
cors_site_network_id integer not null,
effective_from timestamp,
effective_to timestamp);

alter table cors_site_network ADD CONSTRAINT pk_cors_site_network_id PRIMARY KEY(id);
alter table cors_site_in_network ADD CONSTRAINT pk_cors_site_in_network_id PRIMARY KEY(id);

ALTER TABLE cors_site_in_network ADD CONSTRAINT fk_cors_site_in_network_siteid FOREIGN KEY (cors_site_id) REFERENCES cors_site (id);

ALTER TABLE cors_site_in_network ADD CONSTRAINT fk_cors_site_in_network_networkid FOREIGN KEY (cors_site_network_id) REFERENCES cors_site_network (id);


comment on table cors_site_in_network is 'table between cors_site and cors_site_network to break a many-to-many relationship and also store time_in and out information.';

comment on column cors_site_in_network.cors_site_id is 'a foreign key linked to cors_site primary key (id)';

comment on column cors_site_in_network.cors_site_network_id is 'a foreign key linked to cors_site_network primary key (id)';
