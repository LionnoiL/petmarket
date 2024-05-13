package org.petmarket.message.repository;

import org.petmarket.message.dto.UserChatsResponseDto;
import org.petmarket.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = """
            SELECT m
            FROM Message m
            WHERE (m.author.id = :chatUserId AND m.recipient.id = :userId)
            OR (m.author.id = :userId AND m.recipient.id = :chatUserId)
            ORDER BY m.created DESC
            """)
    Page<Message> findAllByUserAndChatUserId(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId,
                                             Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE message m
            SET m.message_status = 'READ'
            WHERE m.author_id = :chatUserId AND m.recipient_id = :userId AND m.message_status = 'UNREAD'
            """, nativeQuery = true)
    void markChatMessagesAsRead(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);

    @Query(value = """
            SELECT m.text, m.created, m.message_status AS messageStatus, u.id AS chatUserId, u.email AS chatUserEmail
            FROM (
                SELECT IF(author_id = :userId, recipient_id, author_id) AS chat_user_id,
                       MAX(created) AS max_created
                FROM message
                WHERE author_id = :userId OR recipient_id = :userId
                GROUP BY chat_user_id
            ) AS latest_messages
            INNER JOIN users u
            ON latest_messages.chat_user_id = u.id
            INNER JOIN message m
            ON (
                (m.author_id = :userId AND m.recipient_id = latest_messages.chat_user_id)
                OR
                (m.recipient_id = :userId AND m.author_id = latest_messages.chat_user_id)
            )
            AND latest_messages.max_created = m.created
            LEFT JOIN users_black_list bl
            ON bl.owner_id = :userId AND bl.user_id = latest_messages.chat_user_id
            WHERE bl.user_id IS NULL
            """, countQuery = """
                          SELECT COUNT(*) FROM (
                              SELECT IF(author_id = :userId, recipient_id, author_id) AS chat_user_id
                              FROM message
                              WHERE author_id = :userId OR recipient_id = :userId
                              GROUP BY chat_user_id
                          ) AS subquery
                          """, nativeQuery = true)
    Page<UserChatsResponseDto> findLatestChatMessagesByUserId(@Param("userId") Long userId, Pageable pageable);
}
