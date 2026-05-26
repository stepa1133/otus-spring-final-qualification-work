package ru.otus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix ="security.jwt")
public record JwtKeyPairProperties(RSAPrivateKey privateKey, RSAPublicKey publicKey) { }
