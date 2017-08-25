package uk.gov.hmcts.payment.smoketests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.auth.provider.service.token.ServiceTokenGenerator;
import uk.gov.hmcts.payment.api.contract.CreatePaymentRequestDto;
import uk.gov.hmcts.payment.smoketests.tokens.UserIdAndJwt;
import uk.gov.hmcts.payment.smoketests.tokens.UserTokenGenerator;

import static uk.gov.hmcts.payment.api.contract.CreatePaymentRequestDto.createPaymentRequestDtoWith;

public class CreatePaymentSmokeTest extends SmokeTestBase {

    private final static CreatePaymentRequestDto VALID_REQUEST = createPaymentRequestDtoWith()
            .amount(100)
            .description("Description")
            .reference("Reference")
            .returnUrl("https://return-url").build();

    @Autowired
    @Value("${payment.baseUrl}")
    private String baseUrl;

    @Autowired
    private ServiceTokenGenerator serviceTokenGenerator;

    @Autowired
    private UserTokenGenerator userAuthenticator;

    @Test
    public void validCreatePaymentRequestShouldResultIn201() throws IOException {
        UserIdAndJwt userIdAndJwt = userAuthenticator.authenticate();

        RestAssured
                .given().baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .header("Authorization", userIdAndJwt.getJwt())
                .header("ServiceAuthorization", serviceTokenGenerator.generate())
                .body(VALID_REQUEST)
                .post("/users/" + userIdAndJwt.getId() + "/payments/")
                .then().statusCode(201);
    }
}
