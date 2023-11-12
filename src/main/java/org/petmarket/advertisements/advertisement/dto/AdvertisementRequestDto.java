package org.petmarket.advertisements.advertisement.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.petmarket.advertisements.advertisement.entity.AdvertisementType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementRequestDto {

    @NotBlank(message = "The 'title' cannot be empty")
    @Size(min = 1, max = 250, message
        = "Title must be between 1 and 250 characters")
    @Schema(example = "Продам собаку породи такса.")
    private String title;
    @Schema(example = """
    Пропоную свою неймовірно веселу та лагідну собаку породи 
    такса у зв'язку з невідкладними обставинами. Цей маленький кумедний пес 
    має веселе характер, прекрасно ладнає з дітьми та іншими тваринами. 
    Він повністю привчений до основних команд і готовий принести радість своєму новому власнику.
    """)
    private String description;
    @DecimalMin(value = "0", message = "Price must be greater than or equal to 0")
    @Schema(example = "1000")
    private BigDecimal price;
    @NotNull
    @Schema(example = "1")
    private Long cityId;
    @NotNull
    @Schema(example = "1")
    private Long categoryId;
    @ArraySchema(schema = @Schema(type = "integer", format = "int64", example = "1"), uniqueItems = true)
    private List<Long> deliveriesIds;
    @ArraySchema(schema = @Schema(type = "integer", format = "int64", example = "1"), uniqueItems = true)
    private List<Long> paymentsIds;
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Schema(example = "1")
    private int quantity;
    @NotNull
    @Schema(example = "SIMPLE")
    private AdvertisementType type;
    private List<Long> attributesIds;
}
