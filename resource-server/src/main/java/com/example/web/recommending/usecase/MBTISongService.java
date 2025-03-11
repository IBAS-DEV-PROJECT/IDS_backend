package com.example.web.recommending.usecase;

import com.example.web.recommending.domain.MBTISong;
import com.example.web.recommending.repository.MBTISongRepository;
import com.example.web.recommending.dto.MBTISongResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MBTISongService {

	@Autowired
	private MBTISongRepository songRepository;

	public MBTISongResponse getRandomSongsByMBTI(String mbti) {
		List<MBTISong> songs = songRepository.findRandomSongsByMBTI(mbti.toUpperCase());

		List<Map<String, String>> songList = songs.stream()
			.map(song -> Map.of(
				"SID", song.getSpotifyId(),
				"title", song.getTitle(),
				"artist", song.getArtist(),
				"cover_image", song.getCoverImage()
			))
			.collect(Collectors.toList());

		return new MBTISongResponse(mbti, songList);
	}
}
