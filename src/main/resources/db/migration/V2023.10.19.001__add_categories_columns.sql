ALTER TABLE categories_translation
CHANGE COLUMN tag_title tag_title VARCHAR(30) NOT NULL;

ALTER TABLE categories
ADD available_in_tags BIT(1) NULL DEFAULT 1;

ALTER TABLE categories
ADD available_in_favorite BIT(1) NULL DEFAULT 1;
