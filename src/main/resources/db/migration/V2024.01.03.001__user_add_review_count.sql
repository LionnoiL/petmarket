ALTER TABLE users ADD COLUMN reviews_count INT NULL DEFAULT 0;

UPDATE users u SET u.reviews_count = (
	SELECT COUNT(r.id) FROM reviews r WHERE r.user_id = u.id AND r.review_type = "BUYER_TO_SELLER"
);

CREATE TRIGGER `review_trigger_insert` AFTER INSERT ON `reviews` FOR EACH ROW BEGIN
	UPDATE users u SET u.reviews_count = (
			SELECT COUNT(r.id) FROM reviews r WHERE r.user_id = u.id AND r.review_type = "BUYER_TO_SELLER")
	WHERE u.id = NEW.user_id;
END;

CREATE TRIGGER `review_trigger_delete` AFTER DELETE ON `reviews` FOR EACH ROW BEGIN
	UPDATE users u SET u.reviews_count = (
			SELECT COUNT(r.id) FROM reviews r WHERE r.user_id = u.id AND r.review_type = "BUYER_TO_SELLER")
	WHERE u.id = OLD.user_id;
END;

CREATE TRIGGER `review_trigger_update` AFTER UPDATE ON `reviews` FOR EACH ROW BEGIN
	UPDATE users u SET u.reviews_count = (
			SELECT COUNT(r.id) FROM reviews r WHERE r.user_id = u.id AND r.review_type = "BUYER_TO_SELLER")
	WHERE u.id = OLD.user_id;

	UPDATE users u SET u.reviews_count = (
			SELECT COUNT(r.id) FROM reviews r WHERE r.user_id = u.id AND r.review_type = "BUYER_TO_SELLER")
	WHERE u.id = NEW.user_id;
END;