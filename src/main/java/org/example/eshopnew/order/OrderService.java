package org.example.eshopnew.order;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }

    public Order create(Order order) {
        return repository.save(order);
    }

    public Order update(UUID id, Order order) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Order not found: " + id);
        }
        order.setId(id);
        return repository.save(order);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Order not found: " + id);
        }
        repository.deleteById(id);
    }
}
