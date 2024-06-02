package org.petmarket.users.repository;

import org.petmarket.users.entity.Complaint;
import org.petmarket.users.entity.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Page<Complaint> findAllByComplainedUserIdAndStatus(Long userId, ComplaintStatus complaintStatus, Pageable pageable);

    Page<Complaint> findAllByStatus(ComplaintStatus complaintStatus, Pageable pageable);
}
