package com.budgeting.backend.dto.in;

import com.budgeting.backend.global.enums.HouseHoldRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForHouseHold{
    String email;
    HouseHoldRoles roles;
}
