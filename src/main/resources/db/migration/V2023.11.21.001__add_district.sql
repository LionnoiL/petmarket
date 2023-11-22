CREATE TABLE districts (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	koatuu_code VARCHAR(10) NOT NULL,
	CONSTRAINT disctricts_pkey PRIMARY KEY (id)
);

ALTER TABLE cities
	ADD COLUMN district_id BIGINT NULL DEFAULT NULL,
	ADD CONSTRAINT cities_fk_districts FOREIGN KEY (district_id) REFERENCES districts (id);

ALTER TABLE districts
	ADD COLUMN state_id BIGINT NULL DEFAULT NULL,
	ADD CONSTRAINT FK_districts_states FOREIGN KEY (state_id) REFERENCES states (id);
