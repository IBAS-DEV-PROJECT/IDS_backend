package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppTest {
	@Test
	void contextLoads() {
		// 기본적인 Spring Context 로드 테스트
		assertTrue(true);
	}

	@Test
	void mainClassExists() {
		// Main 클래스 존재 여부 테스트
		assertNotNull(new Main());
	}
}
