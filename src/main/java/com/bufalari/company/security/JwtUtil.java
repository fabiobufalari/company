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
// import org.springframework.security.oauth2.jwt.JwtException; // <<< REMOVER ESTE IMPORT
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
// import java.security.SignatureException; // <<< REMOVER ESTE IMPORT
import java.util.Date;
import java.util.function.Function;

/**
 * Utility for handling JWT tokens, using a configured secret key.
 * Utilitário para manipulação de tokens JWT, usando uma chave secreta configurada.
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${security.jwt.token.secret-key}")
    private String configuredSecretKey;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (configuredSecretKey == null || configuredSecretKey.isBlank()) {
            log.error("JWT secret key is not configured properly in application properties (security.jwt.token.secret-key)."); // Mensagem corrigida
            throw new IllegalStateException("JWT secret key must be configured.");
        }
        try {
            this.secretKey = Keys.hmacShaKeyFor(configuredSecretKey.getBytes(StandardCharsets.UTF_8));
            log.info("JWT Secret Key initialized successfully for Company Service."); // Mensagem específica
        } catch (Exception e) {
            log.error("Error initializing JWT Secret Key from configured value.", e);
            throw new RuntimeException("Failed to initialize JWT Secret Key", e);
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // --- MÉTODO CORRIGIDO ---
    /**
     * Parses the token and extracts all claims. Handles potential exceptions.
     * Faz o parse do token e extrai todas as claims. Trata exceções potenciais.
     * @param token The JWT token. / O token JWT.
     * @return The Claims object. / O objeto Claims.
     * @throws io.jsonwebtoken.JwtException if the token is invalid or cannot be parsed. / Se o token for inválido ou não puder ser parseado. // <<< TIPO CORRIGIDO
     */
    private Claims extractAllClaims(String token) throws io.jsonwebtoken.JwtException { // <<< TIPO CORRIGIDO
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.warn("JWT token is malformed: {}", e.getMessage());
            throw e;
        } catch (io.jsonwebtoken.security.SignatureException e) { // <<< TIPO CORRIGIDO NO CATCH
            log.warn("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("JWT token argument validation failed: {}", e.getMessage());
            throw e;
        }
        // Qualquer outra JwtException será lançada implicitamente
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (io.jsonwebtoken.JwtException e) { // <<< Usar tipo base correto
            log.warn("Could not determine expiration due to other JWT exception: {}", e.getMessage());
            return true;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            // Adicionado null check para userDetails
            if (userDetails == null) {
                log.warn("UserDetails object provided for validation is null for token subject: {}", username);
                return false;
            }
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (io.jsonwebtoken.JwtException e) { // <<< Usar tipo base correto
            // Logged in extractUsername or isTokenExpired
            return false;
        }
    }

    // Métodos de geração de token podem permanecer comentados
}