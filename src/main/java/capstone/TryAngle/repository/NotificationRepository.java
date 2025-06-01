package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Notification;
import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByReceiver(User receiver);
}
