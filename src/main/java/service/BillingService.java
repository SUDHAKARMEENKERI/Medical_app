package service;

import modal.BillingRequest;
import modal.BillingResponse;

import java.util.List;

public interface BillingService {
    BillingResponse createBill(BillingRequest request);
    List<BillingResponse> getBills(Long storeId, String storeMobile, String email);
}
