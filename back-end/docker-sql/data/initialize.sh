#!/bin/bash

# enable case sensitivity
chmod 0444 /etc/mysql/mysql.conf.d/mysqld.cnf

# mysql -u root -p --password=root linkedit < ~/dumpfile.sql