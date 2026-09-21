package org.example.eshopnew.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {
}
