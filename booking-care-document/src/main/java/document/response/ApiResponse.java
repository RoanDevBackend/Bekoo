package document.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    int code;
    String message;
    String messageError;
    Object value;

    public static ApiResponse success(int code, String message){
        return new ApiResponse(code, message, null, null);
    }
    public static ApiResponse success(int code, String message, Object value){
        return new ApiResponse(code, message, null, value);
    }

    public static ApiResponse error(String message){
        return new ApiResponse(400, message, null, null);
    }
    public static ApiResponse error(int code, String message){
        return new ApiResponse(code, message, null, null);
    }
    public static ApiResponse error(int code, String message, String messageError){
        return new ApiResponse(code, message, messageError, null);
    }
}
