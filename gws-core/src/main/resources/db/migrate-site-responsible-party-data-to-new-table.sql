--liquibase formatted sql
--changeset HongJin:1478058091

insert into sitelog_responsible_party_role (id, responsible_role_name, responsible_role_XMLtag)
values (nextVal('seq_surrogate_keys'),'Site Owner', 'SiteOwner');

insert into sitelog_responsible_party_role (id, responsible_role_name, responsible_role_XMLtag)
values (nextVal('seq_surrogate_keys'),'Site Contact', 'SiteContact');

insert into sitelog_responsible_party_role (id, responsible_role_name, responsible_role_XMLtag)
values (nextVal('seq_surrogate_keys'),'Site Metadata Custodian', 'SiteMetadataCustodian');

insert into sitelog_responsible_party_role (id, responsible_role_name, responsible_role_XMLtag)
values (nextVal('seq_surrogate_keys'),'Site Data Center', 'SiteDataCenter');

insert into sitelog_responsible_party_role (id, responsible_role_name, responsible_role_XMLtag)
values (nextVal('seq_surrogate_keys'),'Site Data Source', 'SiteDataSource');

insert into sitelog_responsible_party select nextVal('seq_surrogate_keys'),b.id, a.iso_19115,(select id from sitelog_responsible_party_role where responsible_role_xmltag = 'SiteContact')
from responsible_party a, sitelog_site b
where a.id = b.site_contact_id; 

insert into sitelog_responsible_party select nextVal('seq_surrogate_keys'),b.id, a.iso_19115,(select id from sitelog_responsible_party_role where responsible_role_xmltag = 'SiteMetadataCustodian')
from responsible_party a, sitelog_site b
where a.id = b.site_metadata_custodian_id;

commit;
