CREATE TABLE blog_attributes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sort_order INT DEFAULT NULL
);

CREATE TABLE blog_attribute_translation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lang_code VARCHAR(10) NOT NULL,
    owner_id BIGINT NOT NULL,
    title VARCHAR(250),

    CONSTRAINT blog_attribute_translation_fk_attribute FOREIGN KEY (owner_id) REFERENCES blog_attributes (id),
    CONSTRAINT blog_attribute_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages (lang_code)
);

CREATE TABLE posts_attributes (
    post_id BIGINT NOT NULL,
    attribute_id BIGINT NOT NULL,

    CONSTRAINT pk_posts_attributes PRIMARY KEY (post_id, attribute_id),
    CONSTRAINT fk_posts_attributes_post FOREIGN KEY (post_id) REFERENCES blog_posts (id),
    CONSTRAINT fk_posts_attributes_attribute FOREIGN KEY (attribute_id) REFERENCES blog_attributes (id)
);
