package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
