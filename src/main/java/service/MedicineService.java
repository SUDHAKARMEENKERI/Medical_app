package service;

import modal.MedicineRequest;
import modal.MedicinePatchRequest;
import modal.MedicineResponse;

import java.util.List;

public interface MedicineService {

    MedicineResponse addMedicine(MedicineRequest request);

    MedicineResponse patchMedicine(Long medicineId, MedicinePatchRequest request);

    void deleteMedicine(Long medicineId);

    List<MedicineResponse> getAllMedicines(Long storeId, String storeMobile, String email);

    List<MedicineResponse> bulkUploadMedicines(List<MedicineRequest> requests);

    int bulkUploadExcel(org.springframework.web.multipart.MultipartFile excelFile);
}
