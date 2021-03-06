package study.microcoffee.order.rest.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import study.microcoffee.order.SecurityConfig;
import study.microcoffee.order.consumer.creditrating.CreditRatingConsumer;
import study.microcoffee.order.domain.DrinkType;
import study.microcoffee.order.domain.Order;
import study.microcoffee.order.repository.OrderRepository;

/**
 * Unit tests of {@link OrderRestService}.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(OrderRestService.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = { "logging.level.study.microcoffee=DEBUG" })
public class OrderRestServiceTest {

    private static final String POST_SERVICE_PATH = "/coffeeshop/{coffeeShopId}/order";
    private static final String GET_SERVICE_PATH = "/coffeeshop/{coffeeShopId}/order/{orderId}";

    private static final int COFFEE_SHOP_ID = 10;

    @MockBean
    private OrderRepository orderRepositoryMock;

    @MockBean
    @Qualifier("Hystrix")
    private CreditRatingConsumer creditRatingCustomerMock;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles={"USER"})
    public void saveOrderShouldReturnCreated() throws Exception {
        Order newOrder = new Order.Builder() //
            .type(new DrinkType("Latte", "Coffee")) //
            .size("Small") //
            .drinker("Dagbjørn") //
            .selectedOption("skimmed milk") //
            .build();

        Order savedOrder = new Order.Builder() //
            .order(newOrder) //
            .id("1234567890abcdf") //
            .build();

        given(orderRepositoryMock.save(any(Order.class))).willReturn(savedOrder);
        given(creditRatingCustomerMock.getCreditRating(anyString())).willReturn(70);

        mockMvc.perform(post(POST_SERVICE_PATH, COFFEE_SHOP_ID) //
            .content(toJson(newOrder)) //
            .cookie(new Cookie("JSESSIONID", "abcdef")) //
            .contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isCreated()) //
            .andExpect(header().string(HttpHeaders.LOCATION, Matchers.endsWith(savedOrder.getId())))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(content().json(toJson(savedOrder)));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void saveOrderWhenCreditRatingIsBadShouldReturnPaymentCreated() throws Exception {
        Order newOrder = new Order.Builder() //
            .type(new DrinkType("Latte", "Coffee")) //
            .size("Small") //
            .drinker("Poor Sod") //
            .selectedOption("skimmed milk") //
            .build();

        given(creditRatingCustomerMock.getCreditRating(anyString())).willReturn(20);

        mockMvc.perform(post(POST_SERVICE_PATH, COFFEE_SHOP_ID) //
            .content(toJson(newOrder)) //
            .cookie(new Cookie("JSESSIONID", "abcdef")) //
            .contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isPaymentRequired()) //
            .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void getOrderShouldReturnOrder() throws Exception {
        Order expectedOrder = new Order.Builder() //
            .id("1234567890abcdef") //
            .type(new DrinkType("Americano", "Coffee")) //
            .size("Large") //
            .drinker("Dagbjørn") //
            .selectedOption("soy milk") //
            .build();

        given(orderRepositoryMock.findById(eq(expectedOrder.getId()))).willReturn(Optional.of(expectedOrder));

        mockMvc.perform(get(GET_SERVICE_PATH, COFFEE_SHOP_ID, expectedOrder.getId()) //
            .accept(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isOk()) //
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(content().json(toJson(expectedOrder)));
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void getOrderWhenNoOrderShouldReturnNoContent() throws Exception {
        String orderId = "1111111111111111";

        given(orderRepositoryMock.findById(eq(orderId))).willReturn(Optional.empty());

        mockMvc.perform(get(GET_SERVICE_PATH, COFFEE_SHOP_ID, orderId) //
            .accept(MediaType.APPLICATION_JSON_UTF8)) //
            .andExpect(status().isNoContent());
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
