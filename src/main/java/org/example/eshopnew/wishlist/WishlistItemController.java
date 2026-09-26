package org.example.eshopnew.wishlist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist-items")
public class WishlistItemController {

    private final WishlistItemService service;

    public WishlistItemController(WishlistItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<WishlistItem> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public WishlistItem findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistItem create(@RequestBody WishlistItem wishlistItem) {
        return service.create(wishlistItem);
    }

    @PutMapping("/{id}")
    public WishlistItem update(@PathVariable UUID id, @RequestBody WishlistItem wishlistItem) {
        return service.update(id, wishlistItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
