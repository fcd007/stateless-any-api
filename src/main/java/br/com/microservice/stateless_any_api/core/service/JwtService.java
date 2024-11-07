package br.com.microservice.stateless_any_api.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import br.com.microservice.stateless_any_api.domain.dto.AuthUserResponse;
import br.com.microservice.stateless_any_api.infra.exception.AuthenticationException;
import br.com.microservice.stateless_any_api.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final String EMPTY_STRING_SPACE = " ";
  private static final Integer TOKEN_INDEX = 1;

  @Value("${app.token.secret-key}")
  private String secretKey;

  public AuthUserResponse getAuthenticatedUser(String token) {
    var tokenClaims = getClaims(token);
    var userId = Integer.valueOf(tokenClaims.get("id", String.class));
    var username = tokenClaims.get("username", String.class);

    return new AuthUserResponse(userId, username);
  }

  public void validateAccessToken(String token) {
    getClaims(token);
  }

  private Claims getClaims(String token) {
    var accessToken = extractToken(token);
    try {
      return Jwts
          .parser()
          .verifyWith(generateSign())
          .build()
          .parseSignedClaims(accessToken)
          .getPayload();
    } catch (Exception exception) {
      throw new AuthenticationException("Invalid access token" + exception.getMessage());
    }
  }

  private SecretKey generateSign() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  private String extractToken(String token) {
    if (isEmpty(token)) {
      throw new ValidationException("The access Token is required");
    }

    if (!token.contains(EMPTY_STRING_SPACE)) {
      String[] tokenParts = token.split(EMPTY_STRING_SPACE);

      if (tokenParts.length > 1) {
        return tokenParts[TOKEN_INDEX];
      }
    }

    return token;
  }

}
