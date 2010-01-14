/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.beans;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class TomcatUsersDaoBean implements UserDetailsService, InitializingBean {

    private Map userDetailMap = new LinkedHashMap();
    private Map roleNameMap = new LinkedHashMap();
    private String tomcatUsersFile = "tomcat-users.xml";

    public Map getRoleNameMap() {
        return roleNameMap;
    }

    public void setRoleNameMap(Map roleNameMap) {
        this.roleNameMap = roleNameMap;
    }

    public String getTomcatUsersFile() {
        return tomcatUsersFile;
    }

    public void setTomcatUsersFile(String tomcatUsersFile) {
        this.tomcatUsersFile = tomcatUsersFile;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserDetails user = (UserDetails) userDetailMap.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid user: "+username);
        }

        return user;
    }

    public void afterPropertiesSet() throws Exception {
        File configBase = new File(System.getProperty("catalina.base"), "conf");
        parseTomcatUsersXml(new File(configBase, tomcatUsersFile));
    }

    private void parseTomcatUsersXml(File tomcatUsers) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder builder = docFactory.newDocumentBuilder();
        Document doc = builder.parse(tomcatUsers);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPathExpression userXPath = xPathFactory.newXPath().compile("/tomcat-users/user");

        NodeList list = (NodeList) userXPath.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {

            NamedNodeMap nnm = list.item(i).getAttributes();
            String username = nnm.getNamedItem("username").getNodeValue();
            String password = nnm.getNamedItem("password").getNodeValue();
            String roleCsv = nnm.getNamedItem("roles").getNodeValue();

            String roles[] = roleCsv.split(",");
            GrantedAuthority authorities[] = new GrantedAuthority[roles.length];

            for (int j = 0; j < roles.length; j++) {
                String mappedName = (String) roleNameMap.get(roles[j]);
                if (mappedName == null) mappedName = roles[j];
                authorities[j] = new GrantedAuthorityImpl(mappedName);
            }
            User user = new User(username, password, true, true, true, true, authorities);
            userDetailMap.put(username, user);
        }
    }
}
