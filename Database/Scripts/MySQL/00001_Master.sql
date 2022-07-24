UPDATE `application_configuration` 
SET `preference`='{"settingstabs":[{"title":"General Settings","xtype":"ekasmartplt-view-collection-dataset-generalsettingsgrid","itemId":"appGeneralSettingsTab","perm_code":"SETT_GENERAL"},{"title":"Mapping Configuration","xtype":"ekasmartplt-view-collection-dataset-listoftemplatedatasetmapping","itemId":"appCollectionMappingTab","perm_code":"SETT_MAPPING_TAB"}]}' 
WHERE `app_id`=22 
AND `type`='SETTINGS';

INSERT INTO `app_permission_mapping_master` (`PERM_CODE`, `APP_ID`) VALUES ('SETT_GENERAL', '22');
INSERT INTO `app_permission_mapping_master` (`PERM_CODE`, `APP_ID`) VALUES ('SETT_MAPPING_TAB', '22');

INSERT INTO `global_permission_mapping`(`PERM_CODE`, `USER_ID`, `USER_GROUP_ID`, `APP_ID`, `ROLE_ID`)
SELECT 'SETT_GENERAL', '-1', '-1', '22', `ID` FROM `roles` 
WHERE `CLIENT_ID` IN (SELECT `ID` FROM `clients` WHERE `CLIENT_TYPE` IN (2,3)) 
AND `NAME` = 'Admin Role'
AND `IS_SYSTEM_CREATED` = 'Y';

INSERT INTO `global_permission_mapping`(`PERM_CODE`, `USER_ID`, `USER_GROUP_ID`, `APP_ID`, `ROLE_ID`)
SELECT 'SETT_MAPPING_TAB', '-1', '-1', '22', `ID` FROM `roles` 
WHERE `CLIENT_ID` IN (SELECT `ID` FROM `clients` WHERE `CLIENT_TYPE` IN (2,3)) 
AND `NAME` = 'Admin Role'
AND `IS_SYSTEM_CREATED` = 'Y';


INSERT INTO `app_client_config` (`app_id`, `preference`, `type`, `client_id`) VALUES 
(22, '{"generalSettings":[[{"allowBlank":true,"margin":"2 2 2 2","xtype":"textfield","fieldLabel":"Offers: Roles To Publish","width":300,"name":"ROLES_TO_PUBLISH","disabled":false,"value":""}],[{"margin":"2 2 2 2","xtype":"checkbox","hidden":false,"fieldLabel":"Is Bid Quantity Locked","name":"IS_QUANTITY_LOCKED","disabled":false,"inputValue":"1","uncheckedValue":"0"}]]}', 'GENERALSETTINGS', '1');

INSERT INTO `app_client_config` (`app_id`, `preference`, `type`, `client_id`)
SELECT 22, '{"generalSettings":[[{"allowBlank":true,"margin":"2 2 2 2","xtype":"textfield","fieldLabel":"Offers: Roles To Publish","width":300,"name":"ROLES_TO_PUBLISH","disabled":false,"value":""}],[{"margin":"2 2 2 2","xtype":"checkbox","hidden":false,"fieldLabel":"Is Bid Quantity Locked","name":"IS_QUANTITY_LOCKED","disabled":false,"inputValue":"1","uncheckedValue":"0"}]]}', 'GENERALSETTINGS', client_id FROM client_app_licences_mapping WHERE std_app LIKE '%"id":"22"%';


--//UNDO


DELETE FROM app_client_config WHERE app_id = 22 AND type = 'GENERALSETTINGS' AND client_id IN(SELECT client_id FROM client_app_licences_mapping WHERE std_app LIKE '%"id":"22"%');
DELETE FROM app_client_config WHERE app_id = 22 AND client_id = 1 AND type = 'GENERALSETTINGS';

DELETE FROM `app_permission_mapping_master` WHERE `PERM_CODE` in ('SETT_GENERAL', 'SETT_MAPPING_TAB') AND `APP_ID`='22';
DELETE FROM `global_permission_mapping` WHERE `PERM_CODE` in ('SETT_GENERAL', 'SETT_MAPPING_TAB') AND `APP_ID`='22';

UPDATE `application_configuration` SET `preference`='{"settingstabs":[{"title":"Mapping Configuration","xtype":"ekasmartplt-view-collection-dataset-listoftemplatedatasetmapping","itemId":"appCollectionMappingTab","perm_code":"SETT_MAPPING_TAB"}]}' 
WHERE `app_id`=22 
AND `type`='SETTINGS';