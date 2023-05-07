# Installation Guide

## Introduction
Thank you for choosing our ice cream software. This guide will provide you with step-by-step instructions on how to install and get the software ready-to-use.

## Preconditions
* Github Profile: A Github Profile is needed in order to install the software. If you do not already have one, please create an account and login.
* Integrated Development Environment (IDE): An IDE (e.g. IntelliJ, Eclipse) is needed in order to run the software.

## Installation Process
### Fork from Github
* Fork the Github Repository into your own Github account.
* It is enough to only fork the Master Branch
* Open the forked Repository in your IDE.
### Set up and start the database server
The following guide uses MariaDB. While any other Database Server is possible, installation steps might vary.
* Download and install MariaDB: [Download](https://mariadb.org/download/?t=mariadb&p=mariadb&r=11.1.0&os=windows&cpu=x86_64&pkg=msi&m=hs-esslingen) and install the software and follow the instructions provided by the installer
* Start the MariaDB server: You might need to manually start the database server.

While it is possible to manage the Database through the MariaDB command prompt, for reasons of usability we recommend the installation of HeidiSQL.
* Download and install HeidiSQL: [Download](https://www.heidisql.com/download.php) and install the software and follow the instructions provided by the installer. Memorize the password you choose during installation.
* Connect to the database server: Open the program and create a connection to the database server by entering the root as user name, 127.0.0.1 as port number and your password as password. 
To ensure that the database is not empty, execute the [given SQL-File](../sql/eiscafegelatelli_query.sql) to fill the database with mockdata.

## Configuration
### Libraries
* Check the pom.xml file. You may need to add libraries to your classpath.

### Database Connection
To connect the Programm to the Database you just created, alter the [db.properties](https://github.com/tomheyden/ATdIT_Gelatelli/blob/ReadMe/Implementation/src/main/resources/db.properties) file to match your configurations.
