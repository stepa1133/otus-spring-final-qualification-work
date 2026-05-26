package ru.otus.dutyschedule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtPublicKeyProperties(RSAPublicKey publicKey) { }
