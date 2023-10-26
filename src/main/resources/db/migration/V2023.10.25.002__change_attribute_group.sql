ALTER TABLE attribute_groups
ADD sort_order int NULL DEFAULT 0;

ALTER TABLE attribute_groups
ADD use_in_filter BIT(1) NULL DEFAULT 0;
