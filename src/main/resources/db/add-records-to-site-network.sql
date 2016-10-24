--liquibase formatted sql
--changeset HongJin:1477274521

DO LANGUAGE plpgsql $$

declare
rsc record;
v_id cors_site_network.id%type;


BEGIN 
  RAISE NOTICE 'Start to update site_network and relation...';

  for rsc IN select distinct network_1 as "name" from temp_site_network where network_1 is not null
union
select distinct network_2 as "name" from temp_site_network where network_2 is not null
union
select distinct network_3 as "name" from temp_site_network where network_3 is not null
union
select distinct network_4 as "name" from temp_site_network where network_4 is not null
order by 1 LOOP

v_id = := nextVal('seq_surrogate_keys');

insert into cors_site_network (id, name) values (v_id, rsc.name);
  
END LOOP;
--commit;
RAISE NOTICE 'Done update site_network and relation.';

END;

$$ ;
