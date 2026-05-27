package spring_boot.session12ex03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring_boot.session12ex03.exception.OrderNotFoundException;
import spring_boot.session12ex03.model.Order;
import spring_boot.session12ex03.service.impl.OrderServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {

        orderService = new OrderServiceImpl();

        orderService.save(
                new Order(
                        null,
                        "Nguyen Van A",
                        "Laptop",
                        2,
                        2000.0
                )
        );
    }

    @Test
    void getAllOrders_ReturnNonEmptyList() {

        List<Order> orders = orderService.findAll();

        assertFalse(orders.isEmpty());
    }

    @Test
    void getOrderById_Found() {

        Order order = orderService.findById(1L);

        assertNotNull(order);

        assertEquals("Laptop", order.getProduct());
    }

    @Test
    void getOrderById_NotFound_ThrowException() {

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findById(999L)
        );
    }

    @Test
    void addOrder_Success() {

        Order order = new Order(
                null,
                "Bàng TRọng Tú",
                "Phone",
                1,
                1000.0
        );

        Order savedOrder = orderService.save(order);

        assertNotNull(savedOrder.getId());

        assertEquals(2, orderService.findAll().size());
    }

    @Test
    void updateOrder_Success() {

        Order updatedOrder = new Order(
                null,
                "Updated Name",
                "Tablet",
                3,
                3000.0
        );

        Order result = orderService.update(1L, updatedOrder);

        assertEquals("Updated Name", result.getCustomerName());

        assertEquals("Tablet", result.getProduct());
    }

    @Test
    void deleteOrder_RemovesElement() {

        orderService.delete(1L);

        assertEquals(0, orderService.findAll().size());
    }
}
