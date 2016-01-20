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
 * The Class SecurityUtils.
 *
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class SecurityUtils {

  /**
   * Checks for attribute value role.
   *
   * @param servletContext the servlet context
   * @param request the request
   * @return true, if successful
   */
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

  /**
   * User has role.
   *
   * @param privilegedRole the privileged role
   * @return true, if successful
   */
  private static boolean userHasRole(String privilegedRole) {
    Collection<? extends GrantedAuthority> authorities =
        SecurityContextHolder.getContext().getAuthentication().getAuthorities();

    boolean result = false;
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    while (iterator.hasNext()) {
      if (privilegedRole.equals(iterator.next().getAuthority())) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * User has role.
   *
   * @param privilegedRole the privileged role
   * @param request the request
   * @return true, if successful
   */
  private static boolean userHasRole(String privilegedRole, HttpServletRequest request) {
    return request.isUserInRole(privilegedRole);
  }

  /**
   * Gets the privileged roles.
   *
   * @param servletContext the servlet context
   * @return the privileged roles
   */
  private static String getPrivilegedRoles(ServletContext servletContext) {
    return servletContext.getInitParameter("attribute.value.roles");
  }

}
