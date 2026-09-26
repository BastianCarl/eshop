#!/usr/bin/env bash
# Wishlists CRUD via curl. Copy any single line into bash.
# Replace <WISHLIST_ID>, <USER_ID> with real ids.

# Get all wishlists
curl -s http://localhost:8080/api/wishlists

# Get wishlist by id
curl -s http://localhost:8080/api/wishlists/<WISHLIST_ID>

# Create wishlist
curl -s -X POST http://localhost:8080/api/wishlists -H "Content-Type: application/json" -d '{"user":{"id":"<USER_ID>"},"name":"My Wishlist"}'

# Update wishlist
curl -s -X PUT http://localhost:8080/api/wishlists/<WISHLIST_ID> -H "Content-Type: application/json" -d '{"user":{"id":"<USER_ID>"},"name":"Birthday Wishlist"}'

# Delete wishlist
curl -s -X DELETE http://localhost:8080/api/wishlists/<WISHLIST_ID>
