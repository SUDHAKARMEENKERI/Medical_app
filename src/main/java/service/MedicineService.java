package service;

import modal.MedicineRequest;
import modal.MedicineResponse;

import java.util.List;

public interface MedicineService {

    MedicineResponse addMedicine(MedicineRequest request);

    List<MedicineResponse> getAllMedicines(Long storeId, String storeMobile, String email);
}
