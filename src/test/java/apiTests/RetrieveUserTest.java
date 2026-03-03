package apiTests;

import api.models.UserResponse;
import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestContext;

import static io.restassured.RestAssured.given;

public class RetrieveUserTest extends BaseApiTest {

    @Test(description = "Retrieve the user using the shared ID")
    public void testRetrieveUser() {


        String userId = TestContext.getUserId();
        String expectedName = TestContext.getUserName();
        String expectedJob = TestContext.getUserJob();

        Assert.assertNotNull(userId, "Error: Shared User ID is null. Did the Create test run first?");

        UserResponse responseBody = given()
                .spec(requestSpec)
                .pathParam("id", userId)
                .when()
                .get("/api/users/{id}")
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);

        Assert.assertEquals(responseBody.getName(), expectedName, "Error: Retrieved Name does not match the created user!");
        Assert.assertEquals(responseBody.getJob(), expectedJob, "Error: Retrieved Job does not match the created user!");

        System.out.println("✅ User retrieved successfully with ID: " + userId);
    }


    @Test(description = "Error Handling: Attempt to retrieve a non-existent user - Expect 404")
    public void testRetrieveInvalidUser() {


        String invalidUserId = "999999999";

        given()
                .spec(requestSpec)
                .pathParam("id", invalidUserId)
                .when()
                .get("/api/users/{id}")
                .then()
                .log().ifError()
                .statusCode(404);

        System.out.println("✅ Error handling validated: Server correctly returned 404 for invalid user ID.");
    }
}
