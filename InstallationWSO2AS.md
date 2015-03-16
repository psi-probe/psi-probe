

---


# Configuration #

Before you deploy PSI Probe to WSO2 AS, you must configure the server. This does not require you to restart the WSO2 AS.

## Security ##

PSI Probe requires four security roles (in order of increasing [privileges](Features#Features_by_Role.md)):

  * **probeuser**
  * **poweruser**
  * **poweruserplus**
  * **manager** - This is the same role required by Tomcat Manager and has the highest level of privileges.

First you must add the above user roles to WSO2 AS user store though its admin console as follows.

  1. Start the WSO2 AS and login to web based admin console.
    * **Note:** The URL will be something like this: https://localhost:9443/carbon/
  1. Navigate to "Roles" page: _Configure > Users and Roles > Roles_
  1. Add above four roles using "Add New Role" wizard.
    * **Note:** When the "Add New Role" wizard ask for permission for each role you may simply select "All Permissions" for testing purposes. However, for production servers it is highly recommended to review and assign necessary permissions on graphical tree view.
  1. Create a new user or select an existing user from "Users" page _Configure > Users and Roles > Users_ and assign one or more above created roles.

By default WSO2 AS comes with a default user called "admin" with highest administrative privileges.  For testing purposes, you may select this user and assign the above roles.

# Deployment #

Once you have completed the configuration steps, it's time to deploy PSI Probe.

## Option 1: Deploy with WSO2 Admin Console ##

  1. Login to WSO2 AS admin console if you are not already logged-in.
    * **Note:** The URL will be something like this: https://localhost:9443/carbon/
  1. In the navigator, under Manage/Web Applications, click **Add**.
  1. Upload probe.war using the "Upload" button.
  1. After a few second you should able to see "probe" as a deployed application on “Running Applications” page _Applications > List_.

## Option 2: Manually Copy to Server ##

  1. While WSO2 AS running copy probe.war file into `$Carbon_HOME/repository/deployment/server/webapps` directory in the server.

# Testing #

Once you have deployed PSI Probe to WSO2 AS, verify that you can access it in your web browser by entering PSI Probe's URL:

  * HTTP: http://localhost:9763/probe/
  * HTTPS: https://localhost:9443/probe/

When you are prompted for a username and password, enter the credentials for the default "admin" account.

If you have any problems, see our [Troubleshooting](Troubleshooting.md) page.