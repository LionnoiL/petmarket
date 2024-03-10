ALTER TABLE users
    ADD COLUMN facebook_link VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE users
    ADD COLUMN instagram_link VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE users
    ADD COLUMN twitter_link VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
