SET FOREIGN_KEY_CHECKS=0;
SET SESSION sql_mode='NO_AUTO_VALUE_ON_ZERO';

SET sql_mode = 'PIPES_AS_CONCAT';

SET @dcbaseurl = 'https://openespivm:8443';
SET @tpbaseurl = 'https://surface:8444';
SET @aiIndex = '4';
SET @clientId = 'surface_tp';

USE `datacustodian`;

/* Add application_information */
INSERT INTO `application_information` (`id`, `kind`, `description`, `published`, `self_link_href`, `self_link_rel`, `up_link_href`, `up_link_rel`, `updated`, `uuid`, `authorizationServerAuthorizationEndpoint`, `authorizationServerRegistrationEndpoint`, `authorizationServerTokenEndpoint`, `authorizationServerUri`, `clientId`, `clientIdIssuedAt`, `clientName`, `clientSecret`, `clientSecretExpiresAt`, `clientUri`, `contacts`, `dataCustodianApplicationStatus`, `dataCustodianBulkRequestURI`, `dataCustodianDefaultBatchResource`, `dataCustodianDefaultSubscriptionResource`, `dataCustodianId`, `dataCustodianResourceEndpoint`, `dataCustodianThirdPartySelectionScreenURI`, `logoUri`, `policyUri`, `redirectUri`, `registrationAccessToken`, `registrationClientUri`, `responseTypes`, `softwareId`, `softwareVersion`, `thirdPartyApplicationDescription`, `thirdPartyApplicationName`, `thirdPartyApplicationStatus`, `thirdPartyApplicationType`, `thirdPartyApplicationUse`, `thirdPartyDataCustodianSelectionScreenURI`, `thirdPartyLoginScreenURI`, `thirdPartyNotifyUri`, `thirdPartyPhone`, `thirdPartyScopeSelectionScreenURI`, `thirdPartyUserPortalScreenURI`, `tokenEndpointAuthMethod`, `tosUri`, `dataCustodianScopeSelectionScreenURI`) VALUES (@aiIndex, 'THIRD_PARTY', 'GreenButtonData.org  ThirdParty Application', '2014-01-02 05:00:00', '/espi/1_1/resource/DataCustodian/ApplicationInformation/' || @aiIndex, 'self', '/espi/1_1/resource/DataCustodian/ApplicationInformation', 'up', '2014-01-02 05:00:00', 'AF6E8B03-0299-467E-972A-A883ECDCC575', @dcbaseurl || '/DataCustodian/oauth/authorize', @dcbaseurl || '/DataCustodian/espi/1_1/register', @dcbaseurl || '/DataCustodian/oauth/token', @dcbaseurl || '/DataCustodian/', @clientId, 1403190000, 'Green Button Third Party', 'secret', 0, @tpbaseurl || '/ThirdParty', 'john.teeter@energyos.org,martin.burns@nist.gov,donald.coffin@reminetworks.com', '1', @dcbaseurl || '/DataCustodian/espi/1_1/resource/Batch/Bulk', @dcbaseurl || '/DataCustodian/espi/1_1/resource/Batch', @dcbaseurl || '/DataCustodian/espi/1_1/resource/Batch/Subscription', 'data_custodian', @dcbaseurl || '/DataCustodian/espi/1_1/resource', @dcbaseurl || '/DataCustodian/RetailCustomer/1/ThirdPartyList', @tpbaseurl || '/ThirdParty/resources/ico/favicon.png', NULL, @tpbaseurl || '/ThirdParty/espi/1_1/OAuthCallBack', 'd89bb056-0f02-4d47-9fd2-ec6a19ba8d0c', @dcbaseurl || '/DataCustodian/espi/1_1/resource/ApplicationInformation/' || @aiIndex, 1, 'EnergyOS OpenESPI Example Third Party', '1.2', 'EnergyOS OpenESPI Example Third Party', 'Third Party (localhost)', '1', '1', '1', @tpbaseurl || '/ThirdParty/RetailCustomer/{retailCustomerId}/DataCustodianList', @tpbaseurl || '/ThirdParty/login', @tpbaseurl || '/ThirdParty/espi/1_1/Notification', NULL, @tpbaseurl || '/ThirdParty/RetailCustomer/ScopeSelection', @tpbaseurl || '/ThirdParty', 'client_secret_basic', NULL, @dcbaseurl || '/DataCustodian/RetailCustomer/ScopeSelectionList?ThirdPartyID=' || @clientId);

/* Add application_information_scopes */ 
INSERT INTO application_information_scopes (application_information_id, scope) VALUES (@aiIndex, 'FB=1_3_4_5_13_14_39;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13');
INSERT INTO application_information_scopes (application_information_id, scope) VALUES (@aiIndex, 'FB=1_3_4_5_13_14_15_39;IntervalDuration=900;BlockDuration=monthly;HistoryLength=13');
INSERT INTO application_information_scopes (application_information_id, scope) VALUES (@aiIndex, 'FB=1_3_4_5_13_14_39;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13');
INSERT INTO application_information_scopes (application_information_id, scope) VALUES (@aiIndex, 'FB=1_3_4_5_6_7_8_9_10_11_29_12_13_14_15_16_17_18_19_27_28_32_33_34_35_37_38_39_40_41_44;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13');

/* Add application_information_grant_types */ 
INSERT INTO application_information_grant_types (application_information_id, grantTypes) VALUES (@aiIndex, 'AUTHORIZATION_CODE');
INSERT INTO application_information_grant_types (application_information_id, grantTypes) VALUES (@aiIndex, 'REFRESH_TOKEN');

USE `tokenstore`;

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;

/* Add client details */
INSERT INTO `oauth_client_details` VALUES 
('REGISTRATION_' ||@clientId,NULL,'secret','FB=36_40','client_credentials',NULL,'ROLE_TP_REGISTRATION',315360000,NULL,NULL,'FALSE'),
(@clientId,NULL,'secret','FB=1_3_4_5_13_14_39;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13,FB=1_3_4_5_13_14_15_39;IntervalDuration=900;BlockDuration=monthly;HistoryLength=13,FB=1_3_4_5_13_14_39;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13,FB=1_3_4_5_6_7_8_9_10_11_29_12_13_14_15_16_17_18_19_27_28_32_33_34_35_37_38_39_40_41_44;IntervalDuration=3600;BlockDuration=monthly;HistoryLength=13','authorization_code,refresh_token', @tpbaseurl || '/ThirdParty/espi/1_1/OAuthCallBack','ROLE_USER',315360000,630720000,NULL,'FALSE'),
(@clientId ||'_admin',NULL,'secret','FB=34_35','client_credentials',NULL,'ROLE_TP_ADMIN',315360000,NULL,NULL,'FALSE');

/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;

