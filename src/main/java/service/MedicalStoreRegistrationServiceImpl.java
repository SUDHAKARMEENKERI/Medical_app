package service;

import dao.MedicalStoreRegistrationDao;
import jakarta.transaction.Transactional;
import modal.MedicalStoreLoginRequest;
import modal.MedicalStoreLoginResponse;
import modal.MedicalStoreRegistrationRequest;
import modal.MedicalStoreRegistrationResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.security.JwtService;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MedicalStoreRegistrationServiceImpl implements MedicalStoreRegistrationService {
    private final MedicalStoreRegistrationDao medicalStoreRegistrationDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public MedicalStoreRegistrationServiceImpl(
            MedicalStoreRegistrationDao medicalStoreRegistrationDao,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.medicalStoreRegistrationDao = medicalStoreRegistrationDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public MedicalStoreRegistrationResponse resetPassword(modal.PasswordResetRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        Optional<modal.MedicalStoreRegistrationRequest> storeOpt = medicalStoreRegistrationDao.findByEmailAndStoreMobile(request.getEmail(), request.getStoreMobile());
        if (storeOpt.isEmpty()) {
            throw new IllegalArgumentException("Store not found with provided email and storeMobile");
        }
        modal.MedicalStoreRegistrationRequest store = storeOpt.get();
        store.setPassword(passwordEncoder.encode(request.getPassword()));
        medicalStoreRegistrationDao.save(store);
        return toResponse(store);
    }

    @Override
    @Transactional
    public MedicalStoreRegistrationResponse registerMedicalStore(MedicalStoreRegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (!Boolean.TRUE.equals(request.getAgreeTerms())) {
            throw new IllegalArgumentException("You must agree to the terms");
        }
        if (medicalStoreRegistrationDao.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered");
        }
        if (medicalStoreRegistrationDao.existsByStoreMobile(request.getStoreMobile())) {
            throw new IllegalStateException("storeMobile already registered");
        }
        if (medicalStoreRegistrationDao.existsByLicenseNo(request.getLicenseNo())) {
            throw new IllegalStateException("License number already registered");
        }

        request.setId(null);
        request.setBilled(false);
    request.setPassword(passwordEncoder.encode(request.getPassword()));
        MedicalStoreRegistrationRequest savedStore = medicalStoreRegistrationDao.save(request);

        return toResponse(savedStore);
    }

    @Override
    public List<MedicalStoreRegistrationResponse> getAllMedicalStores() {
        return medicalStoreRegistrationDao.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public MedicalStoreRegistrationResponse getMedicalStoreDetails(String email, String mobile) {
        String normalizedEmail = nonNull(email).trim();
        String normalizedMobile = nonNull(mobile).trim();

        if (normalizedEmail.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (normalizedMobile.isBlank()) {
            throw new IllegalArgumentException("Mobile is required");
        }

        MedicalStoreRegistrationRequest medicalStore = medicalStoreRegistrationDao
                .findByEmailAndAnyMobile(normalizedEmail, normalizedMobile)
                .orElseThrow(() -> new NoSuchElementException("Medical store not found"));

        return toResponse(medicalStore);
    }

    @Override
    @Transactional
    public MedicalStoreLoginResponse loginMedicalStore(MedicalStoreLoginRequest request) {
        String loginAs = request.getLoginAs().trim().toLowerCase(Locale.ROOT);
        String identifier = request.getStoreMobileOrStoreId().trim();
        if (!"owner".equals(loginAs) && !"staff".equals(loginAs)) {
            throw new IllegalArgumentException("loginAs must be either owner or staff");
        }

        Optional<MedicalStoreRegistrationRequest> storeByStoreMobile = medicalStoreRegistrationDao
            .findByStoreMobile(identifier);

        Optional<MedicalStoreRegistrationRequest> storeByMobile = Optional.empty();
        if (storeByStoreMobile.isEmpty()) {
            storeByMobile = medicalStoreRegistrationDao.findByMobile(identifier);
        }

        Optional<MedicalStoreRegistrationRequest> store = storeByStoreMobile.isPresent() ? storeByStoreMobile : storeByMobile;
        if (store.isEmpty()) {
            try {
                Long storeId = Long.parseLong(identifier);
                store = medicalStoreRegistrationDao.findById(storeId);
            } catch (NumberFormatException ignored) {
                store = Optional.empty();
            }
        }

        if (store.isEmpty() || !passwordMatches(request.getPassword(), store.get())) {
            throw new IllegalArgumentException("Invalid storeMobile/storeId or password");
        }

        MedicalStoreRegistrationRequest medicalStore = store.get();
        if (!nonNull(medicalStore.getRole()).isBlank()
                && !nonNull(medicalStore.getRole()).equalsIgnoreCase(loginAs)) {
            throw new IllegalArgumentException("Invalid role for this account");
        }
        if (!Boolean.TRUE.equals(medicalStore.getBilled())) {
            throw new IllegalArgumentException("Please complete billing first to access login");
        }

        MedicalStoreLoginResponse response = new MedicalStoreLoginResponse();
        String token = jwtService.generateToken(medicalStore, loginAs);
        response.setToken(token);
        response.setTokenType("Bearer");
        return response;
    }

    private MedicalStoreRegistrationResponse toResponse(MedicalStoreRegistrationRequest request) {
        MedicalStoreRegistrationResponse response = new MedicalStoreRegistrationResponse();
        response.setId(request.getId());
        response.setOwnerName(request.getOwnerName());
        response.setStoreName(request.getStoreName());
        response.setStoreMobile(nonNull(request.getStoreMobile()));
        response.setMobile(nonNull(request.getMobile()));
        response.setEmail(nonNull(request.getEmail()));
        response.setLicenseNo(nonNull(request.getLicenseNo()));
        response.setRole(nonNull(request.getRole()));
        response.setGstinNumber(nonNull(request.getGstinNumber()));
        response.setPharmacyCode(nonNull(request.getPharmacyCode()));
        response.setAddress(nonNull(request.getAddress()));
        response.setBilled(Boolean.TRUE.equals(request.getBilled()));
        return response;
    }

    private String nonNull(String value) {
        return value == null ? "" : value;
    }

    private boolean passwordMatches(String rawPassword, MedicalStoreRegistrationRequest store) {
        String storedPassword = store.getPassword();
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (passwordEncoder.matches(rawPassword, storedPassword)) {
            return true;
        }

        if (rawPassword.equals(storedPassword)) {
            store.setPassword(passwordEncoder.encode(rawPassword));
            medicalStoreRegistrationDao.save(store);
            return true;
        }

        return false;
    }

       @Override
    @Transactional
    public MedicalStoreRegistrationResponse patchMedicalStore(Long storeId, modal.MedicalStorePatchRequest request) {
        MedicalStoreRegistrationRequest existingStore = medicalStoreRegistrationDao.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Medical store not found"));

        boolean anyFieldUpdated = false;

        if (request.getOwnerName() != null) {
            existingStore.setOwnerName(request.getOwnerName());
            anyFieldUpdated = true;
        }
        if (request.getStoreName() != null) {
            existingStore.setStoreName(request.getStoreName());
            anyFieldUpdated = true;
        }
        if (request.getStoreMobile() != null) {
            existingStore.setStoreMobile(request.getStoreMobile());
            anyFieldUpdated = true;
        }
        if (request.getMobile() != null) {
            existingStore.setMobile(request.getMobile());
            anyFieldUpdated = true;
        }
        if (request.getEmail() != null) {
            existingStore.setEmail(request.getEmail());
            anyFieldUpdated = true;
        }
        if (request.getLicenseNo() != null) {
            existingStore.setLicenseNo(request.getLicenseNo());
            anyFieldUpdated = true;
        }
        if (request.getGstinNumber() != null) {
            existingStore.setGstinNumber(request.getGstinNumber());
            anyFieldUpdated = true;
        }
        if (request.getPharmacyCode() != null) {
            existingStore.setPharmacyCode(request.getPharmacyCode());
            anyFieldUpdated = true;
        }
        if (request.getAddress() != null) {
            existingStore.setAddress(request.getAddress());
            anyFieldUpdated = true;
        }
        if (request.getPassword() != null) {
            existingStore.setPassword(passwordEncoder.encode(request.getPassword()));
            anyFieldUpdated = true;
        }

        if (!anyFieldUpdated) {
            throw new IllegalArgumentException("At least one field must be provided");
        }

        MedicalStoreRegistrationRequest updatedStore = medicalStoreRegistrationDao.save(existingStore);
        return toResponse(updatedStore);
    }
}