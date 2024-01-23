package org.petmarket.advertisements.attributes.repository;

import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collections;
import java.util.List;

import static org.petmarket.utils.MessageUtils.ATTRIBUTE_NOT_FOUND;

public interface AttributeRepository extends AttributeRepositoryBasic {

    @Query(value = """
                SELECT DISTINCT attr
                FROM Advertisement adv
                JOIN adv.attributes attr
                JOIN attr.group ag
                WHERE adv.category.id = :id AND ag.useInFilter = true AND adv.status = 'ACTIVE'
                """)
    List<Attribute> findAllAttributesInFilterByCategoryId(@Param("id") Long id);

    default List<Attribute> getAttributesFromIds(List<Long> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        return ids.stream()
                .map(id -> findById(id).orElseThrow(
                        () -> {
                            throw new ItemNotFoundException(ATTRIBUTE_NOT_FOUND);
                        }
                ))
                .toList();
    }
}
