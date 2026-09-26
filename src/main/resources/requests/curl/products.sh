#!/usr/bin/env bash
# Products CRUD via curl. Copy any single line into bash. Replace <PRODUCT_ID> with a real id.

# Get all products
curl -s http://localhost:8080/api/products

# Get product by id
curl -s http://localhost:8080/api/products/<PRODUCT_ID>

# Create product
curl -s -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d '{"name":"Wireless Mouse","price":29.99,"stock":100}'

# Update product
curl -s -X PUT http://localhost:8080/api/products/<PRODUCT_ID> -H "Content-Type: application/json" -d '{"name":"Wireless Mouse Pro","price":39.99,"stock":80}'

# Delete product
curl -s -X DELETE http://localhost:8080/api/products/<PRODUCT_ID>
