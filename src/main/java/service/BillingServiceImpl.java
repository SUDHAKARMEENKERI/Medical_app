package service;

import dao.BillingDao;
import modal.BillingRequest;
import modal.BillingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {
    @Autowired
    private BillingDao billingDao;

    @Override
    public BillingResponse createBill(BillingRequest request) {
        BillingResponse response = new BillingResponse();
        response.setCustomerName(request.getCustomerName());
        response.setCustomerPhone(request.getCustomerPhone());
        response.setPatientGender(request.getPatientGender());
        response.setPatientAge(request.getPatientAge());
        response.setDoctorName(request.getDoctorName());
        response.setReferredBy(request.getReferredBy());
        response.setAmount(request.getAmount());
        response.setItemCount(request.getItemCount());
        response.setDate(request.getDate());
        response.setStoreId(request.getStoreId());
        response.setStoreMobile(request.getStoreMobile());
        response.setEmail(request.getEmail());
        response.setLineItems(request.getLineItems());
        BillingResponse saved = billingDao.save(response);
        String invoiceNum = "INV-000" + saved.getBillId();
        saved.setInvoiceNumber(invoiceNum);
        return billingDao.save(saved);
    }

    @Override
    public List<BillingResponse> getBills(Long storeId, String storeMobile, String email) {
        List<BillingResponse> allBills = billingDao.findAll();
        return allBills.stream()
            .filter(bill -> (storeId == null || bill.getStoreId() != null && bill.getStoreId().equals(storeId))
                && (storeMobile == null || bill.getStoreMobile() != null && bill.getStoreMobile().equals(storeMobile))
                && (email == null || bill.getEmail() != null && bill.getEmail().equalsIgnoreCase(email)))
            .toList();
    }
}
