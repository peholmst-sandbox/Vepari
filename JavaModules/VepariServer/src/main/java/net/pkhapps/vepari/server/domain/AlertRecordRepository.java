package net.pkhapps.vepari.server.domain;

import net.pkhapps.vepari.server.security.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Repository of {@link AlertRecord}s.
 */
public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long>,
        JpaSpecificationExecutor<AlertRecord> {

    @NonNull
    static Specification<AlertRecord> newerThan(@NonNull Instant oldestAlertDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(AlertRecord_.alertDate), oldestAlertDate);
    }

    @NonNull
    static Specification<AlertRecord> seenBy(@NonNull User user) {
        return (root, query, cb) -> {
            var receipt = query.from(AlertReceipt.class);
            return cb.exists(query.subquery(AlertRecord.class)
                    .select(receipt.get(AlertReceipt_.alertRecord))
                    .where(cb.equal(receipt.get(AlertReceipt_.user), user)));
        };
    }
}
