<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog 
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog" 
    xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog-ext 
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd 
        http://www.liquibase.org/xml/ns/dbchangelog 
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">
        
    <changeSet author="heya" id="renameTable sitelog_localepisodicevent to sitelog_localepisodiceffect -1" objectQuotingStrategy="QUOTE_ALL_OBJECTS">
      <renameTable 
            newTableName="sitelog_localepisodiceffect"
            oldTableName="sitelog_localepisodicevent"
            schemaName="geodesy"/>
    </changeSet>

    <changeSet author="heya" id="renameTable sitelog_localepisodicevent to sitelog_localepisodiceffect -2" objectQuotingStrategy="QUOTE_ALL_OBJECTS">
       <dropPrimaryKey 
            constraintName="sitelog_localepisodicevent_pkey"
            schemaName="geodesy"
            tableName="sitelog_localepisodiceffect"/>
    </changeSet>

    <changeSet author="heya" id="renameTable sitelog_localepisodicevent to sitelog_localepisodiceffect -3" objectQuotingStrategy="QUOTE_ALL_OBJECTS">
        <addPrimaryKey columnNames="id" 
                       constraintName="sitelog_localepisodiceffect_pkey" 
                       tableName="sitelog_localepisodiceffect"/>
    </changeSet>
</databaseChangeLog>
