#!/usr/bin/env bash
# Order items CRUD via curl. Copy any single line into bash.
# Replace <ORDER_ITEM_ID>, <ORDER_ID>, <PRODUCT_ID> with real ids.

# Get all order items
curl -s http://localhost:8080/api/order-items

# Get order item by id
curl -s http://localhost:8080/api/order-items/<ORDER_ITEM_ID>

# Create order item
curl -s -X POST http://localhost:8080/api/order-items -H "Content-Type: application/json" -d '{"order":{"id":"<ORDER_ID>"},"product":{"id":"<PRODUCT_ID>"},"quantity":2,"unitPrice":29.99}'

# Update order item
curl -s -X PUT http://localhost:8080/api/order-items/<ORDER_ITEM_ID> -H "Content-Type: application/json" -d '{"order":{"id":"<ORDER_ID>"},"product":{"id":"<PRODUCT_ID>"},"quantity":3,"unitPrice":27.99}'

# Delete order item
curl -s -X DELETE http://localhost:8080/api/order-items/<ORDER_ITEM_ID>
