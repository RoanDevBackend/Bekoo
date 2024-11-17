package com.example.bookingserver.application.command.service.impl;

import com.example.bookingserver.application.command.command.doctor.CreateDoctorCommand;
import com.example.bookingserver.application.command.handle.doctor.CreateDoctorHandler;
import com.example.bookingserver.application.command.service.GPTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@EnableAsync
@RequiredArgsConstructor
public class GPTServiceImpl implements GPTService {

    final ObjectMapper objectMapper = new ObjectMapper();
    final CreateDoctorHandler createDoctorHandler;
    @Value("${api.key}")
    private static String API_KEY;

    @Override
    @SneakyThrows
    @Async
    public void addDoctor(int total) {

        for(int i =0 ; i < total ; i++) {
            while (true) {
                try {
                    String content = "Yêu cầu đặc biệt, tạo ra đoạn dữ liệu JSON, không cần phản hồi thêm bất cứ thứ gì khác" +
                            "Hãy tạo ra cho tôi thông tin bác sĩ Việt Nam gần giống có thật với các thuộc tính: " +
                            "   String trainingBy; \n" +
                            "    String description;\n" +
                            "    CreateUserCommand user; " +
                            "    Đây là lớp CreateUserCommand:  public class CreateUserCommand {\n" +
                            "    String name;\n" +
                            "    String phoneNumber;\n" +
                            "    String email;\n" +
                            "    String cccd;\n" +
                            "    String province;\n" +
                            "    String district;\n" +
                            "    String commune;\n" +
                            "    String aboutAddress;\n" +
                            "    String password;\n" +
                            "    String confirmPassword;\n" +
                            "    String dob; yêu cầu định dạng 'yy-mm-dd'\n" +

                            "    String gender;\n" +
                            " Dữ liệu trả về yêu cầu dưới dạng Json theo định dạng như sau . Ví dụ: " +
                            "{\n" +
                            "    \"user\": {\n" +
                            "        \"name\" : \"Lê Trọng Hiếu\"\n" +
                            "        , \"phoneNumber\" : \"0395279591\"\n" +
                            "        , \"email\" : \"hieu201499@gmail.com\"\n" +
                            "        , \"cccd\": \"036204018701\"\n" +
                            "        , \"province\" : \"Lào Cai\"\n" +
                            "        , \"district\" : \"Thành Phố Lào Cai\"\n" +
                            "        , \"commune\":  \"Phường phủ lí\"\n" +
                            "        , \"aboutAddress\" : \"Cong truong cap 2\"\n" +
                            "        , \"password\" : \"1234\"\n" +
                            "        , \"confirmPassword\": \"1234\"\n" +
                            "        , \"dob\" : \"2004-12-24\"\n" +
                            "        , \"gender\" : \"nam\"\n" +
                            "        },\n" +
                            "    \"description\": \"Là người nhiệt tình, vui vẻ hoà đồng, hay giúp đỡ người khác. Sống theo châm nghĩa 'Lương y như từ mẫu'\",\n" +
                            "    \"trainingBy\": \"Thạc sĩ\"\n" +
                            "}" +
                            "ngoài ra không còn cần thêm gì nữa";

                    OpenAiService openAiService = new OpenAiService(API_KEY);

                    List<ChatMessage> messages = new ArrayList<>();
                    messages.add(new ChatMessage("user", content));
                    ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                            .messages(messages)
                            .model("gpt-3.5-turbo")
                            .build();
                    String response = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
                    CreateDoctorCommand createDoctorCommand = objectMapper.readValue(response, CreateDoctorCommand.class);
                    createDoctorHandler.execute(createDoctorCommand);
                    break;
                } catch (JsonProcessingException e) {
                    System.out.println("Dữ liệu này không hợp lệ, cần sửa lại");
                }
            }
        }
    }
}
