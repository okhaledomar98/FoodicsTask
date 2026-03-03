package apiTests;

import api.models.UserRequest;
import api.models.UserResponse;
import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestContext;

import static io.restassured.RestAssured.given;

public class UpdateUserTest extends BaseApiTest {

    @Test(description = "Update the user's job and validate the changes")
    public void testUpdateUser() {


        String userId = TestContext.getUserId();
        Assert.assertNotNull(userId, "Error: Shared User ID is null. Cannot proceed with update.");


        UserRequest updatePayload = UserRequest.builder()
                .name("Ahmed QA")
                .job("Senior SDET")
                .build();


        UserResponse responseBody = given()
                .spec(requestSpec)
                .body(updatePayload)
                .pathParam("id", userId)
                .when()
                .put("/api/users/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);


        Assert.assertEquals(responseBody.getJob(), updatePayload.getJob(), "Error: Job was not updated correctly!");
        Assert.assertEquals(responseBody.getName(), updatePayload.getName(), "Error: Name was changed unexpectedly!");
        Assert.assertNotNull(responseBody.getUpdatedAt(), "Error: updatedAt timestamp is missing!");

        System.out.println("✅ User updated successfully. New Job: " + responseBody.getJob() + " at " + responseBody.getUpdatedAt());


        TestContext.unload();
    }
}