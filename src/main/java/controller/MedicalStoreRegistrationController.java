
package controller;

import jakarta.validation.Valid;
import modal.CustomerPatchRequest;
import modal.CustomerRequest;
import modal.CustomerResponse;
import modal.MedicalStoreLoginRequest;
import modal.MedicalStoreLoginResponse;
import modal.MedicalStoreRegistrationRequest;
import modal.MedicalStoreRegistrationResponse;
import service.CustomerService;
import service.MedicalStoreRegistrationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-store")
@CrossOrigin(origins = "*")
public class MedicalStoreRegistrationController {

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody modal.PasswordResetRequest request) {
        try {
            MedicalStoreRegistrationResponse response = medicalStoreRegistrationService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    private final MedicalStoreRegistrationService medicalStoreRegistrationService;
    private final CustomerService customerService;

    public MedicalStoreRegistrationController(
            MedicalStoreRegistrationService medicalStoreRegistrationService,
            CustomerService customerService) {
        this.medicalStoreRegistrationService = medicalStoreRegistrationService;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMedicalStore(
            @Valid @RequestBody MedicalStoreRegistrationRequest request) {
        try {
            MedicalStoreRegistrationResponse response = medicalStoreRegistrationService.registerMedicalStore(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicalStoreRegistrationResponse>> getAllMedicalStores() {
        List<MedicalStoreRegistrationResponse> medicalStores = medicalStoreRegistrationService.getAllMedicalStores();
        return ResponseEntity.ok(medicalStores);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMedicalStore(@Valid @RequestBody MedicalStoreLoginRequest request) {
        try {
            MedicalStoreLoginResponse response = medicalStoreRegistrationService.loginMedicalStore(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }
    }

    @PostMapping("/customer/add")
    public ResponseEntity<CustomerResponse> addCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.addCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/customer/{customerId}")
    public ResponseEntity<?> patchCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerPatchRequest request) {
        try {
            CustomerResponse response = customerService.patchCustomer(customerId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/customer/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(
            @RequestParam(required = true) Long storeId,
            @RequestParam(required = true) String storeMobile,
            @RequestParam(required = true) String email) {
        List<CustomerResponse> response = customerService.getAllCustomers(storeId, storeMobile, email);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/patch/{storeId}")
    public ResponseEntity<?> patchMedicalStore(
            @PathVariable Long storeId,
            @RequestBody modal.MedicalStorePatchRequest request) {
        try {
            MedicalStoreRegistrationResponse response = medicalStoreRegistrationService.patchMedicalStore(storeId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}