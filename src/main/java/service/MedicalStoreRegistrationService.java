package service;

import modal.MedicalStoreLoginRequest;
import modal.MedicalStoreLoginResponse;
import modal.MedicalStoreRegistrationRequest;
import modal.MedicalStoreRegistrationResponse;

import java.util.List;

public interface MedicalStoreRegistrationService {

    MedicalStoreRegistrationResponse registerMedicalStore(MedicalStoreRegistrationRequest request);

    List<MedicalStoreRegistrationResponse> getAllMedicalStores();

    MedicalStoreRegistrationResponse getMedicalStoreDetails(String email, String mobile);

    MedicalStoreLoginResponse loginMedicalStore(MedicalStoreLoginRequest request);

    MedicalStoreRegistrationResponse patchMedicalStore(Long storeId, modal.MedicalStorePatchRequest request);

    // Password reset API
    MedicalStoreRegistrationResponse resetPassword(modal.PasswordResetRequest request);
}