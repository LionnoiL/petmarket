CREATE TABLE languages (
	lang_code varchar(10) NOT NULL,
	name varchar(50) NOT NULL,
	enable BIT(1) NULL DEFAULT 1,
	CONSTRAINT language_pkey PRIMARY KEY (lang_code)
);

INSERT INTO languages (lang_code, name) VALUES
('ua', 'Українська'),
('en', 'English');

CREATE TABLE options (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NULL DEFAULT '',
	phones varchar(500) NULL DEFAULT '',
	CONSTRAINT options_pkey PRIMARY KEY (id)
);

INSERT INTO options (lang_code) VALUES ('ua');

CREATE TABLE countries (
	id bigint NOT NULL AUTO_INCREMENT,
	name VARCHAR(250) NOT NULL,
	alias varchar(500) NOT NULL,
	CONSTRAINT countries_pkey PRIMARY KEY (id)
);

CREATE TABLE states (
	id bigint NOT NULL AUTO_INCREMENT,
	name VARCHAR(250) NOT NULL,
	alias varchar(500) NOT NULL,
	country_id bigint NOT NULL,
	CONSTRAINT states_pkey PRIMARY KEY (id),
	CONSTRAINT states_fk_countries FOREIGN KEY (country_id) REFERENCES countries(id)
);

CREATE TABLE cities (
	id bigint NOT NULL AUTO_INCREMENT,
	name VARCHAR(250) NOT NULL,
	alias varchar(500) NOT NULL,
	state_id bigint NOT NULL,
	CONSTRAINT cities_pkey PRIMARY KEY (id),
	CONSTRAINT cities_fk_states FOREIGN KEY (state_id) REFERENCES states(id)
);

CREATE TABLE locations (
	id bigint NOT NULL AUTO_INCREMENT,
	city_id bigint NOT NULL,
	latitude FLOAT,
	longitude FLOAT,
	CONSTRAINT locations_pkey PRIMARY KEY (id),
	CONSTRAINT locations_fk_cities FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE users (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	status varchar(50) NOT NULL DEFAULT 'ACTIVE',
	user_type varchar(50) NOT NULL DEFAULT 'USER',
	user_name VARCHAR(250),
	first_name VARCHAR(100),
	last_name VARCHAR(100),
	google_name VARCHAR(250),
	google_user_name VARCHAR(250),
	email VARCHAR(250),
	phone VARCHAR(50),
	password VARCHAR(250),
	site TEXT,
	location_id bigint NULL DEFAULT NULL,
	rating INT NOT NULL DEFAULT 0,
	lang_code varchar(10) NULL DEFAULT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_fk_locations FOREIGN KEY (location_id) REFERENCES locations(id),
	UNIQUE INDEX user_name_uix (user_name),
	UNIQUE INDEX email_uix (email),
	UNIQUE INDEX phone_uix (phone)
);

CREATE TABLE roles (
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(50) NOT NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

INSERT INTO roles (name) VALUES ("ROLE_USER");
INSERT INTO roles (name) VALUES ("ROLE_ADMIN");

CREATE TABLE user_roles(
    user_id bigint,
    role_id bigint,
    CONSTRAINT user_roles_fk_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT user_roles_fk_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO users (user_name,email, password, first_name)
VALUES ('admin@email.com', 'admin@email.com', '$2a$10$5qBqaBeWBCgn61.C1A7MtejTofjljRTMsLs5NhG55cQ2sSeGIZA0u', 'admin');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 2);


CREATE TABLE categories (
	id bigint NOT NULL AUTO_INCREMENT,
	alias varchar(500) NOT NULL,
	parent_id bigint NULL DEFAULT NULL,
	CONSTRAINT categories_pkey PRIMARY KEY (id),
	CONSTRAINT categories_fk_categories FOREIGN KEY (parent_id) REFERENCES categories(id)
);

CREATE TABLE categories_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT categories_translation_pkey PRIMARY KEY (id),
	CONSTRAINT categories_translation_fk_categories FOREIGN KEY (owner_id) REFERENCES categories(id),
	CONSTRAINT categories_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE advertisements (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	ending DATE NULL,
	author_id bigint NOT NULL,
	alias VARCHAR(500) NOT NULL,
	category_id bigint NOT NULL,
	location_id bigint NOT NULL,
	advertisement_status varchar(50) NOT NULL DEFAULT 'ACTIVE',
	price DOUBLE NULL DEFAULT 0,
	quantity INT NOT NULL DEFAULT 1,
	advertisement_type varchar(50) NOT NULL DEFAULT 'SIMPLE',
	rating INT NOT NULL DEFAULT 0,
	CONSTRAINT advertisements_pkey PRIMARY KEY (id),
	CONSTRAINT advertisement_fk_categories FOREIGN KEY (category_id) REFERENCES categories(id),
	CONSTRAINT advertisement_fk_users FOREIGN KEY (author_id) REFERENCES users(id),
	CONSTRAINT advertisement_fk_locations FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE TABLE advertisements_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT advertisements_translation_pkey PRIMARY KEY (id),
	CONSTRAINT advertisements_translation_fk_advertisements FOREIGN KEY (owner_id) REFERENCES advertisements(id),
	CONSTRAINT advertisements_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE users_black_list (
	id bigint NOT NULL AUTO_INCREMENT,
	owner_id bigint NOT NULL,
	user_id bigint NOT NULL,
	CONSTRAINT users_black_list_pkey PRIMARY KEY (id),
	CONSTRAINT users_black_list_fk_users FOREIGN KEY (owner_id) REFERENCES users(id),
	CONSTRAINT users_black_list_fk_users_2 FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE users_favorite_list (
	id bigint NOT NULL AUTO_INCREMENT,
	owner_id bigint NOT NULL,
	advertisement_id bigint NOT NULL,
	CONSTRAINT users_favorite_list_pkey PRIMARY KEY (id),
	CONSTRAINT users_favorite_list_fk_users FOREIGN KEY (owner_id) REFERENCES users(id),
	CONSTRAINT users_favorite_list_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);

CREATE TABLE users_history (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	owner_id bigint NOT NULL,
	advertisement_id bigint NOT NULL,
	CONSTRAINT users_history_list_pkey PRIMARY KEY (id),
	CONSTRAINT users_history_list_fk_users FOREIGN KEY (owner_id) REFERENCES users(id),
	CONSTRAINT users_history_list_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);

CREATE TABLE pages (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	page_type varchar(50) NOT NULL,
	CONSTRAINT pages_pkey PRIMARY KEY (id)
);

CREATE TABLE pages_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT pages_translation_pkey PRIMARY KEY (id),
	CONSTRAINT pages_translation_fk_pages FOREIGN KEY (owner_id) REFERENCES pages(id),
	CONSTRAINT pages_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);


CREATE TABLE deliveries (
	id bigint NOT NULL AUTO_INCREMENT,
	CONSTRAINT deliveries_pkey PRIMARY KEY (id)
);

CREATE TABLE deliveries_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT deliveries_translation_pkey PRIMARY KEY (id),
	CONSTRAINT deliveries_translation_fk_pages FOREIGN KEY (owner_id) REFERENCES deliveries(id),
	CONSTRAINT deliveries_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE pays (
	id bigint NOT NULL AUTO_INCREMENT,
	CONSTRAINT pays_pkey PRIMARY KEY (id)
);

CREATE TABLE pays_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT pays_translation_pkey PRIMARY KEY (id),
	CONSTRAINT pays_translation_fk_pages FOREIGN KEY (owner_id) REFERENCES pays(id),
	CONSTRAINT pays_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE orders (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	author_id bigint NOT NULL,
	order_status varchar(50) NOT NULL DEFAULT 'NEW',
	delvery_id bigint NOT NULL,
	pay_id bigint NOT NULL,
	order_comment TEXT,
	CONSTRAINT orders_pkey PRIMARY KEY (id),
	CONSTRAINT orders_fk_users FOREIGN KEY (author_id) REFERENCES users(id),
	CONSTRAINT orders_fk_deliveries FOREIGN KEY (delvery_id) REFERENCES deliveries(id),
	CONSTRAINT orders_fk_pays FOREIGN KEY (pay_id) REFERENCES pays(id)
);

CREATE TABLE orders_items (
	id bigint NOT NULL AUTO_INCREMENT,
	order_id bigint NOT NULL,
	advertisement_id bigint NOT NULL,
	qty DOUBLE NOT NULL DEFAULT 0,
	price DOUBLE NOT NULL DEFAULT 0,
	item_sum DOUBLE NOT NULL DEFAULT 0,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT orders_items_pkey PRIMARY KEY (id),
	CONSTRAINT orders_items_fk_orders FOREIGN KEY (order_id) REFERENCES orders(id),
	CONSTRAINT orders_items_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);


CREATE TABLE reviews (
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	author_id bigint NOT NULL,
	advertisement_id bigint NULL DEFAULT NULL,
	user_id bigint NULL DEFAULT NULL,
	order_id bigint NULL DEFAULT NULL,
	review_type varchar(50) NULL DEFAULT NULL,
	rewiew_value INT,
	description TEXT,
	CONSTRAINT reviews_pkey PRIMARY KEY (id),
	CONSTRAINT reviews_fk_users FOREIGN KEY (author_id) REFERENCES users(id),
	CONSTRAINT reviews_fk_users_2 FOREIGN KEY (user_id) REFERENCES users(id),
	CONSTRAINT reviews_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id),
	CONSTRAINT reviews_fk_orders FOREIGN KEY (order_id) REFERENCES orders(id)
);


CREATE TABLE advertisements_images (
	id BIGINT NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	image_name VARCHAR(500) NOT NULL,
	advertisement_id BIGINT NOT NULL,
	main_image BIT(1) NULL DEFAULT 0,
	image_url_big TEXT NOT NULL,
	image_url_small TEXT NOT NULL,
	CONSTRAINT advertisements_images_pkey PRIMARY KEY (id),
	CONSTRAINT advertisements_images_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);


CREATE TABLE user_images (
	id BIGINT NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	image_name VARCHAR(500) NOT NULL,
	user_id BIGINT NOT NULL,
	main_image BIT(1) NULL DEFAULT 0,
	image_url_big TEXT NOT NULL,
	image_url_small TEXT NOT NULL,
	CONSTRAINT user_images_pkey PRIMARY KEY (id),
	CONSTRAINT user_images_fk_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE animal_breeds(
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	category_id bigint NOT NULL,
	CONSTRAINT animal_breeds_pkey PRIMARY KEY (id),
	CONSTRAINT animal_breeds_fk_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);


CREATE TABLE animal_breeds_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT animal_breeds_translation_pkey PRIMARY KEY (id),
	CONSTRAINT animal_breeds_translation_fk_animal_breeds FOREIGN KEY (owner_id) REFERENCES animal_breeds(id),
	CONSTRAINT animal_breeds_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE animal_breeds_comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    animal_breeds_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'DRAFT')),
    CONSTRAINT animal_breeds_comments_translation_pkey PRIMARY KEY (id),
    CONSTRAINT animal_breeds_comments_fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT animal_breeds_comments_fk_animal_breeds FOREIGN KEY (animal_breeds_id) REFERENCES animal_breeds(id)
);

CREATE TABLE atribute_groups(
	id bigint NOT NULL AUTO_INCREMENT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	atribute_type VARCHAR(50) NOT NULL,
	category_id bigint NOT NULL,
	CONSTRAINT atribute_groups_pkey PRIMARY KEY (id),
	CONSTRAINT atribute_groups_fk_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE atribute_groups_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	description TEXT,
	CONSTRAINT atribute_groups_translation_pkey PRIMARY KEY (id),
	CONSTRAINT atribute_groups_translation_fk_atribute_groups FOREIGN KEY (owner_id) REFERENCES atribute_groups(id),
	CONSTRAINT atribute_groups_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);

CREATE TABLE atribute(
	id BIGINT NOT NULL AUTO_INCREMENT,
	atribute_group_id BIGINT NOT NULL,
	sort_order INT,
	CONSTRAINT atribute_pkey PRIMARY KEY (id),
	CONSTRAINT atribute_fk_atribute_groups FOREIGN KEY (atribute_group_id) REFERENCES atribute_groups(id)
);


CREATE TABLE atribute_translation (
	id bigint NOT NULL AUTO_INCREMENT,
	lang_code varchar(10) NOT NULL,
	owner_id bigint NOT NULL,
	title varchar(250) NOT NULL,
	CONSTRAINT atribute_translation_pkey PRIMARY KEY (id),
	CONSTRAINT atribute_translation_fk_atribute FOREIGN KEY (owner_id) REFERENCES atribute(id),
	CONSTRAINT atribute_translation_fk_language FOREIGN KEY (lang_code) REFERENCES languages(lang_code)
);


CREATE TABLE atribute_values (
	id bigint NOT NULL AUTO_INCREMENT,
    advertisement_id BIGINT NOT NULL,
    atribute_id BIGINT,
    atribute_value TEXT,
    CONSTRAINT atribute_values_pkey PRIMARY KEY (id),
    CONSTRAINT atribute_values_fk_atribute FOREIGN KEY (atribute_id) REFERENCES atribute(id),
    CONSTRAINT atribute_values_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id)
);
