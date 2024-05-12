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
package psiprobe.controllers.certificates;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.management.ObjectName;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.AbstractTomcatContainerController;
import psiprobe.model.certificates.Cert;
import psiprobe.model.certificates.CertificateInfo;
import psiprobe.model.certificates.ConnectorInfo;
import psiprobe.model.certificates.SslHostConfigInfo;

/**
 * The Class ListCertificatesController.
 */
@Controller
public class ListCertificatesController extends AbstractTomcatContainerController {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ListCertificatesController.class);

  @RequestMapping(path = "/certificates.htm")
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return super.handleRequest(request, response);
  }

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    try {
      List<Connector> connectors = getContainerWrapper().getTomcatContainer().findConnectors();
      List<ConnectorInfo> infos = getConnectorInfos(connectors);

      for (ConnectorInfo info : infos) {
        List<Cert> certs;

        List<SslHostConfigInfo> sslHostConfigInfos = info.getSslHostConfigInfos();
        for (SslHostConfigInfo sslHostConfigInfo : sslHostConfigInfos) {
          if (sslHostConfigInfo.getTruststoreFile() != null) {
            certs = getCertificates(sslHostConfigInfo.getTruststoreType(),
                sslHostConfigInfo.getTruststoreFile(), sslHostConfigInfo.getTruststorePassword());
            sslHostConfigInfo.setTrustStoreCerts(certs);
          }

          List<CertificateInfo> certificateInfos = sslHostConfigInfo.getCertificateInfos();
          for (CertificateInfo certificateInfo : certificateInfos) {
            if (certificateInfo.getCertificateKeystoreFile() != null) {
              certs = getCertificates(certificateInfo.getCertificateKeystoreType(),
                  certificateInfo.getCertificateKeystoreFile(),
                  certificateInfo.getCertificateKeystorePassword());
              certificateInfo.setKeyStoreCerts(certs);
            }
          }
          sslHostConfigInfo.setCertificateInfos(certificateInfos);
        }
        info.setSslHostConfigInfos(sslHostConfigInfos);
      }

      modelAndView.addObject("connectors", infos);
    } catch (Exception e) {
      logger.error("There was an exception listing certificates", e);
    }

    return modelAndView;

  }

  /**
   * Gets the certificates.
   *
   * @param storeType the store type
   * @param storeFile the store file
   * @param storePassword the store password
   *
   * @return the certificates
   *
   * @throws Exception the exception
   */
  public List<Cert> getCertificates(String storeType, String storeFile, String storePassword)
      throws Exception {
    KeyStore keyStore;

    // Get key store
    if (storeType != null) {
      keyStore = KeyStore.getInstance(storeType);
    } else {
      keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    }

    // Get password
    char[] password = null;
    if (storePassword != null) {
      password = storePassword.toCharArray();
    }

    // Load key store from file
    try (InputStream storeInput = getStoreInputStream(storeFile)) {
      keyStore.load(storeInput, password);
    } catch (IOException e) {
      logger.error("Error loading store file {}", storeFile, e);
      return Collections.emptyList();
    }

    List<Cert> certs = new ArrayList<>();

    for (String alias : Collections.list(keyStore.aliases())) {

      Certificate[] certificateChains = keyStore.getCertificateChain(alias);

      if (certificateChains != null) {
        for (Certificate certificateChain : certificateChains) {
          X509Certificate x509Cert = (X509Certificate) certificateChain;
          addToStore(certs, alias, x509Cert);
        }
      } else {
        X509Certificate x509Cert = (X509Certificate) keyStore.getCertificate(alias);
        addToStore(certs, alias, x509Cert);
      }
    }
    return certs;
  }

  /**
   * Gets the connector infos.
   *
   * @param connectors the connectors
   *
   * @return the connector infos
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private List<ConnectorInfo> getConnectorInfos(Collection<Connector> connectors)
      throws IllegalAccessException, InvocationTargetException {
    List<ConnectorInfo> infos = new ArrayList<>();
    for (Connector connector : connectors) {
      if (!connector.getSecure()) {
        continue;
      }

      ProtocolHandler protocolHandler = connector.getProtocolHandler();

      if (protocolHandler instanceof AbstractHttp11JsseProtocol) {
        AbstractHttp11JsseProtocol<?> protocol = (AbstractHttp11JsseProtocol<?>) protocolHandler;
        if (!protocol.getSecure()) {
          continue;
        }
        infos.add(toConnectorInfo(protocol));
      }
    }
    return infos;
  }

  /**
   * Tries to open a InputStream the same way as Tomcat ConfigFileLoader
   * {@link org.apache.tomcat.util.file.ConfigFileLoader#getSource() getSource.getResource(String).getInputStream()}.
   *
   * @param path the path of a store file (absolute or relative to CATALINA.BASE), or URI to store
   *        file (absolute or relative to CATALINA.BASE).
   *
   * @return the input stream of the path file
   *
   * @throws IOException if path can not be resolved
   */
  private InputStream getStoreInputStream(String path) throws IOException {
    File file = new File(path);
    if (file.exists()) {
      return Files.newInputStream(file.toPath());
    }

    File catalinaBaseFolder = new File(System.getProperty("catalina.base"));
    file = new File(catalinaBaseFolder, path);
    if (file.exists()) {
      return Files.newInputStream(file.toPath());
    }

    URI uri = catalinaBaseFolder.toURI().resolve(path);

    URL url = uri.toURL();

    return url.openConnection().getInputStream();
  }

  /**
   * To connector info.
   *
   * @param protocol the protocol
   *
   * @return the connector info
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private ConnectorInfo toConnectorInfo(AbstractHttp11JsseProtocol<?> protocol)
      throws IllegalAccessException, InvocationTargetException {
    ConnectorInfo info = new ConnectorInfo();
    info.setName(ObjectName.unquote(protocol.getName()));

    String defaultSslHostConfigName = protocol.getDefaultSSLHostConfigName();
    if (defaultSslHostConfigName == null) {
      logger.error("Cannot determine defaultSslHostConfigName");
      return info;
    }
    info.setDefaultSslHostConfigName(defaultSslHostConfigName);
    new SslHostConfigHelper(protocol, info);

    return info;
  }

  /**
   * Adds the to store.
   *
   * @param certs the certs
   * @param alias the alias
   * @param x509Cert the x509 cert
   */
  private void addToStore(Collection<Cert> certs, String alias, X509Certificate x509Cert) {
    Cert cert = new Cert();

    cert.setAlias(alias);
    cert.setSubjectDistinguishedName(x509Cert.getSubjectX500Principal().toString());
    cert.setNotBefore(x509Cert.getNotBefore().toInstant());
    cert.setNotAfter(x509Cert.getNotAfter().toInstant());
    cert.setIssuerDistinguishedName(x509Cert.getIssuerX500Principal().toString());

    certs.add(cert);
  }

  @Value("certificates")
  @Override
  public void setViewName(String viewName) {
    super.setViewName(viewName);
  }

}
