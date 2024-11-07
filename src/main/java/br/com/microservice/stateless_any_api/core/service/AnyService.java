package br.com.microservice.stateless_any_api.core.service;

import br.com.microservice.stateless_any_api.domain.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {


  private final JwtService jwtService;

  public AnyResponse getData(String accessToken) {
    jwtService.validateAccessToken(accessToken);

    var authUser = jwtService.getAuthenticatedUser(accessToken);
    return new AnyResponse(HttpStatus.OK.name(), HttpStatus.OK.value(), authUser);
  }
}
