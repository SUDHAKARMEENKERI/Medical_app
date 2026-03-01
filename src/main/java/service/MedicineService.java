package service;

import modal.MedicineRequest;
import modal.MedicineResponse;

import java.util.List;

public interface MedicineService {

    MedicineResponse addMedicine(MedicineRequest request);

    List<MedicineResponse> getAllMedicines(Long storeId, String storeMobile, String email);

    List<MedicineResponse> bulkUploadMedicines(List<MedicineRequest> requests);

    int bulkUploadExcel(org.springframework.web.multipart.MultipartFile excelFile);
}
