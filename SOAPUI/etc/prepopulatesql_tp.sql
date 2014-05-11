SET FOREIGN_KEY_CHECKS=0;

/* prepopulate tables TP */
USE `thirdparty`;
source /home/bitnami/git/energyos/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/prepopulatesql_users_tp.sql
source /home/bitnami/git/energyos/OpenESPI-GreenButtonCMDTest/SOAPUI/etc/prepopulatesql_applicationinformation_tp.sql

SET FOREIGN_KEY_CHECKS=1;

