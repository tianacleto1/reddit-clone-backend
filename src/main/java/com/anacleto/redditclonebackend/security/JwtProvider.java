package com.anacleto.redditclonebackend.security;

import com.anacleto.redditclonebackend.exception.FailToSendEmailException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;
    private static final String PRIVATE_KEY = System.getenv("KEYSTORE_PRIVATE_KEY");

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/redditclone.jks");

            keyStore.load(resourceAsStream, PRIVATE_KEY.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException ex) {
            // TODO: Add a personalized exception
            throw new FailToSendEmailException("Exception occurred while loading keystore!");
        }
    }

    public String generateToken(Authentication authentication) {
        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);

        return true;
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                   .setSigningKey(getPublicKey())
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();

    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("redditclone", PRIVATE_KEY.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            // TODO: Add a personalized exception
            throw new FailToSendEmailException("Exception occurred while retrieving public key from keystore");
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("redditclone").getPublicKey();
        } catch (KeyStoreException ex) {
            // TODO add a psersonalized exception
            throw new FailToSendEmailException("An error occurred while retrieving public key from keystore!");
        }
    }
}
