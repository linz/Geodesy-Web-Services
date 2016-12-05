--liquibase formatted sql
--changeset HongJin:1477364071

select 1 from add_records_to_site_network();
select 1 from add_records_to_site_in_network();
