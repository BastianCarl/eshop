package org.example.eshopnew.wishlist;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WishlistItemService {

    private final WishlistItemRepository repository;

    public WishlistItemService(WishlistItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<WishlistItem> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public WishlistItem findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("WishlistItem not found: " + id));
    }

    public WishlistItem create(WishlistItem wishlistItem) {
        return repository.save(wishlistItem);
    }

    public WishlistItem update(UUID id, WishlistItem wishlistItem) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("WishlistItem not found: " + id);
        }
        wishlistItem.setId(id);
        return repository.save(wishlistItem);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("WishlistItem not found: " + id);
        }
        repository.deleteById(id);
    }
}
