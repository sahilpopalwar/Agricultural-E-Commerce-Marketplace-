package org.project.oopjava.util;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class KeystoreGenerator {
    public static void main(String[] args) throws Exception {
        String configuredPassword = System.getenv("SSL_KEYSTORE_PASSWORD");
        if (configuredPassword == null || configuredPassword.isBlank()) {
            throw new IllegalStateException("SSL_KEYSTORE_PASSWORD must be set");
        }
        char[] password = configuredPassword.toCharArray();
        String keystorePath = "keystore.p12";

        // Generate key pair
        java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        java.security.KeyPair keyPair = keyGen.generateKeyPair();

        // Create keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, password);

        // Add the key pair to the keystore
        keyStore.setKeyEntry("tomcat", keyPair.getPrivate(), password, new Certificate[0]);

        // Save the keystore
        try (FileOutputStream fos = new FileOutputStream(keystorePath)) {
            keyStore.store(fos, password);
        }

        System.out.println("Keystore generated successfully at: " + keystorePath);
    }
}