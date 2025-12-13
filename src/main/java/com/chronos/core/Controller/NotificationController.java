package com.chronos.core.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronos.Entity.NotificationEndPoint;
import com.chronos.Repository.NotificationEndpointRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
	private final NotificationEndpointRepository notificationEndPointRepo;
	
	@PostMapping
	public ResponseEntity<NotificationEndPoint> saveEndPoint(@RequestBody NotificationEndPoint endPoint ){
		NotificationEndPoint saved=notificationEndPointRepo.save(endPoint);
		return ResponseEntity.ok(saved);
	}
	@GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationEndPoint>>getUserEndpoints(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationEndPointRepo.findByUserId(userId));
    }

}
