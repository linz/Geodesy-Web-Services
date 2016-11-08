--liquibase formatted sql
--changeset HongJin:1478571328


alter table sitelog_responsible_party rename column responsible_role to responsible_role_id;

ALTER TABLE sitelog_responsible_party RENAME CONSTRAINT "fk_sitelog_responsible_party_responsiblerole" TO "fk_sitelog_responsible_party_responsible_roleid";
