CREATE TABLE cart (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
   	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_cart_id PRIMARY KEY (id),
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    advertisement_id BIGINT NOT NULL,
    qty INT NOT NULL CHECK (qty >= 1),
    cart_id BIGINT NOT NULL,

    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES cart(id),
    CONSTRAINT fk_cart_items_advertisement FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);

CREATE TABLE blog_posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    author_id BIGINT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PUBLISHED', 'DRAFT')),

    CONSTRAINT fk_blog_post_author FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE blog_posts_translations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lang_code varchar(10) NOT NULL,
	owner_id BIGINT NOT NULL,
    title VARCHAR(250) NOT NULL,
    shortText VARCHAR(500),
    text TEXT NOT NULL,

    CONSTRAINT blog_posts_translation_fk_blog_posts FOREIGN KEY (owner_id) REFERENCES blog_posts(id),
	CONSTRAINT blog_posts_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE blog_categories (
    id BIGINT AUTO_INCREMENT,

	CONSTRAINT blog_categories_pkey PRIMARY KEY (id)
);

CREATE TABLE blog_categories_translations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lang_code varchar(10) NOT NULL,
	owner_id BIGINT NOT NULL,
    category_name VARCHAR(255) NOT NULL,
    category_description VARCHAR(500),

    CONSTRAINT blog_categories_translation_fk_blog_categories FOREIGN KEY (owner_id) REFERENCES blog_categories(id),
	CONSTRAINT blog_categories_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE post_categories (
    post_id BIGINT,
    category_id BIGINT,

    CONSTRAINT pk_post_categories PRIMARY KEY (post_id, category_id),
    CONSTRAINT fk_post_categories_post FOREIGN KEY (post_id) REFERENCES blog_posts(id),
    CONSTRAINT fk_post_categories_category FOREIGN KEY (category_id) REFERENCES blog_categories(id)
);

CREATE TABLE blog_comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment TEXT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'DRAFT')),

    CONSTRAINT fk_blog_comments_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_blog_comments_post FOREIGN KEY (post_id) REFERENCES blog_posts(id)
);

CREATE TABLE blog_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    image_url TEXT NOT NULL,

    CONSTRAINT fk_blog_images_post FOREIGN KEY (post_id) REFERENCES blog_posts(id)
);
