package com.hn.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hn.common.HnApiConstants;
import com.hn.exceptions.NoRecordFoundException;
import com.hn.model.Story;
import com.hn.model.TopComment;
import com.hn.service.HnApiService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
public class HnApiController {


	@Autowired
	private HnApiService hnApiService;


	@GetMapping("/top-stories")
	public List<Story> getTopStories() {
		List<Story> topStories = hnApiService.getTopStories();
		if (topStories.isEmpty()) {
			throw new NoRecordFoundException(HnApiConstants.NO_STORY_FOUND_MESSAGE);
		}

		return topStories;
	}


	@GetMapping("/comments/{storyId}")
	public List<TopComment> getTopComments(@PathVariable int storyId) {
		List<TopComment> topComments = hnApiService.getCommentsById(storyId).stream().limit(10)
				.collect(Collectors.toList());

		if (topComments.isEmpty()) {
			throw new NoRecordFoundException(HnApiConstants.NO_COMMENT_FOUND_MESSAGE);
		}

		return topComments;
	}


	@GetMapping("/past-stories")
	public Set<Story> getPastStories() {
		Set<Story> pastStories = hnApiService.getPastTopStories();
		if (pastStories.isEmpty()) {
			throw new NoRecordFoundException(HnApiConstants.NO_STORY_FOUND_MESSAGE);
		}
		return pastStories;
	}
}
