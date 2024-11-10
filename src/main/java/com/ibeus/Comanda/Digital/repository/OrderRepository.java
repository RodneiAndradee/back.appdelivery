package com.ibeus.Comanda.Digital.repository;

import com.ibeus.Comanda.Digital.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.id DESC")
  List<Order> findByUserId(@Param("userId") Long userId);

  @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status IN :status ORDER BY o.id DESC")
  List<Order> findByUserIdAndStatusIn(@Param("userId") Long userId, @Param("status") List<String> status);

  @Query("SELECT o FROM Order o WHERE o.status IN :status ORDER BY o.id DESC")
  List<Order> findByStatusIn(@Param("status") List<String> status);
}