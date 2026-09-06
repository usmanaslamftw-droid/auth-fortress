package com.usmanaslam.authfortress.repository;

import com.usmanaslam.authfortress.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);
    List<AuditLog> findAllByOrderByTimestampDesc();
}
