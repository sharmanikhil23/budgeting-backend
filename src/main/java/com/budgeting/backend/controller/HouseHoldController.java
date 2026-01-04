package com.budgeting.backend.controller;

import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.HouseHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/households")
public class HouseHoldController {

    private final HouseHoldService houseHoldService;

    @Autowired
    public HouseHoldController(HouseHoldService houseHoldService) {
        this.houseHoldService = houseHoldService;
    }

    /* -------------------------------------------------
       HOUSEHOLD CRUD
    -------------------------------------------------- */

    @PostMapping
    public ResponseEntity<?> createHouseHold(
            @RequestBody HouseHold houseHold,
            @AuthenticationPrincipal User user) {
        return houseHoldService.makeHouseHold(houseHold, user);
    }

    @PatchMapping("/{houseHoldId}")
    public ResponseEntity<?> updateHouseHold(
            @PathVariable String houseHoldId,
            @RequestBody HouseHold houseHold,
            @AuthenticationPrincipal User user) {
        return houseHoldService.updateHouseHold(houseHold, houseHoldId, user);
    }

    @DeleteMapping("/{houseHoldId}")
    public ResponseEntity<?> deleteHouseHold(
            @PathVariable String houseHoldId,
            @AuthenticationPrincipal User user) {
        return houseHoldService.deleteHouseHold(houseHoldId, user);
    }

    /* -------------------------------------------------
       INVITE CODE
    -------------------------------------------------- */

    @GetMapping("/{houseHoldId}/invite-code")
    public ResponseEntity<?> getInviteCode(
            @PathVariable String houseHoldId,
            @AuthenticationPrincipal User user) {
        return houseHoldService.getInviteCode(houseHoldId, user);
    }

    @PostMapping("/{houseHoldId}/invite-code/regenerate")
    public ResponseEntity<?> regenerateInviteCode(
            @PathVariable String houseHoldId,
            @AuthenticationPrincipal User user) {
//        return houseHoldService.regenerateInviteCode(houseHoldId, user);
        return null;
    }

    /* -------------------------------------------------
       MEMBERSHIP
    -------------------------------------------------- */

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<?> joinHouseHold(
            @PathVariable String inviteCode,
            @AuthenticationPrincipal User user) {
        return houseHoldService.addUsersInHouseHold(inviteCode, user);
    }

    @DeleteMapping("/{houseHoldId}/members/me")
    public ResponseEntity<?> leaveHouseHold(
            @PathVariable String houseHoldId,
            @AuthenticationPrincipal User user) {
//        return houseHoldService.leaveHouseHold(houseHoldId, user);
        return null;
    }

    @DeleteMapping("/{houseHoldId}/members/{userId}")
    public ResponseEntity<?> removeMember(
            @PathVariable String houseHoldId,
            @PathVariable String userId,
            @AuthenticationPrincipal User user) {
//        return houseHoldService.removeMember(houseHoldId, userId, user);
        return null;
    }

    /* -------------------------------------------------
       ROLE MANAGEMENT (OWNER ONLY)
    -------------------------------------------------- */

//    @PatchMapping("/{houseHoldId}/members/{userId}/role")
//    public ResponseEntity<?> updateMemberRole(
//            @PathVariable String houseHoldId,
//            @PathVariable String userId,
//            @RequestParam Role role,
//            @AuthenticationPrincipal User user) {
////        return houseHoldService.updateMemberRole(houseHoldId, userId, role, user);
//        return null;
//    }

    @PostMapping("/{houseHoldId}/transfer-ownership/{userId}")
    public ResponseEntity<?> transferOwnership(
            @PathVariable String houseHoldId,
            @PathVariable String userId,
            @AuthenticationPrincipal User user) {
//        return houseHoldService.transferOwnership(houseHoldId, userId, user);r
        return null;
    }
}

/**
 *
 * Action	            MEMBER	ADMIN	OWNER
 * Create household	      ❌	 ❌	     ✅
 * Delete household	      ❌	 ❌	     ✅
 * Invite users	          ❌	 ✅	     ✅
 * Remove members	      ❌	 ✅	     ✅
 * Promote/Demote roles	  ❌	 ❌	     ✅
 * Transfer ownership	  ❌	 ❌	     ✅
 * Leave household	      ✅	 ✅	     ⚠️ (only if ownership transferred)
 */