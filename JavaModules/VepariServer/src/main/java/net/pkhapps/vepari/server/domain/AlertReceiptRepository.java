package net.pkhapps.vepari.server.domain;

import net.pkhapps.vepari.server.security.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

/**
 * Repository of {@link AlertReceipt}s.
 */
public interface AlertReceiptRepository extends JpaRepository<AlertReceipt, Long>,
        JpaSpecificationExecutor<AlertReceipt> {

    @NonNull
    static Specification<AlertReceipt> byUser(@NonNull User user) {
        return (root, query, cb) -> cb.equal(root.get(AlertReceipt_.user), user);
    }

    @NonNull
    static Specification<AlertReceipt> byAlertRecord(@NonNull AlertRecord alertRecord) {
        return (root, query, cb) -> cb.equal(root.get(AlertReceipt_.alertRecord), alertRecord);
    }
}
