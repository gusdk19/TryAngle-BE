package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    @Query("SELECT u.email FROM User u WHERE u.name = :name AND u.phone = :phone")
    Optional<String> findByNameAndPhone(String name, String phone);
}
