package com.example.bookingserverquery.application.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QueryBase {
    @Builder.Default
            @Min(value = 1, message = "Số trang không được nhỏ hơn 1")
    int pageIndex = 1;
    @Builder.Default
    int pageSize = 10000;

    @Builder.Default
    List<OrderDTO> orders= new ArrayList<>();

    @JsonIgnore
    public Pageable getPageable() {
        List<Sort.Order> sortOrder = new ArrayList<>();
        for (OrderDTO x : orders) {
            sortOrder.add(new Sort.Order(x.getDirection(), x.getProperties()));
        }
        Sort sort = Sort.by(sortOrder);
        return org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize, sort);
    }
}

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
class OrderDTO{
    String properties ;
    @Builder.Default
    Sort.Direction direction= Sort.Direction.ASC ;
}
