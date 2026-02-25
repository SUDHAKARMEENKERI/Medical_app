package service;

import modal.CustomerPatchRequest;
import modal.CustomerRequest;
import modal.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse addCustomer(CustomerRequest request);

    CustomerResponse patchCustomer(Long customerId, CustomerPatchRequest request);

    List<CustomerResponse> getAllCustomers(Long storeId, String storeMobile, String email);
}