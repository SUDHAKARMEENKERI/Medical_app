package service;

import modal.MedicalStoreLoginRequest;
import modal.MedicalStoreLoginResponse;
import modal.MedicalStoreRegistrationRequest;
import modal.MedicalStoreRegistrationResponse;

import java.util.List;

public interface MedicalStoreRegistrationService {

    MedicalStoreRegistrationResponse registerMedicalStore(MedicalStoreRegistrationRequest request);

    List<MedicalStoreRegistrationResponse> getAllMedicalStores();

    MedicalStoreLoginResponse loginMedicalStore(MedicalStoreLoginRequest request);
}