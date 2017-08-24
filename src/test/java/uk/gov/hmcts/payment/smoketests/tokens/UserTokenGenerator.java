package uk.gov.hmcts.payment.smoketests.tokens;


import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class UserTokenGenerator {

    private final String baseUrl;
    private final String userEmail;
    private final String userPassword;

    @Autowired
    public UserTokenGenerator(@Value("${idam.baseUrl}") String baseUrl,
                              @Value("${idam.email}") String userEmail,
                              @Value("${idam.password}") String userPassword) {
        this.baseUrl = baseUrl;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public UserIdAndJwt authenticate() {
        String jwt = RestAssured
                .given().auth().preemptive().basic(userEmail, userPassword)
                .when().post(baseUrl + "/login")
                .then().extract().jsonPath().get("access-token");

        Integer id = RestAssured
                .given().auth().oauth2(jwt)
                .when().get(baseUrl + "/details")
                .then().extract().jsonPath().get("id");

        return new UserIdAndJwt(id, jwt);
    }
}
