#!/usr/bin/env bash
# Promotions CRUD via curl. Copy any single line into bash. Replace <PROMOTION_ID> with a real id.

# Get all promotions
curl -s http://localhost:8080/api/promotions

# Get promotion by id
curl -s http://localhost:8080/api/promotions/<PROMOTION_ID>

# Create promotion
curl -s -X POST http://localhost:8080/api/promotions -H "Content-Type: application/json" -d '{"code":"SUMMER25","discountType":"PERCENTAGE","value":25.00,"validUntil":"2026-12-31T23:59:59"}'

# Update promotion
curl -s -X PUT http://localhost:8080/api/promotions/<PROMOTION_ID> -H "Content-Type: application/json" -d '{"code":"SUMMER30","discountType":"PERCENTAGE","value":30.00,"validUntil":"2027-01-31T23:59:59"}'

# Delete promotion
curl -s -X DELETE http://localhost:8080/api/promotions/<PROMOTION_ID>
