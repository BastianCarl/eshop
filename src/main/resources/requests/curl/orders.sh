#!/usr/bin/env bash
# Orders CRUD via curl. Copy any single line into bash.
# Replace <ORDER_ID>, <USER_ID>, <PROMOTION_ID> with real ids.

# Get all orders
curl -s http://localhost:8080/api/orders

# Get order by id
curl -s http://localhost:8080/api/orders/<ORDER_ID>

# Create order
curl -s -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d '{"user":{"id":"<USER_ID>"},"promotion":{"id":"<PROMOTION_ID>"},"status":"PENDING","total":149.99,"createdAt":"2026-09-21T10:00:00"}'

# Update order
curl -s -X PUT http://localhost:8080/api/orders/<ORDER_ID> -H "Content-Type: application/json" -d '{"user":{"id":"<USER_ID>"},"promotion":{"id":"<PROMOTION_ID>"},"status":"PAID","total":149.99,"createdAt":"2026-09-21T10:00:00"}'

# Delete order
curl -s -X DELETE http://localhost:8080/api/orders/<ORDER_ID>
