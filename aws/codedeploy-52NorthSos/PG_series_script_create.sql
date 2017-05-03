--
-- Copyright (C) 2012-2016 52°North Initiative for Geospatial Open Source
-- Software GmbH
--
-- This program is free software; you can redistribute it and/or modify it
-- under the terms of the GNU General Public License version 2 as published
-- by the Free Software Foundation.
--
-- If the program is linked with libraries which are licensed under one of
-- the following licenses, the combination of the program with the linked
-- library is not considered a "derivative work" of the program:
--
--     - Apache License, version 2.0
--     - Apache Software License, version 1.0
--     - GNU Lesser General Public License, version 3
--     - Mozilla Public License, versions 1.0, 1.1 and 2.0
--     - Common Development and Distribution License (CDDL), version 1.0
--
-- Therefore the distribution of the program linked with libraries licensed
-- under the aforementioned licenses, is permitted by the copyright holders
-- if the distribution is compliant with both the GNU General Public
-- License version 2 and the aforementioned licenses.
--
-- This program is distributed in the hope that it will be useful, but
-- WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
-- Public License for more details.
--

create table :schema."procedure" (procedureId int8 not null, hibernateDiscriminator char(1) not null, procedureDescriptionFormatId int8 not null, identifier varchar(255) not null, codespace int8, name varchar(255), codespacename int8, description varchar(255), deleted char(1) default 'F' not null check (deleted in ('T','F')), descriptionFile text, referenceFlag char(1) default 'F' check (referenceFlag in ('T','F')), primary key (procedureId));
create table :schema.blobValue (observationId int8 not null, value oid, primary key (observationId));
create table :schema.booleanValue (observationId int8 not null, value char(1), primary key (observationId), check (value in ('T','F')), check (value in ('T','F')));
create table :schema.categoryValue (observationId int8 not null, value varchar(255), primary key (observationId));
create table :schema.codespace (codespaceId int8 not null, codespace varchar(255) not null, primary key (codespaceId));
create table :schema.compositePhenomenon (parentObservablePropertyId int8 not null, childObservablePropertyId int8 not null, primary key (childObservablePropertyId, parentObservablePropertyId));
create table :schema.countValue (observationId int8 not null, value int4, primary key (observationId));
create table :schema.featureOfInterest (featureOfInterestId int8 not null, hibernateDiscriminator char(1) not null, featureOfInterestTypeId int8 not null, identifier varchar(255), codespace int8, name varchar(255), codespacename int8, description varchar(255), geom GEOMETRY, descriptionXml text, url varchar(255), primary key (featureOfInterestId));
create table :schema.featureOfInterestType (featureOfInterestTypeId int8 not null, featureOfInterestType varchar(255) not null, primary key (featureOfInterestTypeId));
create table :schema.featureRelation (parentFeatureId int8 not null, childFeatureId int8 not null, primary key (childFeatureId, parentFeatureId));
create table :schema.geometryValue (observationId int8 not null, value GEOMETRY, primary key (observationId));
create table :schema.i18nfeatureOfInterest (id int8 not null, objectId int8 not null, locale varchar(255) not null, name varchar(255), description varchar(255), primary key (id));
create table :schema.i18nobservableProperty (id int8 not null, objectId int8 not null, locale varchar(255) not null, name varchar(255), description varchar(255), primary key (id));
create table :schema.i18noffering (id int8 not null, objectId int8 not null, locale varchar(255) not null, name varchar(255), description varchar(255), primary key (id));
create table :schema.i18nprocedure (id int8 not null, objectId int8 not null, locale varchar(255) not null, name varchar(255), description varchar(255), shortname varchar(255), longname varchar(255), primary key (id));
create table :schema.numericValue (observationId int8 not null, value double precision, primary key (observationId));
create table :schema.observableProperty (observablePropertyId int8 not null, hibernateDiscriminator char(1) not null, identifier varchar(255) not null, codespace int8, name varchar(255), codespacename int8, description varchar(255), primary key (observablePropertyId));
create table :schema.observation (observationId int8 not null, seriesId int8 not null, phenomenonTimeStart timestamp not null, phenomenonTimeEnd timestamp not null, resultTime timestamp not null, identifier varchar(255), codespace int8, name varchar(255), codespacename int8, description varchar(255), deleted char(1) default 'F' not null check (deleted in ('T','F')), validTimeStart timestamp, validTimeEnd timestamp, unitId int8, samplingGeometry GEOMETRY, primary key (observationId));
create table :schema.observationConstellation (observationConstellationId int8 not null, observablePropertyId int8 not null, procedureId int8 not null, observationTypeId int8, offeringId int8 not null, deleted char(1) default 'F' not null check (deleted in ('T','F')), hiddenChild char(1) default 'F' not null check (hiddenChild in ('T','F')), primary key (observationConstellationId));
create table :schema.observationHasOffering (observationId int8 not null, offeringId int8 not null, primary key (observationId, offeringId));
create table :schema.observationType (observationTypeId int8 not null, observationType varchar(255) not null, primary key (observationTypeId));
create table :schema.offering (offeringId int8 not null, hibernateDiscriminator char(1) not null, identifier varchar(255) not null, codespace int8, name varchar(255), codespacename int8, description varchar(255), primary key (offeringId));
create table :schema.offeringAllowedFeatureType (offeringId int8 not null, featureOfInterestTypeId int8 not null, primary key (offeringId, featureOfInterestTypeId));
create table :schema.offeringAllowedObservationType (offeringId int8 not null, observationTypeId int8 not null, primary key (offeringId, observationTypeId));
create table :schema.offeringHasRelatedFeature (relatedFeatureId int8 not null, offeringId int8 not null, primary key (offeringId, relatedFeatureId));
create table :schema.parameter (parameterId int8 not null, observationId int8 not null, definition varchar(255) not null, title varchar(255), value oid not null, primary key (parameterId));
create table :schema.procedureDescriptionFormat (procedureDescriptionFormatId int8 not null, procedureDescriptionFormat varchar(255) not null, primary key (procedureDescriptionFormatId));
create table :schema.relatedFeature (relatedFeatureId int8 not null, featureOfInterestId int8 not null, primary key (relatedFeatureId));
create table :schema.relatedFeatureHasRole (relatedFeatureId int8 not null, relatedFeatureRoleId int8 not null, primary key (relatedFeatureId, relatedFeatureRoleId));
create table :schema.relatedFeatureRole (relatedFeatureRoleId int8 not null, relatedFeatureRole varchar(255) not null, primary key (relatedFeatureRoleId));
create table :schema.resultTemplate (resultTemplateId int8 not null, offeringId int8 not null, observablePropertyId int8 not null, procedureId int8 not null, featureOfInterestId int8 not null, identifier varchar(255) not null, resultStructure text not null, resultEncoding text not null, primary key (resultTemplateId));
create table :schema.sensorSystem (parentSensorId int8 not null, childSensorId int8 not null, primary key (childSensorId, parentSensorId));
create table :schema.series (seriesId int8 not null, featureOfInterestId int8 not null, observablePropertyId int8 not null, procedureId int8 not null, deleted char(1) default 'F' not null check (deleted in ('T','F')), published char(1) default 'T' not null check (published in ('T','F')), firstTimeStamp timestamp, lastTimeStamp timestamp, firstNumericValue double precision, lastNumericValue double precision, unitId int8, primary key (seriesId));
create table :schema.sweDataArrayValue (observationId int8 not null, value text, primary key (observationId));
create table :schema.textValue (observationId int8 not null, value text, primary key (observationId));
create table :schema.unit (unitId int8 not null, unit varchar(255) not null, primary key (unitId));
create table :schema.validProcedureTime (validProcedureTimeId int8 not null, procedureId int8 not null, procedureDescriptionFormatId int8 not null, startTime timestamp not null, endTime timestamp, descriptionXml text not null, primary key (validProcedureTimeId));
alter table :schema."procedure" add constraint procIdentifierUK unique (identifier);
alter table :schema.codespace add constraint codespaceUK unique (codespace);
alter table :schema.featureOfInterest add constraint foiIdentifierUK unique (identifier);
alter table :schema.featureOfInterest add constraint obsUrl unique (url);
alter table :schema.featureOfInterestType add constraint featureTypeUK unique (featureOfInterestType);
alter table :schema.i18nfeatureOfInterest add constraint i18nFeatureIdentity unique (objectId, locale);
create index i18nFeatureIdx on :schema.i18nfeatureOfInterest (objectId);
alter table :schema.i18nobservableProperty add constraint i18nobsPropIdentity unique (objectId, locale);
create index i18nObsPropIdx on :schema.i18nobservableProperty (objectId);
alter table :schema.i18noffering add constraint i18nOfferingIdentity unique (objectId, locale);
create index i18nOfferingIdx on :schema.i18noffering (objectId);
alter table :schema.i18nprocedure add constraint i18nProcedureIdentity unique (objectId, locale);
create index i18nProcedureIdx on :schema.i18nprocedure (objectId);
alter table :schema.observableProperty add constraint obsPropIdentifierUK unique (identifier);
alter table :schema.observation add constraint observationIdentity unique (seriesId, phenomenonTimeStart, phenomenonTimeEnd, resultTime);
alter table :schema.observation add constraint obsIdentifierUK unique (identifier);
create index obsSeriesIdx on :schema.observation (seriesId);
create index obsPhenTimeStartIdx on :schema.observation (phenomenonTimeStart);
create index obsPhenTimeEndIdx on :schema.observation (phenomenonTimeEnd);
create index obsResultTimeIdx on :schema.observation (resultTime);
create index obsCodespaceIdx on :schema.observation (codespaceId);
alter table :schema.observationConstellation add constraint obsnConstellationIdentity unique (observablePropertyId, procedureId, offeringId);
create index obsConstObsPropIdx on :schema.observationConstellation (observablePropertyId);
create index obsConstProcedureIdx on :schema.observationConstellation (procedureId);
create index obsConstOfferingIdx on :schema.observationConstellation (offeringId);
create index obshasoffobservationidx on :schema.observationHasOffering (observationId);
create index obshasoffofferingidx on :schema.observationHasOffering (offeringId);
alter table :schema.observationType add constraint observationTypeUK unique (observationType);
alter table :schema.offering add constraint offIdentifierUK unique (identifier);
alter table :schema.procedureDescriptionFormat add constraint procDescFormatUK unique (procedureDescriptionFormat);
alter table :schema.relatedFeatureRole add constraint relFeatRoleUK unique (relatedFeatureRole);
create index resultTempOfferingIdx on :schema.resultTemplate (offeringId);
create index resultTempeObsPropIdx on :schema.resultTemplate (observablePropertyId);
create index resultTempProcedureIdx on :schema.resultTemplate (procedureId);
create index resultTempIdentifierIdx on :schema.resultTemplate (identifier);
alter table :schema.series add constraint seriesIdentity unique (featureOfInterestId, observablePropertyId, procedureId);
create index seriesFeatureIdx on :schema.series (featureOfInterestId);
create index seriesObsPropIdx on :schema.series (observablePropertyId);
create index seriesProcedureIdx on :schema.series (procedureId);
alter table :schema.unit add constraint unitUK unique (unit);
create index validProcedureTimeStartTimeIdx on :schema.validProcedureTime (startTime);
create index validProcedureTimeEndTimeIdx on :schema.validProcedureTime (endTime);
alter table :schema."procedure" add constraint procProcDescFormatFk foreign key (procedureDescriptionFormatId) references :schema.procedureDescriptionFormat;
alter table :schema."procedure" add constraint procCodespaceIdentifierFk foreign key (codespace) references :schema.codespace;
alter table :schema."procedure" add constraint procCodespaceNameFk foreign key (codespacename) references :schema.codespace;
alter table :schema.blobValue add constraint observationBlobValueFk foreign key (observationId) references :schema.observation;
alter table :schema.booleanValue add constraint observationBooleanValueFk foreign key (observationId) references :schema.observation;
alter table :schema.categoryValue add constraint observationCategoryValueFk foreign key (observationId) references :schema.observation;
alter table :schema.compositePhenomenon add constraint observablePropertyChildFk foreign key (childObservablePropertyId) references :schema.observableProperty;
alter table :schema.compositePhenomenon add constraint observablePropertyParentFk foreign key (parentObservablePropertyId) references :schema.observableProperty;
alter table :schema.countValue add constraint observationCountValueFk foreign key (observationId) references :schema.observation;
alter table :schema.featureOfInterest add constraint featureFeatureTypeFk foreign key (featureOfInterestTypeId) references :schema.featureOfInterestType;
alter table :schema.featureOfInterest add constraint featureCodespaceIdentifierFk foreign key (codespace) references :schema.codespace;
ALTER TABLE :schema.featureofinterest add constraint featureCodespaceNameFk foreign key (codespacename) references :schema.codespace
alter table :schema.featureRelation add constraint featureOfInterestChildFk foreign key (childFeatureId) references :schema.featureOfInterest;
alter table :schema.featureRelation add constraint featureOfInterestParentFk foreign key (parentFeatureId) references :schema.featureOfInterest;
alter table :schema.geometryValue add constraint observationGeometryValueFk foreign key (observationId) references :schema.observation;
alter table :schema.i18nfeatureOfInterest add constraint i18nFeatureFeatureFk foreign key (objectId) references :schema.featureOfInterest;
alter table :schema.i18nobservableProperty add constraint i18nObsPropObsPropFk foreign key (objectId) references :schema.observableProperty;
alter table :schema.i18noffering add constraint i18nOfferingOfferingFk foreign key (objectId) references :schema.offering;
alter table :schema.i18nprocedure add constraint i18nProcedureProcedureFk foreign key (objectId) references :schema."procedure";
alter table :schema.numericValue add constraint observationNumericValueFk foreign key (observationId) references :schema.observation;
alter table :schema.observableproperty add constraint obsPropCodespaceIdentifierFk foreign key (codespace) references :schema.codespace;
alter table :schema.observableproperty add constraint obsPropCodespaceNameFk foreign key (codespacename) references :schema.codespace;
alter table :schema.observation add constraint observationSeriesFk foreign key (seriesId) references :schema.series;
alter table :schema.observation add constraint obsCodespaceIdentifierFk foreign key (codespace) references :schema.codespace;
alter table :schema.observation add constraint obsCodespaceNameFk foreign key (codespacename) references :schema.codespace;
alter table :schema.observation add constraint observationUnitFk foreign key (unitId) references :schema.unit;
alter table :schema.observationConstellation add constraint obsConstObsPropFk foreign key (observablePropertyId) references :schema.observableProperty;
alter table :schema.observationConstellation add constraint obsnConstProcedureFk foreign key (procedureId) references :schema."procedure";
alter table :schema.observationConstellation add constraint obsConstObservationIypeFk foreign key (observationTypeId) references :schema.observationType;
alter table :schema.observationConstellation add constraint obsConstOfferingFk foreign key (offeringId) references :schema.offering;
alter table :schema.observationHasOffering add constraint observationOfferingFk foreign key (offeringId) references :schema.offering;
alter table :schema.observationHasOffering add constraint FK_9ex7hawh3dbplkllmw5w3kvej foreign key (observationId) references :schema.observation;
alter table :schema.offering add constraint offCodespaceIdentifierFk foreign key (codespace) references :schema.codespace;
alter table :schema.offering add constraint offCodespaceNameFk foreign key (codespacename) references :schema.codespace;
alter table :schema.offeringAllowedFeatureType add constraint offeringFeatureTypeFk foreign key (featureOfInterestTypeId) references :schema.featureOfInterestType;
alter table :schema.offeringAllowedFeatureType add constraint FK_6vvrdxvd406n48gkm706ow1pt foreign key (offeringId) references :schema.offering;
alter table :schema.offeringAllowedObservationType add constraint offeringObservationTypeFk foreign key (observationTypeId) references :schema.observationType;
alter table :schema.offeringAllowedObservationType add constraint FK_lkljeohulvu7cr26pduyp5bd0 foreign key (offeringId) references :schema.offering;
alter table :schema.offeringHasRelatedFeature add constraint relatedFeatureOfferingFk foreign key (offeringId) references :schema.offering;
alter table :schema.offeringHasRelatedFeature add constraint offeringRelatedFeatureFk foreign key (relatedFeatureId) references :schema.relatedFeature;
alter table :schema.relatedFeature add constraint relatedFeatureFeatureFk foreign key (featureOfInterestId) references :schema.featureOfInterest;
alter table :schema.relatedFeatureHasRole add constraint relatedFeatRelatedFeatRoleFk foreign key (relatedFeatureRoleId) references :schema.relatedFeatureRole;
alter table :schema.relatedFeatureHasRole add constraint FK_6ynwkk91xe8p1uibmjt98sog3 foreign key (relatedFeatureId) references :schema.relatedFeature;
alter table :schema.resultTemplate add constraint resultTemplateOfferingIdx foreign key (offeringId) references :schema.offering;
alter table :schema.resultTemplate add constraint resultTemplateObsPropFk foreign key (observablePropertyId) references :schema.observableProperty;
alter table :schema.resultTemplate add constraint resultTemplateProcedureFk foreign key (procedureId) references :schema."procedure";
alter table :schema.resultTemplate add constraint resultTemplateFeatureIdx foreign key (featureOfInterestId) references :schema.featureOfInterest;
alter table :schema.sensorSystem add constraint procedureChildFk foreign key (childSensorId) references :schema."procedure";
alter table :schema.sensorSystem add constraint procedureParenfFk foreign key (parentSensorId) references :schema."procedure";
alter table :schema.series add constraint seriesFeatureFk foreign key (featureOfInterestId) references :schema.featureOfInterest;
alter table :schema.series add constraint seriesObPropFk foreign key (observablePropertyId) references :schema.observableProperty;
alter table :schema.series add constraint seriesProcedureFk foreign key (procedureId) references :schema."procedure";
alter table :schema.series add constraint seriesUnitFk foreign key (unitId) references :schema.unit;
alter table :schema.sweDataArrayValue add constraint observationSweDataArrayValueFk foreign key (observationId) references :schema.observation;
alter table :schema.textValue add constraint observationTextValueFk foreign key (observationId) references :schema.observation;
alter table :schema.validProcedureTime add constraint validProcedureTimeProcedureFk foreign key (procedureId) references :schema."procedure";
alter table :schema.validProcedureTime add constraint validProcProcDescFormatFk foreign key (procedureDescriptionFormatId) references :schema.procedureDescriptionFormat;
create sequence :schema.codespaceId_seq;
create sequence :schema.featureOfInterestId_seq;
create sequence :schema.featureOfInterestTypeId_seq;
create sequence :schema.i18nObsPropId_seq;
create sequence :schema.i18nOfferingId_seq;
create sequence :schema.i18nProcedureId_seq;
create sequence :schema.i18nfeatureOfInterestId_seq;
create sequence :schema.observablePropertyId_seq;
create sequence :schema.observationConstellationId_seq;
create sequence :schema.observationId_seq;
create sequence :schema.observationTypeId_seq;
create sequence :schema.offeringId_seq;
create sequence :schema.parameterId_seq;
create sequence :schema.procDescFormatId_seq;
create sequence :schema.procedureId_seq;
create sequence :schema.relatedFeatureId_seq;
create sequence :schema.relatedFeatureRoleId_seq;
create sequence :schema.resultTemplateId_seq;
create sequence :schema.seriesId_seq;
create sequence :schema.unitId_seq;
create sequence :schema.validProcedureTimeId_seq;
