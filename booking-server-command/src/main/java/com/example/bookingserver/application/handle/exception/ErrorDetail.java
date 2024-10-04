package com.example.bookingserver.application.handle.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email đã tồn tại")
    , ERR_USER_NOT_EXISTED(404, "Không tìm thấy người dùng")
    , ERR_USER_UN_AUTHENTICATE(401, "Không thể xác thực người dùng")
    , ERR_PASSWORD_NOT_CONFIRM(400, "Mật khẩu xác nhận chưa đúng")
    , ERR_PASSWORD_NOT_CORRECT(400, "Mật khẩu cũ chưa chính xác")
    , ERR_FILE(400, "File ảnh đang có lỗi, chưa thể lưu")
    ;

    private final int code;
    private final String massage;

    ErrorDetail(int code, String massage) {
        this.code = code;
        this.massage = massage;
    }
}
