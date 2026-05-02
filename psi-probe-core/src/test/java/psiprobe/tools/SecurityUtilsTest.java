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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codebox.bean.JavaBeanTester;

import jakarta.servlet.ServletContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Tests for {@link SecurityUtils}.
 */
class SecurityUtilsTest {

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void testPrivateConstructor() {
    JavaBeanTester.builder(SecurityUtils.class).testPrivateConstructor();
  }

  @Test
  void testHasAttributeValueRoleTrue() {
    Collection<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", authorities));

    ServletContext context = mock(ServletContext.class);
    when(context.getInitParameter("attribute.value.roles")).thenReturn("ROLE_ADMIN");

    assertTrue(SecurityUtils.hasAttributeValueRole(context));
  }

  @Test
  void testHasAttributeValueRoleFalse() {
    Collection<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", authorities));

    ServletContext context = mock(ServletContext.class);
    when(context.getInitParameter("attribute.value.roles")).thenReturn("ROLE_ADMIN");

    assertFalse(SecurityUtils.hasAttributeValueRole(context));
  }

  @Test
  void testHasAttributeValueRoleMultipleRoles() {
    Collection<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"));
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", authorities));

    ServletContext context = mock(ServletContext.class);
    when(context.getInitParameter("attribute.value.roles"))
        .thenReturn("ROLE_ADMIN,ROLE_MANAGER,ROLE_USER");

    assertTrue(SecurityUtils.hasAttributeValueRole(context));
  }

  @Test
  void testHasAttributeValueRoleNoMatch() {
    Collection<GrantedAuthority> authorities =
        Arrays.asList(new SimpleGrantedAuthority("ROLE_A"), new SimpleGrantedAuthority("ROLE_B"));
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass", authorities));

    ServletContext context = mock(ServletContext.class);
    when(context.getInitParameter("attribute.value.roles")).thenReturn("ROLE_X,ROLE_Y");

    assertFalse(SecurityUtils.hasAttributeValueRole(context));
  }

  @Test
  void testHasAttributeValueRoleEmptyAuthorities() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("user", "pass", Collections.emptyList()));

    ServletContext context = mock(ServletContext.class);
    when(context.getInitParameter("attribute.value.roles")).thenReturn("ROLE_ADMIN");

    assertFalse(SecurityUtils.hasAttributeValueRole(context));
  }
}
