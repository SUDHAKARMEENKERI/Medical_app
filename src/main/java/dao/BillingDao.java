package dao;

import modal.BillingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingDao extends JpaRepository<BillingResponse, Long> {
    // Custom query methods if neede
}
