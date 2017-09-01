// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.apache.cloudstack.framework.ca;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public interface CAProvider {

    /**
     * Method returns capability of the plugin to participate in certificate issuance, revocation and provisioning
     * @return
     */
    boolean canProvisionCertificates();

    /**
     * Returns root CA certificate
     * @return returns concatenated root CA certificate string
     */
    List<X509Certificate> getCaCertificate();

    /**
     * Issues certificate with provided options
     * @param domainNames
     * @param ipAddresses
     * @param validityDays
     * @return
     */
    Certificate issueCertificate(final List<String> domainNames, final List<String> ipAddresses, final int validityDays);

    /**
     * Issues certificate using given CSR and other options
     * @param csr
     * @param domainNames
     * @param ipAddresses
     * @param validityDays
     * @return
     */
    Certificate issueCertificate(final String csr, final List<String> domainNames, final List<String> ipAddresses, final int validityDays);

    /**
     * Revokes certificate using certificate serial and CN
     * @param certSerial
     * @param certCn
     * @return returns true on success
     */
    boolean revokeCertificate(final BigInteger certSerial, final String certCn);

    /**
     * This method can add/inject custom TrustManagers for client connection validations.
     * @param sslContext The SSL context used while accepting a client connection
     * @param remoteAddress
     * @param certMap
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    SSLEngine createSSLEngine(final SSLContext sslContext, final String remoteAddress, final Map<String, X509Certificate> certMap) throws GeneralSecurityException, IOException;

    /**
     * Returns the unique name of the provider
     * @return
     */
    String getProviderName();

    /**
     * Returns description about the CA provider plugin
     * @return
     */
    String getDescription();
}