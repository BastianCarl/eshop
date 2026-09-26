#!/usr/bin/env bash
# Users CRUD via curl. Copy any single line into bash. Replace <USER_ID> with a real id.

# Get all users
curl -s http://localhost:8080/api/users

# Get user by id
curl -s http://localhost:8080/api/users/<USER_ID>

# Create user
curl -s -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{"email":"john.doe@example.com","name":"John Doe"}'

# Update user
curl -s -X PUT http://localhost:8080/api/users/<USER_ID> -H "Content-Type: application/json" -d '{"email":"john.updated@example.com","name":"John Updated"}'

# Delete user
curl -s -X DELETE http://localhost:8080/api/users/<USER_ID>
