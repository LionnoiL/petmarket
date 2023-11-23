CREATE TABLE cities_types (
	id BIGINT NOT NULL AUTO_INCREMENT,
	CONSTRAINT cities_types_pkey PRIMARY KEY (id)
);

CREATE TABLE cities_types_translations (
	id BIGINT(19) NOT NULL AUTO_INCREMENT,
	lang_code VARCHAR(10) NOT NULL,
	owner_id BIGINT(19) NOT NULL,
	name VARCHAR(255) NOT NULL,
	short_name VARCHAR(10) NOT NULL,
	CONSTRAINT cities_types_translations_pkey PRIMARY KEY (id),
	CONSTRAINT cities_types_translations_fk_cities_types FOREIGN KEY (owner_id) REFERENCES cities_types (id),
	CONSTRAINT cities_types_translations_fk_language FOREIGN KEY (lang_code) REFERENCES languages (lang_code)
);

ALTER TABLE cities ADD COLUMN city_type_name VARCHAR(255) NULL DEFAULT NULL;

ALTER TABLE cities ADD COLUMN city_type_short_name VARCHAR(10) NULL DEFAULT NULL;