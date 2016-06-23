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

package psiprobe.controllers.truststore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.TomcatContainerController;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Creates an instance of SystemInformation POJO.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class TrustStoreController extends TomcatContainerController {

  /** The Constant Logger. */
  private static final Logger logger = LoggerFactory.getLogger(TrustStoreController.class);

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    List<Map<String, String>> certificateList = new ArrayList<>();
    try {
      String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType");
      KeyStore ks;
      if (trustStoreType != null) {
        ks = KeyStore.getInstance(trustStoreType);
      } else {
        ks = KeyStore.getInstance("JKS");
      }
      String trustStore = System.getProperty("javax.net.ssl.trustStore");
      String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
      if (trustStore != null) {
        ks.load(new FileInputStream(trustStore), trustStorePassword != null ? trustStorePassword.toCharArray() : null);
        Enumeration<String> aliases = ks.aliases();
        Map<String, String> attributes;
        while (aliases.hasMoreElements()) {
          attributes = new HashMap<>();
          String alias = aliases.nextElement();
          if (ks.getCertificate(alias).getType().equals("X.509")) {
            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

            attributes.put("alias", alias);
            attributes.put("cn", cert.getSubjectDN().toString());
            attributes.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd").format(cert.getNotAfter()));
            certificateList.add(attributes);
          }
        }
      }
    } catch (Exception e) {
      logger.error("There was an exception obtaining truststore: ", e);
    }
    ModelAndView mv = new ModelAndView(getViewName());
    mv.addObject("certificates", certificateList);
    return mv;
  }
}
