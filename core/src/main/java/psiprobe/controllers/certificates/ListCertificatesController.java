package psiprobe.controllers.certificates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.management.ObjectName;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.TomcatContainerController;
import psiprobe.model.certificates.Cert;
import psiprobe.model.certificates.ConnectorInfo;

public class ListCertificatesController extends TomcatContainerController {

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    // Acquiring System configured Key Store
    String keyStore = System.getProperty("javax.net.ssl.keyStore");
    if (keyStore != null && !"".equals(keyStore)) {
      File trustStoreFile = new File(keyStore);
      if (trustStoreFile.exists()) {
        String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        String keyStoreType = System.getProperty("javax.net.ssl.keyStoreType");
        List<Cert> certs = getCertificates(keyStoreType, trustStoreFile, keyStorePassword);
        modelAndView.addObject("systemKeyCerts", certs);
      }
    }

    // Acquiring System configured Trust Store
    String trustStore = System.getProperty("javax.net.ssl.trustStore");
    if (trustStore != null && !"".equals(trustStore)) {
      File trustStoreFile = new File(trustStore);
      if (trustStoreFile.exists()) {
        String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
        String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType");
        List<Cert> certs = getCertificates(trustStoreType, trustStoreFile, trustStorePassword);
        modelAndView.addObject("systemTrustCerts", certs);
      }
    }

    List<Connector> connectors = getContainerWrapper().getTomcatContainer().findConnectors();
    // TODO diogo exception handle
    List<ConnectorInfo> infos = getConnectorInfos(connectors);

    for (ConnectorInfo info : infos) {
      if (info.getKeyStoreFile() != null) {
        List<Cert> certs =
            getCertificates(info.getKeystoreType(), info.getKeyStoreFile(), info.getKeystorePass());
        info.setKeyStoreCerts(certs);
      }
      if (info.getTrustStoreFile() != null) {
        List<Cert> certs = getCertificates(info.getTruststoreType(), info.getTrustStoreFile(),
            info.getTruststorePass());
        info.setTrustStoreCerts(certs);
      }
    }

    return modelAndView.addObject("connectors", infos);
  }

  public List<Cert> getCertificates(String storeType, File storeFile, String storePassword) {
    try {
      List<Cert> certs = new ArrayList<>();
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
      try (InputStream storeInput = new FileInputStream(storeFile)) {
        keyStore.load(storeInput, password);
      }

      Enumeration<String> keystoreAliases = keyStore.aliases();
      while (keystoreAliases.hasMoreElements()) {
        String alias = keystoreAliases.nextElement();

        Certificate[] certificateChain = keyStore.getCertificateChain(alias);

        if (certificateChain != null) {
          for (int i = 0; i < certificateChain.length; i++) {
            X509Certificate x509Cert = (X509Certificate) certificateChain[i];
            addToStore(certs, alias, x509Cert);
          }
        } else {
          X509Certificate x509Cert = (X509Certificate) keyStore.getCertificate(alias);
          addToStore(certs, alias, x509Cert);
        }
      }

      return certs;

    } catch (KeyStoreException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (CertificateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<ConnectorInfo> getConnectorInfos(List<Connector> connectors)
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
        // TODO diogo exception handle
        infos.add(toConnectorInfo(protocol));
      }
    }
    return infos;
  }

  private ConnectorInfo toConnectorInfo(AbstractHttp11JsseProtocol<?> protocol)
      throws IllegalAccessException, InvocationTargetException {
    ConnectorInfo info = new ConnectorInfo();
    BeanUtils.copyProperties(info, protocol);
    info.setName(ObjectName.unquote(info.getName()));

    String keystoreFile = info.getKeystoreFile();
    if (keystoreFile != null && !"".equals(keystoreFile)) {
      File file = new File(keystoreFile);
      if (file.exists()) {
        info.setKeyStoreFile(file);
      }
    }
    String truststoreFile = info.getTruststoreFile();
    if (truststoreFile != null && !"".equals(truststoreFile)) {
      File file = new File(truststoreFile);
      if (file.exists()) {
        info.setTrustStoreFile(file);
      }
    }
    return info;
  }

  private void addToStore(List<Cert> certs, String alias, X509Certificate x509Cert) {
    Cert cert = new Cert();

    cert.setAlias(alias);
    cert.setSubjectDistinguishedName(x509Cert.getSubjectDN().toString());
    cert.setNotBefore(x509Cert.getNotBefore());
    cert.setNotAfter(x509Cert.getNotAfter());
    cert.setIssuerDistinguishedName(x509Cert.getIssuerDN().toString());

    certs.add(cert);
  }

}
