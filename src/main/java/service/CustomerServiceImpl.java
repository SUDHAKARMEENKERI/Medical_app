package service;

import dao.CustomerDao;
import modal.CustomerPatchRequest;
import modal.CustomerRequest;
import modal.CustomerResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public CustomerResponse addCustomer(CustomerRequest request) {
        request.setId(null);
        if (isBlank(request.getStoreMobile())) {
            request.setStoreMobile(request.getPhone());
        }
        CustomerRequest savedCustomer = customerDao.save(request);

        return toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse patchCustomer(Long customerId, CustomerPatchRequest request) {
        CustomerRequest existingCustomer = customerDao.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        boolean anyFieldUpdated = false;

        if (request.getName() != null) {
            existingCustomer.setName(request.getName());
            anyFieldUpdated = true;
        }
        if (request.getStoreId() != null) {
            existingCustomer.setStoreId(request.getStoreId());
            anyFieldUpdated = true;
        }
        if (request.getPhone() != null) {
            existingCustomer.setPhone(request.getPhone());
            anyFieldUpdated = true;
        }
        if (request.getStoreMobile() != null) {
            existingCustomer.setStoreMobile(request.getStoreMobile());
            anyFieldUpdated = true;
        }
        if (request.getEmail() != null) {
            existingCustomer.setEmail(request.getEmail());
            anyFieldUpdated = true;
        }
        if (request.getGender() != null) {
            existingCustomer.setGender(request.getGender());
            anyFieldUpdated = true;
        }
        if (request.getAge() != null) {
            existingCustomer.setAge(request.getAge());
            anyFieldUpdated = true;
        }
        if (request.getDoctorName() != null) {
            existingCustomer.setDoctorName(request.getDoctorName());
            anyFieldUpdated = true;
        }
        if (request.getReferredBy() != null) {
            existingCustomer.setReferredBy(request.getReferredBy());
            anyFieldUpdated = true;
        }
        if (request.getSpent() != null) {
            if (request.getSpent().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("spent cannot be negative");
            }
            existingCustomer.setSpent(request.getSpent());
            anyFieldUpdated = true;
        }
        if (request.getVisited() != null) {
            existingCustomer.setVisited(request.getVisited());
            anyFieldUpdated = true;
        }

        if (!anyFieldUpdated) {
            throw new IllegalArgumentException("At least one field must be provided");
        }

        CustomerRequest updatedCustomer = customerDao.save(existingCustomer);
        return toResponse(updatedCustomer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers(Long storeId, String storeMobile, String email) {
        return customerDao.findByFilters(storeId, normalize(storeMobile), normalize(email)).stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(CustomerRequest savedCustomer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(savedCustomer.getId());
        response.setName(savedCustomer.getName());
        response.setStoreId(savedCustomer.getStoreId());
        response.setPhone(savedCustomer.getPhone());
        response.setStoreMobile(nonNull(savedCustomer.getStoreMobile()));
        response.setEmail(nonNull(savedCustomer.getEmail()));
        response.setGender(savedCustomer.getGender());
        response.setAge(savedCustomer.getAge());
        response.setDoctorName(savedCustomer.getDoctorName());
        response.setReferredBy(savedCustomer.getReferredBy());
        response.setSpent(savedCustomer.getSpent());
        response.setVisited(savedCustomer.getVisited());
        return response;
    }

    private String nonNull(String value) {
        return value == null ? "" : value;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}