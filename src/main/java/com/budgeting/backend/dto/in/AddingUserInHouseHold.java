package com.budgeting.backend.dto.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddingUserInHouseHold {
    List<UserForHouseHold> user;
}
