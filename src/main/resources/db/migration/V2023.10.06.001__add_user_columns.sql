ALTER TABLE users
ADD login_provider VARCHAR(50) NULL DEFAULT 'LOCAL';

ALTER TABLE users DROP COLUMN google_name;

ALTER TABLE users DROP COLUMN google_user_name;