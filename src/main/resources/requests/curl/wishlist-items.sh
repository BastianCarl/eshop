#!/usr/bin/env bash
# Wishlist items CRUD via curl. Copy any single line into bash.
# Replace <WISHLIST_ITEM_ID>, <WISHLIST_ID>, <PRODUCT_ID> with real ids.

# Get all wishlist items
curl -s http://localhost:8080/api/wishlist-items

# Get wishlist item by id
curl -s http://localhost:8080/api/wishlist-items/<WISHLIST_ITEM_ID>

# Create wishlist item
curl -s -X POST http://localhost:8080/api/wishlist-items -H "Content-Type: application/json" -d '{"wishlist":{"id":"<WISHLIST_ID>"},"product":{"id":"<PRODUCT_ID>"},"addedAt":"2026-09-21T10:00:00"}'

# Update wishlist item
curl -s -X PUT http://localhost:8080/api/wishlist-items/<WISHLIST_ITEM_ID> -H "Content-Type: application/json" -d '{"wishlist":{"id":"<WISHLIST_ID>"},"product":{"id":"<PRODUCT_ID>"},"addedAt":"2026-09-22T12:00:00"}'

# Delete wishlist item
curl -s -X DELETE http://localhost:8080/api/wishlist-items/<WISHLIST_ITEM_ID>
