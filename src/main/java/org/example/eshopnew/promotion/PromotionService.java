package org.example.eshopnew.promotion;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PromotionService {

    private final PromotionRepository repository;

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Promotion> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Promotion findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found: " + id));
    }

    public Promotion create(Promotion promotion) {
        return repository.save(promotion);
    }

    public Promotion update(UUID id, Promotion promotion) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Promotion not found: " + id);
        }
        promotion.setId(id);
        return repository.save(promotion);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Promotion not found: " + id);
        }
        repository.deleteById(id);
    }
}
