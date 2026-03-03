package apiTests;

import api.models.UserRequest;
import api.models.UserResponse;
import base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestContext;

import static io.restassured.RestAssured.given;

public class CreateUserTest extends BaseApiTest {



    @Test(description = "Create a new user and share ID securely")
    public void testCreateUser() {

        UserRequest requestPayload = UserRequest.builder()
                .name("Ahmed QA")
                .job("Automation Expert")
                .build();

        UserResponse responseBody = given()
                .spec(requestSpec)
                .body(requestPayload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponse.class);

        Assert.assertNotNull(responseBody.getId(), "Error: User ID is null!");
        Assert.assertEquals(responseBody.getName(), requestPayload.getName(), "Error: Name mismatch!");
        Assert.assertEquals(responseBody.getJob(), requestPayload.getJob(), "Error: Job mismatch!");
        Assert.assertNotNull(responseBody.getCreatedAt(), "Error: Creation date is missing!");


        TestContext.setUserId(responseBody.getId());
        TestContext.setUserName(responseBody.getName());
        TestContext.setUserJob(responseBody.getJob());
        System.out.println("✅ User created. ID saved to TestContext: " + responseBody.getId());
    }
}
