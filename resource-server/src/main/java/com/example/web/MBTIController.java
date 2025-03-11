package com.example.web;

import com.example.web.recommending.usecase.MBTISongService;
import com.example.web.recommending.dto.MBTISongResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class MBTIController {

	@Autowired
	private MBTISongService songService;

	@GetMapping
	public ResponseEntity<Object> getMBTISongs(@RequestParam(required = false) String mbti) {
		if (mbti == null || mbti.length() != 4) {
			return ResponseEntity.badRequest().body(Map.of(
				"status", 400,
				"message", "잘못된 요청입니다."
			));
		}

		MBTISongResponse response = songService.getRandomSongsByMBTI(mbti);
		return ResponseEntity.ok(response);
	}
}
