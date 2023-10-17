ALTER TABLE pages
ADD CONSTRAINT pages_unique_page_type UNIQUE (page_type);