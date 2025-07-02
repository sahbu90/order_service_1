package com.example.demo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
}

