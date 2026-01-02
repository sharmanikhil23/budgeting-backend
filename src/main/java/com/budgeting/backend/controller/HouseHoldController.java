package com.budgeting.backend.controller;

import com.budgeting.backend.dto.in.AddingUserInHouseHold;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.HouseHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/house-hold")
public class HouseHoldController {

    private HouseHoldService houseHoldService;

    @Autowired
    public HouseHoldController(HouseHoldService houseHoldService){
        this.houseHoldService = houseHoldService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createHouseHold(@RequestBody HouseHold houseHold, @AuthenticationPrincipal User user){
        return houseHoldService.makeHouseHold(houseHold, user);
    }

    @PostMapping("/add-users/{inviteCode}")
    public ResponseEntity<?> addMembersInHouseHold(@PathVariable String inviteCode,
                                                   @AuthenticationPrincipal User user){
        return houseHoldService.addUsersInHouseHold(inviteCode,user.getId());
    }

    @GetMapping("/invite-code/{houseHoldId}")
    public ResponseEntity<?> getInviteCode(@PathVariable String houseHoldId,@AuthenticationPrincipal User user){
        return houseHoldService.getInviteCode(houseHoldId, user.getId());
    }
}
