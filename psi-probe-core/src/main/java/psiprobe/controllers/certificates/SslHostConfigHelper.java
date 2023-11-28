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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;

import psiprobe.model.certificates.CertificateInfo;
import psiprobe.model.certificates.ConnectorInfo;
import psiprobe.model.certificates.SslHostConfigInfo;

/**
 * The Class SslHostConfigHelper.
 */
public class SslHostConfigHelper {

  /**
   * Instantiates a new SSL host config helper.
   *
   * @param protocol the protocol
   * @param info the info
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  public SslHostConfigHelper(AbstractHttp11JsseProtocol<?> protocol, ConnectorInfo info)
      throws IllegalAccessException, InvocationTargetException {
    SSLHostConfig[] sslHostConfigs = protocol.findSslHostConfigs();
    List<SslHostConfigInfo> sslHostConfigInfos = new ArrayList<>(sslHostConfigs.length);
    info.setSslHostConfigInfos(sslHostConfigInfos);

    for (SSLHostConfig sslHostConfig : sslHostConfigs) {
      sslHostConfigInfos.add(toSslHostConfigInfo(sslHostConfig));
    }
  }

  /**
   * To SslHostConfig info.
   *
   * @param sslHostConfig the SslHostConfig
   *
   * @return the SslHostConfig info
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private SslHostConfigInfo toSslHostConfigInfo(SSLHostConfig sslHostConfig)
      throws IllegalAccessException, InvocationTargetException {
    SslHostConfigInfo sslHostConfigInfo = new SslHostConfigInfo();
    BeanUtils.copyProperties(sslHostConfigInfo, sslHostConfig);

    Set<SSLHostConfigCertificate> certificates = sslHostConfig.getCertificates();
    List<CertificateInfo> certificateInfos = new ArrayList<>(certificates.size());
    for (SSLHostConfigCertificate sslHostConfigCertificate : certificates) {
      certificateInfos.add(toCertificateInfo(sslHostConfigCertificate));
    }
    sslHostConfigInfo.setCertificateInfos(certificateInfos);

    return sslHostConfigInfo;
  }

  /**
   * To certificate info.
   *
   * @param sslHostConfigCertificate the SslHostConfigCertificate
   *
   * @return the certificate info
   *
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   */
  private CertificateInfo toCertificateInfo(SSLHostConfigCertificate sslHostConfigCertificate)
      throws IllegalAccessException, InvocationTargetException {
    CertificateInfo certificateInfo = new CertificateInfo();
    BeanUtils.copyProperties(certificateInfo, sslHostConfigCertificate);
    return certificateInfo;
  }
}
