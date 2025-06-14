package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.service.OnlinePayService;
import com.example.bookingserver.domain.Schedule;
import com.example.bookingserver.domain.repository.ScheduleRepository;
import com.example.bookingserver.infrastructure.config.VNPayConfig;
import com.example.bookingserver.infrastructure.persistence.repository.RedisRepository;
import document.constant.ApplicationConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnlinePayServiceImpl implements OnlinePayService{

    final ScheduleRepository scheduleRepository;
    final RedisRepository redisRepository;
    final String PREFIX="VNPayID: ";
    @Value("${vnpay.return.url}")
    String vnp_ReturnUrl;



    @Override
    @SneakyThrows
    public String payCart(String scheduleId, HttpServletRequest req) {
        Schedule schedule= scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new RuntimeException("Hệ thống thanh toán đang được bảo trì")
        );
        String orderType = "other";
        Integer amount = schedule.getSpecialize().getPrice() * 100;
        String vnp_TxnRef = VNPayConfig.getRandomNumber() + "";
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán gói khám " + schedule.getSpecialize().getName());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr" , vnp_IpAddr) ;
        vnp_Params.put("vnp_OrderType", orderType);


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        redisRepository.set(PREFIX + vnp_TxnRef, scheduleId);
        redisRepository.setTimeToLive(PREFIX + vnp_TxnRef, 1000 * 60 * 15L);
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }


    @SneakyThrows
    @Override
    public String extractPay(Map<String, String> params, HttpServletRequest request) {
        String value = "";
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        Map<String, String> hashData = new HashMap<>();
        for(String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());
                hashData.put(fieldName, fieldValue);
            }
        }
        hashData.remove("vnp_SecureHashType");
        String receivedHash = hashData.remove("vnp_SecureHash");
        String signValue = VNPayConfig.hashAllFields(hashData);
        if (signValue.equals(receivedHash)) {
            String scheduleId = redisRepository.get(PREFIX + params.get("vnp_TxnRef")).toString();
            Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                    () -> new RuntimeException("Lỗi thanh toán")
            );
            String vnp_ResponseCode = params.get("vnp_ResponseCode");
            if(vnp_ResponseCode.equals("00")){
                schedule.setPaymentStatus(ApplicationConstant.PaymentMethod.CREDIT);
                scheduleRepository.save(schedule);
                value = "Thanh toán thành công!";
            }
            if(vnp_ResponseCode.equals("11"))
                value = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            if(vnp_ResponseCode.equals("12"))
                value = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            if(vnp_ResponseCode.equals("13"))
                value = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
            if(vnp_ResponseCode.equals("24"))
                value = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            if(vnp_ResponseCode.equals("51"))
                value = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            if(vnp_ResponseCode.equals("65"))
                value = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            if(vnp_ResponseCode.equals("75"))
                value = "Ngân hàng thanh toán đang bảo trì.";
            if(vnp_ResponseCode.equals("79"))
                value = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
        }else{
            log.warn("Giao dịch giả mạo từ IP: {}", request.getRemoteAddr());
            value = "Giao dịch không hợp lệ, vui lòng kết nối để biết thêm chi tiết";
        }
        return value;
    }
}
