CREATE TABLE attributes_group_categories (
	  attribute_group_id BIGINT,
    category_id BIGINT,
    CONSTRAINT attributes_group_categories_pk PRIMARY KEY (attribute_group_id, category_id),
    CONSTRAINT attributes_group_categories_fk_attribute_groups FOREIGN KEY (attribute_group_id) REFERENCES attribute_groups(id),
    CONSTRAINT attributes_group_categories_fk_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);

ALTER TABLE attribute_groups DROP FOREIGN KEY atribute_groups_fk_categories;
ALTER TABLE attribute_groups DROP COLUMN category_id;
