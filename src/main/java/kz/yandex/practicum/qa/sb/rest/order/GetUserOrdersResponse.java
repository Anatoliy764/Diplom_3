package kz.yandex.practicum.qa.sb.rest.order;

import kz.yandex.practicum.qa.sb.rest.common.CommonApiResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetUserOrdersResponse extends CommonApiResponse {

    List<Order> orders;

    int total;

    int totalToday;

    public GetUserOrdersResponse() {
        orders = new LinkedList<>();
    }
}
