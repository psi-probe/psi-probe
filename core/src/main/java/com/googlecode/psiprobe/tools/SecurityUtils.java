/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package com.googlecode.psiprobe.tools;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class SecurityUtils {

  private SecurityUtils() {}

  public static boolean hasAttributeValueRole(ServletContext servletContext,
      HttpServletRequest request) {

    String[] privilegedRoles = getPrivilegedRoles(servletContext).split(",");
    for (String privilegedRole : privilegedRoles) {
      if (userHasRole(privilegedRole)) {
        return true;
      }
    }
    return false;
  }

  private static boolean userHasRole(String privilegedRole) {
    Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

    boolean result = false;
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    while (iterator.hasNext()) {
      if (privilegedRole.equals(iterator.next())) {
        result = true;
        break;
      }
    }
    return result;
  }

  private static boolean userHasRole(String privilegedRole, HttpServletRequest request) {
    return request.isUserInRole(privilegedRole);
  }

  private static String getPrivilegedRoles(ServletContext servletContext) {
    return servletContext.getInitParameter("attribute.value.roles");
  }

}
