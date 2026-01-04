package com.budgeting.backend.dto.out;

import com.budgeting.backend.entity.HouseHoldEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseHoldDTO {
    private String id;
    private String name;
    private String currency;

    public HouseHoldDTO(HouseHoldEntity entity) {
        this.id = entity.getId().toString();
        this.name = entity.getName();
        this.currency = entity.getCurrency().name();
    }
}

