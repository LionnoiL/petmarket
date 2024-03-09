ALTER TABLE users
	DROP COLUMN phone;

CREATE TABLE user_phones (
	id bigint NOT NULL AUTO_INCREMENT,
	owner_id BIGINT NOT NULL,
	phone_number VARCHAR(50) NOT NULL DEFAULT '',
	is_main BIT NOT NULL DEFAULT 0,
	CONSTRAINT user_phones_pkey PRIMARY KEY (id),
	CONSTRAINT `FK__users_phones` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
);

