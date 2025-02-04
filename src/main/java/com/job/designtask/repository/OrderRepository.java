package com.job.designcodingtask.repository;

import com.job.designcodingtask.model.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderRequest, UUID> {
}