/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package psiprobe.tools;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The Class SecurityUtils.
 */
public class SecurityUtils {

  /**
   * Prevent Instantiation of security utils.
   */
  private SecurityUtils() {
    // Prevent Instantiation
  }

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
    for (GrantedAuthority authority : authorities) {
      if (privilegedRole.equals(authority.getAuthority())) {
        result = true;
        break;
      }
    }
    return result;
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
