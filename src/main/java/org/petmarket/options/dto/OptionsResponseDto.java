package org.petmarket.options.dto;

import lombok.*;
import org.petmarket.options.entity.OptionsKey;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionsResponseDto {

    private OptionsKey key;
    private String value;
}
