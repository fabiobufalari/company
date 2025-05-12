package com.bufalari.company.security;

// --- IMPORTS CORRIGIDOS ---
import io.jsonwebtoken.*; // Imports base como Claims, ExpiredJwtException, etc.
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // <<< Import CORRETO para erro de assinatura JJWT
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.oauth2.jwt.JwtException; // <<< REMOVER ESTE IMPORT (OAuth2)
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
// import java.security.SignatureException; // <<< REMOVER ESTE IMPORT (java.security)
import java.util.Date;
import java.util.function.Function;

/**
 * Utility for handling JWT tokens (validation, extraction) using a configured secret key.
 * Utilitário para manipulação de tokens JWT (validação, extração), usando uma chave secreta configurada.
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // Injetar a chave secreta do application.yml/properties
    @Value("${security.jwt.token.secret-key}")
    private String configuredSecretKey;

    private SecretKey secretKey; // Usar tipo SecretKey do javax.crypto

    @PostConstruct
    public void init() {
        // Validar se a chave foi configurada
        if (configuredSecretKey == null || configuredSecretKey.isBlank() || "${security.jwt.token.secret-key}".equals(configuredSecretKey)) {
            log.error("FATAL: JWT secret key ('security.jwt.token.secret-key') is not configured properly or is still the placeholder value.");
            throw new IllegalStateException("JWT secret key must be configured with a real value.");
        }
        // Avisar se a chave padrão/teste está em uso
        if (configuredSecretKey.startsWith("DefaultWeakSecretKey") || configuredSecretKey.startsWith("TestSecretKey") || configuredSecretKey.startsWith("MySecureTestKey") || configuredSecretKey.equals("some-long-test-secret-key-cashflow")) {
            log.warn("--------------------------------------------------------------------------------");
            log.warn("WARNING: Using default/test JWT secret key. THIS IS INSECURE FOR PRODUCTION!");
            log.warn("Ensure 'JWT_SECRET_KEY' environment variable or a secure application property is set.");
            log.warn("--------------------------------------------------------------------------------");
        }
        // Validação básica do tamanho da chave (ex: para HS256, >= 256 bits / 32 bytes)
        if (configuredSecretKey.getBytes(StandardCharsets.UTF_8).length < 32) {
            log.warn("Configured JWT secret key is shorter than 32 bytes. This might be insecure for algorithms like HS256.");
        }

        try {
            // Criar a SecretKey a partir da string configurada
            this.secretKey = Keys.hmacShaKeyFor(configuredSecretKey.getBytes(StandardCharsets.UTF_8));
            log.info("JWT Secret Key initialized successfully for Company Service.");
        } catch (Exception e) {
            log.error("Error initializing JWT Secret Key from configured value: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize JWT Secret Key", e);
        }
    }

    /**
     * Extracts the username (subject) from the token.
     */
    public String extractUsername(String token) throws JwtException { // Lança exceção base JJWT
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the token.
     */
    public Date extractExpiration(String token) throws JwtException { // Lança exceção base JJWT
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtException { // Lança exceção base JJWT
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the token and extracts all claims, handling specific JJWT exceptions.
     */
    private Claims extractAllClaims(String token) throws JwtException { // Lança exceção base JJWT
        try {
            // Usa o parser builder com a chave secreta
            return Jwts.parserBuilder()
                    .setSigningKey(this.secretKey)
                    .build()
                    .parseClaimsJws(token) // Verifica assinatura e expiração
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            throw e; // Re-lança para ser tratada especificamente se necessário
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) { // <<< Usar io.jsonwebtoken.security.SignatureException
            log.error("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) { // Ex: token nulo ou vazio
            log.error("JWT token argument invalid: {}", e.getMessage());
            throw e;
        } catch (Exception e) { // Captura genérica para outros erros inesperados
            log.error("Unexpected error parsing JWT: {}", e.getMessage(), e);
            throw new JwtException("Unexpected error parsing JWT", e); // Envolve em JwtException
        }
    }

    /**
     * Checks if the token is expired.
     */
    private Boolean isTokenExpired(String token) throws JwtException { // Lança exceção base JJWT
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Se a exceção específica de expiração for lançada, está expirado
        }
        // Outras exceções (Malformed, Signature, etc.) serão propagadas pelo extractExpiration
    }

    /**
     * Validates the token against UserDetails (username match and not expired).
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        if (token == null || userDetails == null) {
            log.warn("Attempting to validate with null token or userDetails.");
            return false;
        }
        try {
            final String username = extractUsername(token);
            // Verifica se o username do token bate com o UserDetails E se o token não está expirado
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            // O log do erro específico já ocorreu em extractUsername ou isTokenExpired
            log.debug("Token validation failed due to JwtException: {}", e.getMessage());
            return false; // Qualquer exceção do JJWT invalida o token
        } catch (Exception e) {
            log.error("Unexpected error during token validation for user '{}': {}", userDetails.getUsername(), e.getMessage(), e);
            return false;
        }
    }

    // Métodos de geração de token não são necessários neste serviço.
    // Manter comentados.
    /*
    public String generateToken(UserDetails userDetails) { ... }
    private String createToken(Map<String, Object> claims, String subject) { ... }
    */
}