package bank_service.bank_service.service;

import bank_service.bank_service.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    private final RestTemplate restTemplate;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public void createPayment(PaymentRequest request) {
        restTemplate.postForObject(paymentServiceUrl + "/payments", request, String.class);
    }
}
