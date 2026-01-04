package com.budgeting.backend.UnitTest.Service;

import com.budgeting.backend.UnitTest.Base;
import com.budgeting.backend.dto.in.HouseHold;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.global.enums.Currency;
import com.budgeting.backend.service.HouseHoldService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HouseHoldServiceTest extends Base {

    @Autowired
    HouseHoldService houseHoldService;

    String houseHoldId;
    String currency;

    @BeforeAll
    public void fixtureSetup(){
        HouseHold houseHold = new HouseHold();
        houseHold.setName("Testing");
        houseHold.setCurrency(Currency.CAD.getCode());

        ResponseEntity<HashMap<String,String>> created = houseHoldService.makeHouseHold(houseHold, getPrimaryUser());

        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode(),"Status code must be 201");
        Assertions.assertNotNull(created, "It should not be null");
        Assertions.assertNotNull(Objects.requireNonNull(created.getBody()).get("houseHoldId"));
        this.houseHoldId = Objects.requireNonNull(created.getBody()).get("houseHoldId");
        this.currency = Currency.CAD.getCode();
    }

    @AfterAll
    public void fixtureClearUp(){
        houseHoldService.deleteHouseHold(houseHoldId,getPrimaryUser());
    }

    @Test
    public void createHouseHold(){
        HouseHold houseHold = new HouseHold();
        houseHold.setName("Sharma house");
        houseHold.setCurrency(Currency.CAD.getCode());

        ResponseEntity<HashMap<String,String>> created = houseHoldService.makeHouseHold(houseHold, getPrimaryUser());

        Assertions.assertEquals(HttpStatus.CREATED, created.getStatusCode(),"Status code must be 201");
        Assertions.assertNotNull(created, "It should not be null");
        Assertions.assertNotNull(Objects.requireNonNull(created.getBody()).get("houseHoldId"));
    }

    @Test
    public void generateInviteCode(){
        ResponseEntity<?> temp = houseHoldService.getInviteCode(houseHoldId,getPrimaryUser());
        Assertions.assertEquals(HttpStatus.CREATED,temp.getStatusCode(),"Must be 201 status code");
    }

    @Test
    public void generateInviteCodeWithInvalidHouseHoldID(){
        String invalidId = "aaaaaaaaaaaaaaaaaaaaaaaa"; // 24 chars, not in DB
        ResponseEntity<?> temp = houseHoldService.getInviteCode(invalidId, getPrimaryUser());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, temp.getStatusCode());
    }

    @Test
    public void addingUserInHouseHold(){
        // first create a new user which we want to add to the household
        User newUser = super.createNewUser();

        String inviteCode = null;
        // now primary user or the admin will generate the code to add the user
        ResponseEntity<?> temp = houseHoldService.getInviteCode(houseHoldId,getPrimaryUser());
        Assertions.assertEquals(HttpStatus.CREATED,temp.getStatusCode(),"Must be 201 status code");

        Map<String, Object> body = (Map<String, Object>) temp.getBody();
        inviteCode = body.get("inviteCode").toString();

        ResponseEntity<?> addedUser = houseHoldService.addUsersInHouseHold(inviteCode, newUser);
        Assertions.assertEquals(HttpStatus.CREATED,addedUser.getStatusCode(),"Must return 201 status code");
    }

    @Test
    public void addingExistingUserInHouseHold(){
        // first create a new user which we want to add to the household
        User newUser = super.createNewUser();

        String inviteCode = null;
        // now primary user or the admin will generate the code to add the user
        ResponseEntity<?> temp = houseHoldService.getInviteCode(houseHoldId,getPrimaryUser());
        Assertions.assertEquals(HttpStatus.CREATED,temp.getStatusCode(),"Must be 201 status code");

        Map<String, Object> body = (Map<String, Object>) temp.getBody();
        inviteCode = body.get("inviteCode").toString();

        ResponseEntity<?> addedUser = houseHoldService.addUsersInHouseHold(inviteCode, newUser);
        Assertions.assertEquals(HttpStatus.CREATED,addedUser.getStatusCode(),"Must return 201 status code");

        inviteCode = null;
        // now primary user or the admin will generate the code to add the user
        temp = houseHoldService.getInviteCode(houseHoldId,getPrimaryUser());
        Assertions.assertEquals(HttpStatus.CREATED,temp.getStatusCode(),"Must be 201 status code");

        body = (Map<String, Object>) temp.getBody();
        inviteCode = body.get("inviteCode").toString();

        addedUser = houseHoldService.addUsersInHouseHold(inviteCode, newUser);
        Assertions.assertEquals(HttpStatus.CONFLICT,addedUser.getStatusCode(),"Must return 201 status code");
    }

}
