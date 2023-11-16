ALTER TABLE advertisements
	ADD COLUMN breed_id BIGINT NULL DEFAULT NULL AFTER rating,
	ADD CONSTRAINT advertisement_fk_breeds FOREIGN KEY (breed_id) REFERENCES animal_breeds (id);