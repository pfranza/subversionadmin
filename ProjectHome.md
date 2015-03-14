# Subversion Administration #

## Features ##
  * Manage Local User Accounts
  * Supports Active Directory Integration (ldap)
  * Manage svn access control lists
  * Allows users to subscribe to projects
  * Trigger Emails to subscribed users

## Setup ##

  1. Download the .war file and place in a servlet container such as jetty, tomcat or jboss
  1. Create a [configuration file](ConfigurationReference.md).
  1. Reference the configuration file location using one of the following
    * Enviroment Variable (e.g. export subversionadmin.configuration=...)
    * System Property (e.g. -Dsubversionadmin.configuration=...)
    * Servlet Context Parameter  e.g.
> > > 

&lt;init-param&gt;


> > > > 

&lt;param-name&gt;

 subversionadmin.configuration

&lt;/param-name&gt;


> > > > 

&lt;param-value&gt;

 ...

&lt;/param-value&gt;



> > > 

&lt;/init-param&gt;


  1. [Setup Post Commit Hook](SetupEmailHook.md) (for email notification)
  1. Start Service

## Usage ##

### Logging In ###

#### Local Accounts ####
If you are using local accounts then by default the username and password is admin/admin until another user account is created once another administrator account is made the default admin account will automatically shutoff.

If your account is designated as an administrator account then logging in will bring you into the application.  If your account is not an administrator account then logging in will bring you to a dialog that will allow you to change your password.

#### Active Directory (ldap) Accounts ####
To login you need to type your full ldap principle name (likely username@domain.suffix) and your password.  When using the active directory module the application automatically removes the user account management module.  You will only be designated as an administrator if your account is a member of the administrators group that you specified in the configuration file.

### User Management ###

This area is as simple as it gets.
  1. Click Create User

> > This will bring up a dialog that allows you to specify the users name.  Enter the desired username and press ok.  The name will appear in the list.
  1. Select the user and press Modify
> > The will allow you to set the users email address, their password and their status as an administrator.
  * Select the user and press Delete
> > This will remove the user from the system

### Group Management ###

This area allows you to group users together
  * Create Group
> > Displays a dialog allowing you to input the group name
  * Modify Group
> > Displays a dialog allowing you to add or remove users from the group
  * Delete Group
> > Removes the group from the system

### Repository Management ###

This is where most of the work is done. On this page you can have the tool automatically scan from project paths.  A project path is a folder that contains trunk, branches & tags subdirectories.  Additionally you can add a project location if your repository setup doesn't follow this convention.

Then you can select the project path from the list and manage a users subscription to it.  This determines whether or not they receive an email from the post commit hook.  And you can manage access, you can determine if a group has write, read or no access to a particular project.