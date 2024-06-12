DROP TRIGGER IF EXISTS `review_trigger_insert`;
DROP TRIGGER IF EXISTS `review_trigger_delete`;
DROP TRIGGER IF EXISTS `review_trigger_update`;

ALTER TABLE reviews RENAME COLUMN rewiew_value TO review_value;

UPDATE users u
SET
    u.reviews_count = (
        SELECT COUNT(r.id)
        FROM reviews r
        WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
    ),
    u.rating = COALESCE((
        SELECT AVG(r.review_value)
        FROM reviews r
        WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        ), 0);

CREATE TRIGGER `review_trigger_insert`
    AFTER INSERT ON `reviews`
    FOR EACH ROW
BEGIN
    UPDATE users u
    SET
        u.reviews_count = (
            SELECT COUNT(r.id)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        ),
        u.rating = (
            SELECT AVG(r.review_value)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        )
    WHERE u.id = NEW.user_id;
END;

CREATE TRIGGER `review_trigger_delete`
    AFTER DELETE ON `reviews`
    FOR EACH ROW
BEGIN
    UPDATE users u
    SET
        u.reviews_count = (
        SELECT COUNT(r.id)
        FROM reviews r
        WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
    ),
    u.rating = COALESCE((
        SELECT AVG(r.review_value)
        FROM reviews r
        WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
    ), 0)
    WHERE u.id = OLD.user_id;
END;

CREATE TRIGGER `review_trigger_update` AFTER UPDATE ON `reviews` FOR EACH ROW BEGIN
    UPDATE users u
    SET
        u.reviews_count = (
            SELECT COUNT(r.id)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        ),
        u.rating = (
            SELECT AVG(r.review_value)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        )
    WHERE u.id = OLD.user_id;

    UPDATE users u
    SET
        u.reviews_count = (
            SELECT COUNT(r.id)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        ),
        u.rating = (
            SELECT AVG(r.review_value)
            FROM reviews r
            WHERE r.user_id = u.id AND r.review_type IN ("BUYER_TO_SELLER", "SELLER_TO_BUYER")
        )
    WHERE u.id = NEW.user_id;
END;
