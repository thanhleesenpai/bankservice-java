package notification_service.notification_service.repository;

import notification_service.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToAccountIdOrderByCreatedAtDesc(String toAccountId);
    List<Notification> findByFromAccountIdOrderByCreatedAtDesc(String fromAccountId);
}
