RENAME TABLE atribute_groups TO attribute_groups;
RENAME TABLE atribute_groups_translation TO attribute_groups_translation;
RENAME TABLE atribute TO attribute;
RENAME TABLE atribute_translation TO attribute_translation;
RENAME TABLE atribute_values TO attribute_values;


ALTER TABLE attribute_values ADD COLUMN attribute_id bigint;
ALTER TABLE attribute_values ADD COLUMN attribute_value TEXT;

UPDATE attribute_values SET attribute_id = atribute_id;
UPDATE attribute_values SET attribute_value = atribute_value;

ALTER TABLE attribute_values DROP FOREIGN KEY atribute_values_fk_atribute;
ALTER TABLE attribute_values DROP COLUMN atribute_value;
ALTER TABLE attribute_values DROP COLUMN atribute_id;

ALTER TABLE attribute_values ADD CONSTRAINT atеribute_values_fk_atribute
FOREIGN KEY (attribute_id) REFERENCES attribute(id);



ALTER TABLE attribute ADD COLUMN attribute_group_id bigint;
UPDATE attribute SET attribute_group_id = atribute_group_id;

ALTER TABLE attribute DROP FOREIGN KEY atribute_fk_atribute_groups;
ALTER TABLE attribute DROP COLUMN atribute_group_id;

ALTER TABLE attribute ADD CONSTRAINT attribute_fk_atribute_groups
FOREIGN KEY (attribute_group_id) REFERENCES attribute_groups(id);



ALTER TABLE attribute_groups ADD COLUMN attribute_type varchar(50);
UPDATE attribute_groups SET attribute_type = atribute_type;
ALTER TABLE attribute_groups DROP COLUMN atribute_type;