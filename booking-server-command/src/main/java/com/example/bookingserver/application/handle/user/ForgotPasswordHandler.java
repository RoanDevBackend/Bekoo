package com.example.bookingserver.application.handle.user;

import com.example.bookingserver.application.command.user.ForgotPasswordCommand;
import com.example.bookingserver.application.handle.Handler;
import com.example.bookingserver.application.handle.exception.BookingCareException;
import com.example.bookingserver.application.handle.exception.ErrorDetail;
import com.example.bookingserver.application.service.MessageService;
import com.example.bookingserver.domain.User;
import com.example.bookingserver.domain.repository.UserRepository;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordHandler implements Handler<ForgotPasswordCommand> {

    final UserRepository userRepository;
    final RedisRepository redisRepository;
    final MessageService messageService;
    @Override
    @SneakyThrows
    public void execute(ForgotPasswordCommand command) {
        User user= userRepository.findByEmail(command.getEmail())
                    .orElseThrow(() -> new BookingCareException(ErrorDetail.ERR_USER_NOT_EXISTED));
        Random random= new Random();
        int code= random.nextInt(100000, 999999);
        String content= "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email OTP</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f2f2f2;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 30px auto;\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 8px 8px 0 0;\n" +
                "        }\n" +
                "        .otp {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            color: #4CAF50;\n" +
                "            margin: 20px 0;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            font-size: 12px;\n" +
                "            color: #666;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "        a {\n" +
                "            color: #4CAF50;\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Bekoo xin chào quý khách</h1>\n" +
                "        </div>\n" +
                "        <p>Xin chào,</p>\n" +
                "        <p>Cảm ơn bạn đã sử dụng dịch vụ. Dưới đây là mã OTP của bạn để lấy lại mật khẩu:</p>\n" +
                "        <div class=\"otp\">" + code + "\n" +
                "        <p>Vui lòng nhập mã OTP này trong ứng dụng của chúng tôi để hoàn tất quy trình lấy lại mật khẩu.</p>\n" +
                "        <p>Nếu bạn không yêu cầu mã OTP này, vui lòng bỏ qua email này.</p>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Trân trọng,<br>Đội ngũ hỗ trợ khách hàng</p>\n" +
                "            <p><a href=\"#\">Tìm hiểu thêm về chúng tôi</a></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>" ;
        messageService.sendMail(command.getEmail(), content, true);
        redisRepository.set(user.getEmail(), code);
        redisRepository.setTimeToLive(user.getEmail(), 60L);
    }
}
