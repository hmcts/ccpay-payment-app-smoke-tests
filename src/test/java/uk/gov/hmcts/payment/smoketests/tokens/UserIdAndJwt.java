package uk.gov.hmcts.payment.smoketests.tokens;


public class UserIdAndJwt { /* Tarun */
    private final Integer id;
    private final String jwt;

    public UserIdAndJwt(Integer id, String jwt) {
        this.id = id;
        this.jwt = jwt;
    }

    public Integer getId() {
        return id;
    }

    public String getJwt() {
        return jwt;
    }
}
