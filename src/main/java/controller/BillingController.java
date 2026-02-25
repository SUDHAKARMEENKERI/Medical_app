package controller;

import modal.BillingRequest;
import modal.BillingResponse;
import service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {
    @Autowired
    private BillingService billingService;

    @PostMapping
    public BillingResponse createBill(@RequestBody BillingRequest request) {
        return billingService.createBill(request);
    }

    @GetMapping
    public List<BillingResponse> getBills(
            @RequestParam(required = true) Long storeId,
            @RequestParam(required = true) String storeMobile,
            @RequestParam(required = true) String email) {
        return billingService.getBills(storeId, storeMobile, email);
    }
}
