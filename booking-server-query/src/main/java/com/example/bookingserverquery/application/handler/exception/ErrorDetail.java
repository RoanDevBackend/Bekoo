package com.example.bookingserverquery.application.handler.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email đã tồn tại")
    , ERR_USER_NOT_EXISTED(404, "Không tìm thấy người dùng")
    , ERR_USER_UN_AUTHENTICATE(401, "Không thể xác thực người dùng")
    , ERR_PASSWORD_NOT_CONFIRM(400, "Mật khẩu xác nhận chưa đúng")
    , ERR_PASSWORD_NOT_CORRECT(400, "Mật khẩu cũ chưa chính xác")
    , ERR_FILE(400, "File ảnh đang có lỗi, chưa thể lưu")
    , ERR_DEPARTMENT_NOT_EXISTED(404, "Không tồn tại chuyên khoa này")
    , ERR_SPECIALIZE_NOT_EXISTED(404, "Không tồn tại gói khám này")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
