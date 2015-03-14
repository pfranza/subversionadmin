# Introduction #

The following instructions will show how to setup a subversion server running through Apache 2 over SSL (https) connection.  At the time of writing I am using Fedora 9, the linux flavor you choose will likely only affect the initial steps dealing with the package manager and installing software.

# Installing Packages #

  1. **Install Packages**
```
 yum -y install httpd subversion mod_dav_svn mod_ssl crypto-utils php
```
  1. **Setup a place for all of our files to reside to make backups easier**
```
   mkdir -p /cm
   mkdir -p /cm/etc  
   mkdir -p /cm/subversion
```
  1. **Setup a Subversion repository**
```
   svnadmin create /cm/subversion/repos
   chown -R apache /cm/subversion/repos
   mv /etc/httpd/conf.d/subversion.conf /etc/httpd/conf.d/subversion.conf.orig
```
> > Edit the contents of _/etc/httpd/conf.d/subversion.conf_ so it reads as the following
```

LoadModule dav_svn_module modules/mod_dav_svn.so
LoadModule authz_svn_module modules/mod_authz_svn.so

  <Location /svn>
    DAV svn
    SVNPath /cm/subversion/repos
    SVNListParentPath on
    AuthzSVNAccessFile /cm/etc/svnauthorz.conf
    AuthType Basic
    AuthName "Members Only"
    AuthUserFile /cm/etc/svn-users
    Order deny,allow
    Require valid-user
    SSLRequireSSL
 </Location>

```
  1. **Create the passwords file**
    * For the first user created the _htpasswd_ command uses the _-c_ flag to create a new file
```
htpasswd -cm /cm/etc/svn-users username
```
    * For subsequent user creations use
```
htpasswd -m /cm/etc/svn-users username
```
  1. **Generate a Selfsigned SSL Certificate**
```
    genkey --days 1024 servername
```
> > Then you need to edit _/etc/httpd/conf.d/ssl.conf_ to have it utilize your new certificate
```
  SSLCertificateFile /etc/pki/tls/certs/servername.cert
  SSLCertificateKeyFile /etc/pki/tls/private/servername.key
```
  1. **Setting the access permission levels**
> > Edit _/cm/etc/svnauthorz.conf_ so it contains the following
```
[/]
username = rw
```
  1. **Install the subversion administration tool**
> > _details soon_