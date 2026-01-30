package com.petfuneral.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 宠物物种统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetSpeciesStats {
    /**
     * 物种
     */
    private String species;

    /**
     * 物种名称
     */
    private String speciesName;

    /**
     * 数量
     */
    private Long count;

    /**
     * 占比
     */
    private Double percentage;
}
