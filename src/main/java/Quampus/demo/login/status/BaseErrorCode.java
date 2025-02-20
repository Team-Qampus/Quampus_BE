package Quampus.demo.login.status;

import Quampus.demo.login.dto.ErrorReasonDTO;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
