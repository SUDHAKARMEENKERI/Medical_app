    package controller;

    import jakarta.validation.Valid;
    import modal.MedicineRequest;
    import modal.MedicineResponse;
    import modal.BillingRequest;
    import modal.BillingResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.CrossOrigin;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import service.MedicineService;
    import service.BillingService;

    import java.util.List;

    @RestController
    @RequestMapping("/api/medicine")
    @CrossOrigin(origins = "*")
    public class MedicineController {

        private final MedicineService medicineService;

        @Autowired
        private BillingService billingService;

        public MedicineController(MedicineService medicineService) {
            this.medicineService = medicineService;
        }

        @PostMapping("/add")
        public ResponseEntity<MedicineResponse> addMedicine(@Valid @RequestBody MedicineRequest request) {
            MedicineResponse response = medicineService.addMedicine(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/all")
        public ResponseEntity<List<MedicineResponse>> getAllMedicines(
                @RequestParam(required = true) Long storeId,
                @RequestParam(required = true) String storeMobile,
                @RequestParam(required = true) String email) {
            List<MedicineResponse> response = medicineService.getAllMedicines(storeId, storeMobile, email);
            return ResponseEntity.ok(response);
        }

        @PostMapping("/billing")
        public ResponseEntity<BillingResponse> createBill(@RequestBody BillingRequest request) {
            BillingResponse response = billingService.createBill(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/billing")
        public ResponseEntity<List<BillingResponse>> getBills(
                @RequestParam(required = false) Long storeId,
                @RequestParam(required = false) String storeMobile,
                @RequestParam(required = false) String email) {
            List<BillingResponse> response = billingService.getBills(storeId, storeMobile, email);
            return ResponseEntity.ok(response);
        }
    }
