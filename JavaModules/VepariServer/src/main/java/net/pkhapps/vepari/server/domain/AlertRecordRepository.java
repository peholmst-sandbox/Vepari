package net.pkhapps.vepari.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link AlertRecord}s.
 */
public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long> {
}
