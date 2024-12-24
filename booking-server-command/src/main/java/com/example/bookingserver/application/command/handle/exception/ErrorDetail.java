package com.example.bookingserver.application.command.handle.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email đã tồn tại")
    , ERR_USER_NOT_EXISTED(404, "Không tìm thấy người dùng")
    , ERR_USER_UN_AUTHENTICATE(401, "Không thể xác thực người dùng")
    , ERR_PASSWORD_NOT_CONFIRM(400, "Mật khẩu xác nhận chưa đúng")
    , ERR_PASSWORD_NOT_CORRECT(400, "Mật khẩu cũ chưa chính xác")
    , ERR_FILE(400, "File ảnh đang có lỗi, chưa thể lưu")

    , ERR_DOCTOR_EXISTED(400, "Thông tin bác sĩ cho người dùng này đã tồn tại")
    , ERR_DOCTOR_PERSON(400, "Bác sĩ đã đạt giới hạn lịch khám trong ngày")
    , ERR_DOCTOR_NOT_EXISTED(404, "Không tồn tại bác sĩ này")

    , ERR_DEPARTMENT_NOT_EXISTED(404, "Không tồn tại chuyên khoa này")

    , ERR_SPECIALIZE_NOT_EXISTED(404, "Chuyên ngành không tồn tại")

    , ERR_PATIENT_NOT_EXISTED(404, "Bệnh nhân không tồn tại")

    , ERR_SCHEDULE_DATE(400, "Ngày đặt không hợp lệ")
    ,ERR_INVALID_FILE_TYPE(400, "Chỉ được phép là hình ảnh")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
