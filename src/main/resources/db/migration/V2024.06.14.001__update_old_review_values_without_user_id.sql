CREATE TEMPORARY TABLE temp_ad_author_ids AS
SELECT id AS advertisement_id, author_id
FROM advertisements;

UPDATE reviews r
    JOIN temp_ad_author_ids t ON r.advertisement_id = t.advertisement_id
SET r.user_id = t.author_id
WHERE r.user_id IS NULL;

DROP TEMPORARY TABLE temp_ad_author_ids;
