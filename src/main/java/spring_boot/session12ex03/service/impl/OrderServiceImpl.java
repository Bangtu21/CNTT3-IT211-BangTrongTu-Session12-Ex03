package spring_boot.session12ex03.service.impl;

import org.springframework.stereotype.Service;
import spring_boot.session12ex03.exception.OrderNotFoundException;
import spring_boot.session12ex03.model.Order;
import spring_boot.session12ex03.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {
    private final List<Order> orders = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public List<Order> findAll() {
        return orders;
    }

    @Override
    public Order findById(Long id) {

        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found"));
    }

    @Override
    public Order save(Order order) {

        order.setId(idGenerator.incrementAndGet());

        orders.add(order);

        return order;
    }

    @Override
    public Order update(Long id, Order order) {

        Order oldOrder = findById(id);

        oldOrder.setCustomerName(order.getCustomerName());
        oldOrder.setProduct(order.getProduct());
        oldOrder.setQuantity(order.getQuantity());
        oldOrder.setTotalAmount(order.getTotalAmount());

        return oldOrder;
    }

    @Override
    public void delete(Long id) {

        Order order = findById(id);

        orders.remove(order);
    }
}
