package com.chronos.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class NotificationEndPoint {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long userId;
private String type;
private String target;
private boolean active=true;
private String endpointUrl;
 private String endpointType;

}
