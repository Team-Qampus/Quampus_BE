package Quampus.demo.login.status.handler;

import Quampus.demo.login.status.ErrorStatus;
import lombok.Getter;

@Getter
public class OauthHandler extends RuntimeException {

    private final ErrorStatus errorStatus;

    public OauthHandler(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
