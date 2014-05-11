SET FOREIGN_KEY_CHECKS=0;

/* prepopulate tables DC */
USE `datacustodian`;
source /home/bitnami/git/energyos/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/prepopulatesql_users_dc.sql
source /home/bitnami/git/energyos/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/prepopulatesql_applicationinformation_dc.sql
source /home/bitnami/git/energyos/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/prepopulatesql_tokenstore.sql

SET FOREIGN_KEY_CHECKS=1;

