--liquibase formatted sql
--changeset HongJin:1474424364


insert into equipment_configuration(equipment_configuration_id,equipment_id,configuration_time)
select a.id, a.equipment_id, a.configuration_time
from clock_configuration a, equipment b
where a.equipment_id = b.id 
union
select a.id ,a.equipment_id, a.configuration_time
from gnss_antenna_configuration a, equipment b
where a.equipment_id = b.id   
union
select a.id, a.equipment_id, a.configuration_time
from gnss_receiver_configuration a, equipment b
where a.equipment_id = b.id  
union
select a.id, a.equipment_id, a.configuration_time
from humidity_sensor_configuration a, equipment b
where a.equipment_id = b.id;

commit;

