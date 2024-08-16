package com.crypto.investment.price.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides CRUD operations for the {@link Price} entity.
 */
public interface PriceRepository extends JpaRepository<Price, Long> {

}
