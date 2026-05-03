package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.InvalidAcquirerRequestException;
import com.checkout.payment.gateway.exception.InvalidPaymentIdException;
import com.checkout.payment.gateway.executor.AcquirerClient;
import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.AcquirerResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import com.checkout.payment.gateway.transformer.AcquirerTransformer;
import com.checkout.payment.gateway.transformer.PaymentApiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentApiTransformer paymentApiTransformer;
  private final AcquirerTransformer acquirerTransformer;
  private final AcquirerClient acquirerClient;
  private final PaymentsRepository paymentsRepository;

  public PaymentGatewayService(PaymentApiTransformer paymentApiTransformer,
      AcquirerTransformer acquirerTransformer,
      AcquirerClient acquirerClient, PaymentsRepository paymentsRepository) {
    this.paymentApiTransformer = paymentApiTransformer;
    this.acquirerTransformer = acquirerTransformer;
    this.acquirerClient = acquirerClient;
    this.paymentsRepository = paymentsRepository;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new InvalidPaymentIdException("No payment found against ID: " + id));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    PostPaymentRequest normalizedRequest = paymentApiTransformer.normalize(paymentRequest);
    AcquirerRequest acquirerRequest = acquirerTransformer.toAcquirerRequest(normalizedRequest);
    AcquirerResponse acquirerResponse = invokeAcquirer(acquirerRequest);
    PostPaymentResponse response = paymentApiTransformer.toClientJson(normalizedRequest, toPaymentStatus(acquirerResponse));
    paymentsRepository.add(response);
    return response;
  }

  private AcquirerResponse invokeAcquirer(AcquirerRequest acquirerRequest) {
    try {
      return acquirerClient.execute(acquirerRequest);
    } catch (InvalidAcquirerRequestException e) {
      return declinedAcquirerResponse();
    }
  }

  public PostPaymentResponse processRejectedPayment(PostPaymentRequest paymentRequest) {
    PostPaymentResponse response = paymentApiTransformer.toRejectedPaymentResponse(paymentRequest);
    paymentsRepository.add(response);
    return response;
  }

  public AcquirerResponse declinedAcquirerResponse() {
    return new AcquirerResponse(false);
  }

  private PaymentStatus toPaymentStatus(AcquirerResponse acquirerResponse) {
    return acquirerResponse.authorized() ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED;
  }
}
