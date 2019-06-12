package com.github.maxopoly.kira.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class SSLContextFactory {

	public static SSLContext generate(File certFolder, String keyPassword) {
		SSLContext context;
		try {
			context = SSLContext.getInstance("TLSv1.2");

			byte[] certBytes = parseDERFromPEM(Files.readAllBytes(new File(certFolder, "cert.pem").toPath()),
					"-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
			byte[] keyBytes = parseDERFromPEM(Files.readAllBytes(new File(certFolder, "privkey.pem").toPath()),
					"-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
			byte[] chainBytes = parseDERFromPEM(Files.readAllBytes(new File(certFolder, "chain.pem").toPath()),
					"-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----"); 

			X509Certificate cert = generateCertificateFromDER(certBytes);
			RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);
			X509Certificate chain = generateCertificateFromDER(chainBytes);

			KeyStore keystore = KeyStore.getInstance("JKS");
			keystore.load(null);
			keystore.setCertificateEntry("cert-chain-alias", chain);
			keystore.setCertificateEntry("cert-alias", cert);
			keystore.setKeyEntry("key-alias", key, keyPassword.toCharArray(), new Certificate[] { cert });
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keystore, keyPassword.toCharArray());

			KeyManager[] km = kmf.getKeyManagers();

			context.init(km, null, null);
		} catch (IOException | KeyManagementException | KeyStoreException | InvalidKeySpecException
				| UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e) {
			throw new IllegalArgumentException();
		}
		return context;
	}

	protected static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
		String data = new String(pem);
		data = data.replace("\n", "");
		String[] tokens = data.split(beginDelimiter);
		tokens = tokens[1].split(endDelimiter);
		return Base64.getDecoder().decode(tokens[0]);
	}

	protected static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) factory.generatePrivate(spec);
	}

	protected static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException {
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
	}

	private SSLContextFactory() {
	}

}
