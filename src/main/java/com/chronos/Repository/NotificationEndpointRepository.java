package com.chronos.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chronos.Entity.NotificationEndPoint;



@Repository
public interface NotificationEndpointRepository extends JpaRepository<NotificationEndPoint,Long> {
List<NotificationEndPoint>findByUserId(Long userId);
}
