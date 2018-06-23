package net.pkhapps.vepari.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link AlertReceipt}s.
 */
public interface AlertReceiptRepository extends JpaRepository<AlertReceipt, Long> {
}
