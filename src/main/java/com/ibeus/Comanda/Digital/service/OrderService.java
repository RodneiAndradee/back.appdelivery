package com.ibeus.Comanda.Digital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ibeus.Comanda.Digital.model.Order;
import com.ibeus.Comanda.Digital.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAllOrders(Long userId, List<String> status) {
        if (userId != null && status != null && !status.isEmpty()) {
            return orderRepository.findByUserIdAndStatusIn(userId, status);
        } else if (userId != null) {
            return orderRepository.findByUserId(userId);
        } else if (status != null && !status.isEmpty()) {
            return orderRepository.findByStatusIn(status);
        } else {
            return orderRepository.findAll();
        }
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Order update(Long id, Order orderDetails) {
        Order order = findById(id);
        order.setStatus(orderDetails.getStatus());

        // Recalcula o total baseado nos pratos atualizados
        calculateTotalAmount(order);

        return orderRepository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order order = findById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    private void calculateTotalAmount(Order order) {
        if (order.getDishes() == null || order.getDishes().isEmpty()) {
            order.setTotalAmount(0.0);
            return;
        }

        double total = order.getDishes().stream()
                .mapToDouble(dish -> dish.getPrice() != null ? dish.getPrice() : 0.0)
                .sum();

        order.setTotalAmount(total);
    }
}