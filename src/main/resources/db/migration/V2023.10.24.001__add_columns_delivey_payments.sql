ALTER TABLE pays
ADD enable BIT(1) NULL DEFAULT 1;

ALTER TABLE pays
ADD alias varchar(500) NOT NULL;

ALTER TABLE deliveries
ADD enable BIT(1) NULL DEFAULT 1;

ALTER TABLE deliveries
ADD alias varchar(500) NOT NULL;

INSERT INTO pays (alias) VALUES ('gotivkou');
INSERT INTO pays_translation (lang_code, owner_id, title) VALUES (
'ua',
(SELECT id FROM pays WHERE alias = 'gotivkou'),
'Готівкою');

INSERT INTO deliveries (alias) VALUES ('samoviviz');
INSERT INTO deliveries_translation (lang_code, owner_id, title) VALUES (
'ua',
(SELECT id FROM deliveries WHERE alias = 'samoviviz'),
'Самовивіз');