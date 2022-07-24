INSERT INTO `templates_configuration` 
(`TEMPLATE_ID`, `TEMPLATE_NAME`, `DESCRIPTION`, `SECURITY_REQUIRED`, `TEMPLATE_MAPPINGS`, `TEMPLATE_COLUMNS`, `IS_USER_ADDED_COLUMN_REQUIRED`, `IS_PLATFORM_TEMPLATE`) VALUES 
('bidToContract', 'Bid To Contract', 'This template containing information required to create contract from a bid.', 'Y', '{}', '[{"accepted_values":[],"templateColumnId":"c1","dataType":"String","isMandatory":"true","appColumn":"corporate","name":"Corporate","desc":"Corporate Name"},{"accepted_values":[],"templateColumnId":"c2","dataType":"String","isMandatory":"true","appColumn":"cpRefNo","name":"CP Ref No.","desc":"CP Ref No."},{"accepted_values":[],"templateColumnId":"c3","dataType":"Date","isMandatory":"true","appColumn":"contractIssueDate","name":"Contract Issue Date","desc":"Contract Issue Date"},{"accepted_values":[],"templateColumnId":"c4","dataType":"String","isMandatory":"true","appColumn":"traderName","name":"Trader Name","desc":"Trader Name"},{"accepted_values":[],"templateColumnId":"c5","dataType":"String","isMandatory":"true","appColumn":"dealType","name":"Deal Type","desc":"Deal Type"},{"accepted_values":[],"templateColumnId":"c6","dataType":"String","isMandatory":"true","appColumn":"cpName","name":"CP Name","desc":"CP Name"},{"accepted_values":[],"templateColumnId":"c7","dataType":"String","isMandatory":"true","appColumn":"legalEntity","name":"Legal Entity","desc":"Legal Entity"},{"accepted_values":[],"templateColumnId":"c8","dataType":"String","isMandatory":"true","appColumn":"incoTerms","name":"INCO Terms","desc":"INCO Terms"},{"accepted_values":[],"templateColumnId":"c9","dataType":"String","isMandatory":"true","appColumn":"paymentTerms","name":"Payment Terms","desc":"Payment Terms"},{"accepted_values":[],"templateColumnId":"c10","dataType":"String","isMandatory":"true","appColumn":"contractQuantityUnit","name":"Contract Quantity Unit","desc":"Contract Quantity Unit"},{"accepted_values":[],"templateColumnId":"c11","dataType":"String","isMandatory":"true","appColumn":"product","name":"Product","desc":"Product"},{"accepted_values":[],"templateColumnId":"c12","dataType":"String","isMandatory":"true","appColumn":"origin","name":"Origin","desc":"Origin"},{"accepted_values":[],"templateColumnId":"c13","dataType":"String","isMandatory":"true","appColumn":"cropYear","name":"Crop Year","desc":"Crop Year"},{"accepted_values":[],"templateColumnId":"c14","dataType":"String","isMandatory":"true","appColumn":"quality","name":"Quality","desc":"Quality"},{"accepted_values":[],"templateColumnId":"c15","dataType":"String","isMandatory":"true","appColumn":"profitCenter","name":"Profit Center","desc":"Profit Center"},{"accepted_values":[],"templateColumnId":"c16","dataType":"String","isMandatory":"true","appColumn":"strategy","name":"Strategy","desc":"Strategy"},{"accepted_values":[],"templateColumnId":"c17","dataType":"Number","isMandatory":"true","appColumn":"itemQuantity","name":"Item Quantity","desc":"Item Quantity"},{"accepted_values":[],"templateColumnId":"c18","dataType":"String","isMandatory":"true","appColumn":"packingType","name":"Packing Type","desc":"Packing Type"},{"accepted_values":[],"templateColumnId":"c19","dataType":"String","isMandatory":"true","appColumn":"packingSize","name":"Packing Size","desc":"Packing Size"},{"accepted_values":[],"templateColumnId":"c20","dataType":"Number","isMandatory":"true","appColumn":"tolerance","name":"Tolerance","desc":"Tolerance"},{"accepted_values":[],"templateColumnId":"c21","dataType":"String","isMandatory":"true","appColumn":"toleranceType","name":"Tolerance Type","desc":"Tolerance Type"},{"accepted_values":[],"templateColumnId":"c22","dataType":"String","isMandatory":"true","appColumn":"toleranceLevel","name":"Tolerance Level","desc":"Tolerance Level"},{"accepted_values":[],"templateColumnId":"c23","dataType":"Number","isMandatory":"true","appColumn":"plannedShipmentQuantity","name":"Planned Shipment Quantity","desc":"Planned Shipment Quantity"},{"accepted_values":[],"templateColumnId":"c24","dataType":"String","isMandatory":"true","appColumn":"payInCurrency","name":"Pay In Currency","desc":"Pay In Currency"},{"accepted_values":[],"templateColumnId":"c25","dataType":"String","isMandatory":"true","appColumn":"priceType","name":"Price Type","desc":"Price Type"},{"accepted_values":[],"templateColumnId":"c26","dataType":"Number","isMandatory":"true","appColumn":"contractPrice","name":"Contract Price","desc":"Contract Price"},{"accepted_values":[],"templateColumnId":"c27","dataType":"String","isMandatory":"true","appColumn":"priceUnit","name":"Price Unit","desc":"Price Unit"},{"accepted_values":[],"templateColumnId":"c28","dataType":"String","isMandatory":"true","appColumn":"loadingType","name":"Loading Type","desc":"Loading Type"},{"accepted_values":[],"templateColumnId":"c29","dataType":"String","isMandatory":"true","appColumn":"loadingCountry","name":"Loading Country","desc":"Loading Country"},{"accepted_values":[],"templateColumnId":"c30","dataType":"String","isMandatory":"true","appColumn":"loadingLocation","name":"Loading Location","desc":"Loading Location"},{"accepted_values":[],"templateColumnId":"c31","dataType":"String","isMandatory":"true","appColumn":"destinationType","name":"Destination Type","desc":"Destination Type"},{"accepted_values":[],"templateColumnId":"c32","dataType":"String","isMandatory":"true","appColumn":"destinationCountry","name":"Destination Country","desc":"Destination Country"},{"accepted_values":[],"templateColumnId":"c33","dataType":"String","isMandatory":"true","appColumn":"destinationLocation","name":"Destination Location","desc":"Destination Location"},{"accepted_values":[],"templateColumnId":"c34","dataType":"String","isMandatory":"true","appColumn":"periodType","name":"Period Type","desc":"Period Type"},{"accepted_values":[],"templateColumnId":"c35","dataType":"Date","isMandatory":"true","appColumn":"deliveryFromDate","name":"Delivery From Date","desc":"Delivery From Date"},{"accepted_values":[],"templateColumnId":"c36","dataType":"Date","isMandatory":"true","appColumn":"deliveryToDate","name":"Delivery To Date","desc":"Delivery To Date"},{"accepted_values":[],"templateColumnId":"c37","dataType":"Date","isMandatory":"true","appColumn":"paymentDueDate","name":"Payment Due Date","desc":"Payment Due Date"},{"accepted_values":[],"templateColumnId":"c38","dataType":"String","isMandatory":"true","appColumn":"invoiceDocumentPriceUnit","name":"Invoice Document Price Unit","desc":"Invoice Document Price Unit"},{"accepted_values":[],"templateColumnId":"c39","dataType":"String","isMandatory":"true","appColumn":"taxScheduleApplicableCountry","name":"Tax Schedule Applicable Country","desc":"Tax Schedule Applicable Country"},{"accepted_values":[],"templateColumnId":"c40","dataType":"String","isMandatory":"true","appColumn":"taxSchedule","name":"Tax Schedule","desc":"Tax Schedule"},{"accepted_values":[],"templateColumnId":"c41","dataType":"String","isMandatory":"true","appColumn":"applicableLawContract","name":"Applicable Law Contract","desc":"Applicable Law Contract"},{"accepted_values":[],"templateColumnId":"c42","dataType":"String","isMandatory":"true","appColumn":"arbitration","name":"Arbitration","desc":"Arbitration"},{"accepted_values":[],"templateColumnId":"c43","dataType":"String","isMandatory":"true","appColumn":"weightFinalAt","name":"Weight Final At","desc":"Weight Final At"},{"accepted_values":[],"templateColumnId":"c44","dataType":"String","isMandatory":"true","appColumn":"qualityFinalAt","name":"Quality Final At","desc":"Quality Final At"},{"accepted_values":[],"templateColumnId":"c45","dataType":"String","isMandatory":"true","appColumn":"approvalStatus","name":"Approval Status","desc":"Status of approval"}]', 'Y', 'N');

INSERT INTO `templates_configuration` 
(`TEMPLATE_ID`, `TEMPLATE_NAME`, `DESCRIPTION`, `SECURITY_REQUIRED`, `TEMPLATE_MAPPINGS`, `TEMPLATE_COLUMNS`, `IS_USER_ADDED_COLUMN_REQUIRED`, `IS_PLATFORM_TEMPLATE`) VALUES 
('contractStatus', 'Contract Status', 'This template contains information regarding the status of contract created from a bid.', 'Y', '{}', '[{"accepted_values":[],"templateColumnId":"c1","dataType":"String","isMandatory":"false","appColumn":"corporate","name":"Corporate","desc":"Corporate Name"},{"accepted_values":[],"templateColumnId":"c2","dataType":"String","isMandatory":"false","appColumn":"cpName","name":"CP Name","desc":"CP Name"},{"accepted_values":[],"templateColumnId":"c3","dataType":"String","name":"Offer Type","appColumn":"offerType","isMandatory":"false","desc":"Offer Type"},{"accepted_values":[],"templateColumnId":"c4","dataType":"String","name":"Offer No.","appColumn":"offerNo","isMandatory":"false","desc":"Offer No."},{"accepted_values":[],"templateColumnId":"c5","dataType":"String","name":"Bid Ref No.","appColumn":"bidRefNo","isMandatory":"false","desc":"Bid Ref No."},{"accepted_values":[],"templateColumnId":"c6","dataType":"String","name":"Status","appColumn":"status","isMandatory":"false","desc":"Status"},{"accepted_values":[],"templateColumnId":"c7","dataType":"String","name":"Reason For Failure","appColumn":"reasonForFailure","isMandatory":"false","desc":"Reason For Failure"}]', 'Y', 'N');

UPDATE `templates_configuration`
SET `TEMPLATE_COLUMNS` = '[{"accepted_values":[],"templateColumnId":"c1","dataType":"String","name":"PTA Ref Number","appColumn":"ptaRefNumber","isMandatory":"true","desc":"PTA Ref Number"},{"accepted_values":[],"templateColumnId":"c2","dataType":"String","name":"Product","appColumn":"product","isMandatory":"true","desc":"Product"},{"accepted_values":[],"templateColumnId":"c3","dataType":"String","name":"Quality","appColumn":"quality","isMandatory":"true","desc":"Quality"},{"accepted_values":[],"templateColumnId":"c4","dataType":"String","name":"Crop Year","appColumn":"cropYear","isMandatory":"true","desc":"Crop Year"},{"accepted_values":[],"templateColumnId":"c5","dataType":"String","name":"Location","appColumn":"location","isMandatory":"true","desc":"Location"},{"accepted_values":[],"templateColumnId":"c6","dataType":"Number","name":"Published Price","appColumn":"publishedPrice","isMandatory":"true","desc":"Published Price"},{"accepted_values":[],"templateColumnId":"c7","dataType":"Date","name":"Expiry Date","appColumn":"expiryDate","isMandatory":"true","desc":"Expiry Date"},{"accepted_values":[],"templateColumnId":"c8","dataType":"String","name":"Price Unit","appColumn":"priceUnit","isMandatory":"true","desc":"Published Price Unit"},{"accepted_values":[],"templateColumnId":"c9","dataType":"String","name":"INCO Term","appColumn":"incoTerm","isMandatory":"true","desc":"INCO Term"},{"accepted_values":[],"templateColumnId":"c10","dataType":"Number","name":"Quantity","appColumn":"quantity","isMandatory":"false","desc":"Offered Quantity"},{"accepted_values":[],"templateColumnId":"c11","dataType":"String","name":"Quantity Unit","appColumn":"quantityUnit","isMandatory":"false","desc":"Unit of Quanity"},{"accepted_values":[],"templateColumnId":"c13","dataType":"String","name":"Username","appColumn":"username","isMandatory":"false","desc":"Offeror Username"},{"accepted_values":[],"templateColumnId":"c14","dataType":"String","name":"Roles to Publish","appColumn":"rolesToPublish","isMandatory":"false","desc":"Roles to Publish"},{"accepted_values":["Purchase","Sale"],"templateColumnId":"c15","dataType":"String","name":"Offer Type","appColumn":"offerType","isMandatory":"false","desc":"Offer Type"},{"accepted_values":[],"templateColumnId":"c16","dataType":"Date","name":"Delivery From Date","appColumn":"deliveryFromDate","isMandatory":"true","desc":"Delivery From Date"},{"accepted_values":[],"templateColumnId":"c17","dataType":"Date","name":"Delivery To Date","appColumn":"deliveryToDate","isMandatory":"true","desc":"Delivery To Date"},{"accepted_values":[],"templateColumnId":"c18","dataType":"String","name":"Payment Terms","appColumn":"paymentTerms","isMandatory":"false","desc":"Payment Terms"},{"accepted_values":[],"templateColumnId":"c19","dataType":"String","name":"Packing Type","appColumn":"packingType","isMandatory":"false","desc":"Packing Type"},{"accepted_values":[],"templateColumnId":"c20","dataType":"String","name":"Packing Size","appColumn":"packingSize","isMandatory":"false","desc":"Packing Size"}]'
WHERE `TEMPLATE_ID`= 'priceSheet';

UPDATE `measure_master`
SET `mapped_templates` = '["priceSheet", "bidToContract", "contractStatus"]'
WHERE `id`= 'farmer_connect_dummy';

--//@UNDO

UPDATE `templates_configuration`
SET `TEMPLATE_COLUMNS` = '[{"accepted_values":[],"templateColumnId":"c1","dataType":"String","name":"PTA Ref Number","appColumn":"ptaRefNumber","isMandatory":"true","desc":"PTA Ref Number"},{"accepted_values":[],"templateColumnId":"c2","dataType":"String","name":"Product","appColumn":"product","isMandatory":"true","desc":"Product"},{"accepted_values":[],"templateColumnId":"c3","dataType":"String","name":"Quality","appColumn":"quality","isMandatory":"true","desc":"Quality"},{"accepted_values":[],"templateColumnId":"c4","dataType":"String","name":"Crop Year","appColumn":"cropYear","isMandatory":"true","desc":"Crop Year"},{"accepted_values":[],"templateColumnId":"c5","dataType":"String","name":"Location","appColumn":"location","isMandatory":"true","desc":"Location"},{"accepted_values":[],"templateColumnId":"c6","dataType":"Number","name":"Published Price","appColumn":"publishedPrice","isMandatory":"true","desc":"Published Price"},{"accepted_values":[],"templateColumnId":"c7","dataType":"Date","name":"Expiry Date","appColumn":"expiryDate","isMandatory":"true","desc":"Expiry Date"},{"accepted_values":[],"templateColumnId":"c8","dataType":"String","name":"Price Unit","appColumn":"priceUnit","isMandatory":"true","desc":"Published Price Unit"},{"accepted_values":[],"templateColumnId":"c9","dataType":"String","name":"INCO Term","appColumn":"incoTerm","isMandatory":"true","desc":"INCO Term"},{"accepted_values":[],"templateColumnId":"c10","dataType":"Number","name":"Quantity","appColumn":"quantity","isMandatory":"false","desc":"Offered Quantity"},{"accepted_values":[],"templateColumnId":"c11","dataType":"String","name":"Quantity Unit","appColumn":"quantityUnit","isMandatory":"false","desc":"Unit of Quanity"},{"accepted_values":[],"templateColumnId":"c12","dataType":"Date","name":"Shipment Date","appColumn":"shipmentDate","isMandatory":"false","desc":"Expected Shipment Date"},{"accepted_values":[],"templateColumnId":"c13","dataType":"String","name":"Username","appColumn":"username","isMandatory":"false","desc":"Offeror Username"},{"accepted_values":[],"templateColumnId":"c14","dataType":"String","name":"Roles to Publish","appColumn":"rolesToPublish","isMandatory":"false","desc":"Roles to Publish"},{"accepted_values":["Purchase","Sale"],"templateColumnId":"c15","dataType":"String","name":"Offer Type","appColumn":"offerType","isMandatory":"false","desc":"Offer Type"}]'
WHERE `TEMPLATE_ID`= 'priceSheet';

UPDATE `measure_master`
SET `mapped_templates` = '["priceSheet"]'
WHERE `id`= 'farmer_connect_dummy';

DELETE FROM `templates_configuration` WHERE `TEMPLATE_ID` IN ('bidToContract', 'contractStatus');