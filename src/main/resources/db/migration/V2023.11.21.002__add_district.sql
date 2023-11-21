CREATE TABLE disctricts (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	CONSTRAINT disctricts_pkey PRIMARY KEY (id)
);

ALTER TABLE cities
	ADD COLUMN district_id BIGINT NULL DEFAULT NULL,
	ADD CONSTRAINT cities_fk_disctricts FOREIGN KEY (district_id) REFERENCES disctricts (id);