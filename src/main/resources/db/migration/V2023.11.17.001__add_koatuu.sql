ALTER TABLE states
	ADD COLUMN koatuu_code VARCHAR(10) NULL DEFAULT NULL;

ALTER TABLE cities
	ADD COLUMN koatuu_code VARCHAR(10) NULL DEFAULT NULL;
