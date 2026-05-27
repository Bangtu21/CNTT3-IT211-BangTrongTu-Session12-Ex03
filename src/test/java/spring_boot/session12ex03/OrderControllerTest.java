package spring_boot.session12ex03;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import spring_boot.session12ex03.controller.OrderController;
import spring_boot.session12ex03.exception.GlobalExceptionHandler;
import spring_boot.session12ex03.exception.OrderNotFoundException;
import spring_boot.session12ex03.model.Order;
import spring_boot.session12ex03.service.OrderService;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.graalvm.nativeimage.RuntimeOptions.get;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllOrders() throws Exception {

        List<Order> orders = List.of(
                new Order(
                        1L,
                        "Nguyen Van A",
                        "Laptop",
                        2,
                        2000.0
                )
        );

        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName")
                        .value("Nguyen Van A"));
    }

    @Test
    void testGetOrderById_Found() throws Exception {

        Order order = new Order(
                1L,
                "Nguyen Van A",
                "Laptop",
                2,
                2000.0
        );

        when(orderService.findById(1L))
                .thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product")
                        .value("Laptop"));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {

        when(orderService.findById(999L))
                .thenThrow(
                        new OrderNotFoundException(
                                "Order not found"
                        )
                );

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateOrder() throws Exception {

        Order order = new Order(
                1L,
                "Nguyen Van A",
                "Laptop",
                2,
                2000.0
        );

        when(orderService.save(org.mockito.ArgumentMatchers.any(Order.class)))
                .thenReturn(order);

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(order)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
