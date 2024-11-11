package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageResponse {
    public static class LoginMessage {
        public static final String USER_NAME = "Sai tên đăng nhập";
        public static final String USER_HAS_LOCK = "Tài khoản đã bị khóa";
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    }

    public static class TipMessage {
        public static final String SAVED = "Bạn đã lưu bài viết này rồi";
    }

    public static class RegisterMessage {
        public static final String NO_VALID_USER_NAME = "Tên đăng nhập không hợp lệ";
        public static final String USER_NAME_DUPLICATE = "Tên đăng nhập đã tồn tại";
        public static final String WRONG_CONFIRM_PASSWORD = "Xác nhận lại mật khẩu không trùng khớp";
        public static final String WRONG_PASSWORD = "Mật khẩu không đúng";

        public static final String NO_VALID_EMAIL = "Địa chỉ email không hợp lệ";
        public static final String CANT_SEND_EMAIL = "Không gửi được mail";
        public static final String EMAIL_USED = "Địa chỉ email đã được sử dụng";
        public static final String PASSWORD_RETRIEVAL_FAIL = "Địa chỉ email và tên đăng nhập không khớp";
        public static final String PASSWORD_RETRIEVAL = "Mật khẩu mới đã được gửi về địa chỉ email: ";

    }

    public static class Email {
        public static final String WELL_COME = "Xin chào ";
        public static final String THANK = "Cảm ơn bạn đã sử dụng dịch vụ";
        public static final String RESET_PASSWORD = "Cấp lại mật khẩu";
        public static final String CHANGE_PASSWORD = "Đổi mật khẩu";
        public static final String APP = "Fake notebook";
        public static final String SPACE = " ";
        public static final String NEW_PASSWORD = "Mật khẩu mới của bạn là: ";
    }

    public static final String ID_NOT_FOUND = "Không tìm thấy ";
    public static final String DESCRIPTION = "";
    public static final String DESCRIPTION2 = "Không có dữ liệu";
    public static final String LIST_BLANK = "Danh sách trống";
    public static final String DESCRIPTION_REQUEST = "Trường dữ liệu không hợp lệ: ";

    public static final String DESCRIPTION_DUPLICATE = "Trường dữ liệu đã trùng lặp: ";
    public static final String DESCRIPTION_BLANK = "Trường dữ liệu bị trống: ";
    public static final String WRONG_SOME_THING = "Dữ liệu đầu vào không phù hợp";
    public static final String WRONG_NUMBER_PHONE = "Số điện thoại không phù hợp";
    public static final String ERROR = "Có lỗi sảy ra";
    public static final String IN_VALID = "Không hợp lệ";
    public static final String NOT_FOUND_USER = "Người dùng không tồn tại: ";
    public static final String NOT_FOUND_POST = "Bài viết không tồn tại: ";
}
