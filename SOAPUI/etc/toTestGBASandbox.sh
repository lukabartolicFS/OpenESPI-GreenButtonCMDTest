cp prepopulatesql_newthirdparty_testgbasandbox.sql prepopulatesql_newthirdparty.sql
cp prepopulatesql_applicationinformation_dc_testgbasandbox.sql prepopulatesql_applicationinformation_dc.sql
cp prepopulatesql_tokenstore_testgbasandbox.sql prepopulatesql_tokenstore.sql
cp gbcmdcert_target_testgbasandbox.conf gbcmdcert_target.conf
cp gbcmd_secure.conf gbcmd.conf
./initializedatabases.sh
