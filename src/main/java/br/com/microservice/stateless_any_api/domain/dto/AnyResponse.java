package br.com.microservice.stateless_any_api.domain.dto;

public record AnyResponse(String status, Integer code, AuthUserResponse authUser) {
}
