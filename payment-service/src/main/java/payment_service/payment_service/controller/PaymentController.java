package payment_service.payment_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment_service.payment_service.dto.PaymentRequest;
import payment_service.payment_service.service.PaymentService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        paymentService.sendPayment(request);
        return ResponseEntity.ok("Payment request sent for id: " + request.getPaymentId());
    }
}
