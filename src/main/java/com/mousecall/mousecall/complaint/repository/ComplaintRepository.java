package com.mousecall.mousecall.complaint.repository;

import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUser(User user);
}
