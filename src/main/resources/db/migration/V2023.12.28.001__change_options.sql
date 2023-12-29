ALTER TABLE options DROP COLUMN lang_code;
ALTER TABLE options DROP COLUMN phones;

ALTER TABLE options ADD COLUMN options_key varchar(255) NULL;
ALTER TABLE options ADD COLUMN options_value TEXT NULL;

delete from options;

insert into options (options_key, options_value) values ("DEFAULT_LANGUAGE", "ua");
insert into options (options_key, options_value) values ("CONTACT_PHONE", "");
insert into options (options_key, options_value) values ("CONTACT_EMAIL", "");
insert into options (options_key, options_value) values ("FAVORITE_ATTRIBUTE_1_ID", "");
insert into options (options_key, options_value) values ("FAVORITE_ATTRIBUTE_2_ID", "");

