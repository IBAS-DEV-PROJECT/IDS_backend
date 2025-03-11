package com.example.web.recommending.dto;

import java.util.List;
import java.util.Map;

public class MBTISongResponse {
	private String mbti;
	private List<Map<String, String>> recommendedTracks;

	public MBTISongResponse(String mbti, List<Map<String, String>> recommendedTracks) {
		this.mbti = mbti;
		this.recommendedTracks = recommendedTracks;
	}

	public String getMbti() {
		return mbti;
	}

	public List<Map<String, String>> getRecommendedTracks() {
		return recommendedTracks;
	}
}
