package org.petmarket.message.repository;

import org.petmarket.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByAuthorIdOrRecipientId(Long authorId, Long recipientId, Pageable pageable);

    Page<Message> findByAuthorId(Long authorId, Pageable pageable);

    Page<Message> findByRecipientId(Long recipientId, Pageable pageable);
}
