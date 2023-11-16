package org.petmarket.advertisements.category.repository;

import lombok.RequiredArgsConstructor;
import org.petmarket.advertisements.category.dto.AdvertisementCategoryResponseDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class AdvertisementCategoryTranslateRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<AdvertisementCategoryResponseDto> findAllByLanguage(String langCode) {
        return jdbcTemplate.query(
                """
                             SELECT ua.id, :langCode langCode, ua.parent_id, ua.alias, ua.available_in_tags, ua.available_in_favorite,
                             IFNULL(lang.title, ua.title) AS title_lang,
                             IFNULL(lang.tag_title, ua.tag_title) AS tag_title_lang,
                             IFNULL(lang.description, ua.description) AS description_lang
                             FROM
                             (SELECT c.*, t.title, t.tag_title, t.description FROM categories c
                             LEFT JOIN categories_translation t ON t.owner_id = c.id
                             LEFT JOIN languages l ON t.lang_code = l.lang_code
                             WHERE t.lang_code = (SELECT lang_code FROM options WHERE id = 1) AND l.enable) ua
                             LEFT JOIN
                             (SELECT c.id, t.title, t.tag_title, t.description FROM categories c
                             LEFT JOIN categories_translation t ON t.owner_id = c.id
                             LEFT JOIN languages l ON t.lang_code = l.lang_code
                             WHERE t.lang_code = :langCode AND l.enable) lang
                             ON ua.id = lang.id
                        """,

                Map.of("langCode", langCode),
                new CategoryRowMapper()
        );
    }

    private static class CategoryRowMapper implements RowMapper<AdvertisementCategoryResponseDto> {
        @Override
        public AdvertisementCategoryResponseDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            AdvertisementCategoryResponseDto dto = new AdvertisementCategoryResponseDto();
            dto.setId(resultSet.getLong("id"));
            dto.setAlias(resultSet.getString("alias"));
            dto.setLangCode(resultSet.getString("langCode"));
            dto.setTitle(resultSet.getString("title_lang"));
            dto.setTagTitle(resultSet.getString("tag_title_lang"));
            dto.setDescription(resultSet.getString("description_lang"));
            dto.setParentId(resultSet.getLong("parent_id"));
            dto.setAvailableInFavorite(resultSet.getBoolean("available_in_favorite"));
            dto.setAvailableInTags(resultSet.getBoolean("available_in_tags"));
            return dto;
        }
    }
}
