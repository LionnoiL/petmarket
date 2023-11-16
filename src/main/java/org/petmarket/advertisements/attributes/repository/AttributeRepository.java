package org.petmarket.advertisements.attributes.repository;

import org.petmarket.advertisements.attributes.entity.Attribute;
import org.petmarket.errorhandling.ItemNotFoundException;

import java.util.List;

import static org.petmarket.utils.MessageUtils.ATTRIBUTE_NOT_FOUND;

public interface AttributeRepository extends AttributeRepositoryBasic {

    default List<Attribute> getAttributesFromIds(List<Long> ids) {
        return ids.stream()
                .map(id -> findById(id).orElseThrow(
                        () -> {
                            throw new ItemNotFoundException(ATTRIBUTE_NOT_FOUND);
                        }
                ))
                .toList();
    }
}
