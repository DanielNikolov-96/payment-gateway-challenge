package com.checkout.payment.gateway.executor;

import com.checkout.payment.gateway.exception.ExternalFailureException;
import com.checkout.payment.gateway.exception.InvalidAcquirerRequestException;
import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.AcquirerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class AcquirerClient {

  private static final Logger LOG = LoggerFactory.getLogger(AcquirerClient.class);

  private final RestTemplate restTemplate;
  private final String baseUrl;
  private final String port;
  private final String endpoint;

  public AcquirerClient(RestTemplate restTemplate,
      @Value("${acquirerService.baseUrl}") String baseUrl,
      @Value("${acquirerService.port}") String port,
      @Value("${acquirerService.endpoint}") String endpoint) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
    this.port = port;
    this.endpoint = endpoint;
  }

  public AcquirerResponse execute(AcquirerRequest request) {
    try {
      LOG.debug("Executing payment for request: {}", request);
      return callAcquirer(request);
    } catch (HttpClientErrorException e) {
      LOG.error("Client error occurred while processing payment for request: {} with status code: {} and response body: {}", request, e.getStatusCode(), e.getResponseBodyAsString());
      throw new InvalidAcquirerRequestException(String.format("Invalid request sent to acquirer for request: %s with error: %s", request, e.getMessage()));
    } catch (HttpServerErrorException | ResourceAccessException e) {
      String errorMessage = String.format("Acquirer error occurred while processing payment for request: %s with error: %s", request, e.getMessage());
      throw new ExternalFailureException(errorMessage);
    }
  }

  private AcquirerResponse callAcquirer(AcquirerRequest request) {
    ResponseEntity<AcquirerResponse> response =
        restTemplate.postForEntity(getUrl(baseUrl, port, endpoint),
            request,
            AcquirerResponse.class);
    if (response.getBody() == null) {
      throw new ExternalFailureException(String.format("Received invalid response from acquirer for request: %s", request));
    }

    LOG.info("Payment completed for request: {} with response code: {} and body: {}", request, response.getStatusCode(), response.getBody());
    return response.getBody();
  }

  private String getUrl(String baseUrl, String port, String endpoint) {
    return String.format("%s:%s/%s", baseUrl, port, endpoint);
  }
}