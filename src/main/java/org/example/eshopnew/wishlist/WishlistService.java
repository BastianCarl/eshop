package org.example.eshopnew.wishlist;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Wishlist> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Wishlist findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found: " + id));
    }

    public Wishlist create(Wishlist wishlist) {
        return repository.save(wishlist);
    }

    public Wishlist update(UUID id, Wishlist wishlist) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Wishlist not found: " + id);
        }
        wishlist.setId(id);
        return repository.save(wishlist);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Wishlist not found: " + id);
        }
        repository.deleteById(id);
    }
}
