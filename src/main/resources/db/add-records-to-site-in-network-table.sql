--liquibase formatted sql
--changeset HongJin:1477274990

DO LANGUAGE plpgsql $$

declare
rsc record;
v_id cors_site_in_network.id%type;
--v_newwork_1 cors_site_network.network%type;
--v_newwork_2 cors_site_network.network%type;
--v_newwork_3 cors_site_network.network%type;
--v_network_id cors_site_network.id%type;

BEGIN 
  RAISE NOTICE 'Start to update site_in_network and relation...';

  for rsc IN select b.id as "site_id", a.network_1, c.id as "network_id"
from temp_site_network a, cors_site b, cors_site_network c
where a.four_character_id = b.four_character_id
and a.network_1 = c.name
union
select b.id as "site_id", a.network_2, c.id as "network_id"
from temp_site_network a, cors_site b, cors_site_network c
where a.four_character_id = b.four_character_id
and a.network_2 = c.name
union
select b.id as "site_id", a.network_3, c.id as "network_id"
from temp_site_network a, cors_site b, cors_site_network c
where a.four_character_id = b.four_character_id
and a.network_3 = c.name
union
select b.id as "site_id", a.network_4, c.id as "network_id"
from temp_site_network a, cors_site b, cors_site_network c
where a.four_character_id = b.four_character_id
and a.network_4 = c.name
order by 1 LOOP


      v_id  := nextVal('seq_surrogate_keys');
      insert into cors_site_in_network (id, cors_site_id, cors_site_network_id) values (v_id, rsc.site_id, rsc.network_id);       

  
END LOOP;
--commit;
RAISE NOTICE 'Done update site_in_network table.';

END;

$$ ;
