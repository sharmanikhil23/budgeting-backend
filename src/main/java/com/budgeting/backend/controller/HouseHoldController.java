package com.budgeting.backend.controller;

import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.dto.out.ApiResponseDTO;
import com.budgeting.backend.dto.out.HouseHoldDTO;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.HouseHoldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/household")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Invalid input - validation failed",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = "{ \"status\": \"error\", \"message\": \"Validation failed\", \"data\": {}, \"errors\": \"Name is mandatory\" }"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - JWT token is missing or invalid",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = "{ \"status\": \"error\", \"message\": \"Validation failed\", \"data\": " +
                                        "{}, \"errors\": \"Not Authorized\" }"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error - Unexpected server-side crash",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = "{ \"status\": \"error\", \"message\": \"Internal Server Error\", " +
                                        "\"data\": " +
                                        "{}, \"errors\": \"Internal server error\" }"
                        )
                )
        )
})
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
    @Operation(summary = "Create New HouseHold",
    description = "Endpoint helps to create new household for the user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Household created successfully"
            )
    })
    public ResponseEntity<ApiResponseDTO<HouseHoldDTO>> createHouseHold(
            @RequestBody HouseHold houseHold,
            @AuthenticationPrincipal User user) {
        return houseHoldService.makeHouseHold(houseHold, user);
    }

    @Operation(summary = "Update HouseHold",
            description = "Endpoint helps to update the houseHold only admin or owner can do it")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Household updated successfully"
            )
    })
    @PatchMapping("/{houseHoldId}")
    public ResponseEntity<?> updateHouseHold(
            @PathVariable String houseHoldId,
            @RequestBody HouseHold houseHold,
            @AuthenticationPrincipal User user) {
        return houseHoldService.updateHouseHold(houseHold, houseHoldId, user);
    }

    @DeleteMapping("/{houseHoldId}/{deleteNow}")
    public ResponseEntity<?> deleteHouseHold(
            @PathVariable String houseHoldId,
            @AuthenticationPrincipal User user, @PathVariable Boolean deleteNow) {
        return houseHoldService.deleteHouseHold(houseHoldId, user, deleteNow);
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