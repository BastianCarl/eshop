package org.example.eshopnew.order;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public OrderItem findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found: " + id));
    }

    public OrderItem create(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    public OrderItem update(UUID id, OrderItem orderItem) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem not found: " + id);
        }
        orderItem.setId(id);
        return repository.save(orderItem);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem not found: " + id);
        }
        repository.deleteById(id);
    }
}
