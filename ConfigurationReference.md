# Introduction #

Below is a sample configuration file and some explanation of the properties being set


```
########################
# Global Options
########################
svnadministrator.config.home = /etc/svndata/

########################
# SVN Repository Setup
########################
svnadministrator.svn.url = http://your.server.here/svn
svnadministrator.svn.username = username  #If Required
svnadministrator.svn.password = password  #If Required
svnadministrator.svn.authorsfile = ${svnadministrator.config.home}\svnauthorz

#######################
# User Account Setup
#######################

## Choose between local account or active directory accounts
svnadministrator.users.useActiveDirectory = false  

############
# Local Account Options
svnadministrator.svn.passwordsfile = ${svnadministrator.config.home}\svnpasswordz

############
# Active Directory Account Options
svnadministrator.users.ad.url = ldap://127.0.0.1
svnadministrator.users.ad.username = username
svnadministrator.users.ad.password = password
svnadministrator.users.ad.admingroup = svn_admin_group


#######################
# Email Options
#######################
svnadministrator.email.enable = true
svnadministrator.email.smtpserver = mail.host.com
svnadministrator.email.defaultsender = svn-changes@localhost.com

```