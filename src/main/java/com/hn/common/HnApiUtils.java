package com.hn.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import com.hn.model.Comment;
import com.hn.model.Story;
import com.hn.model.User;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.RestTemplate;


@Log4j2
public class HnApiUtils {

	private static final RestTemplate template = new RestTemplate();

	public static List<Integer> getTopStoryIds() {
		List<Integer> topStories;
		StringBuilder builder = new StringBuilder();
		builder.append(HnApiConstants.HACKER_NEWS_API_BASE_URL).append(HnApiConstants.TOP_STORIES_END_POINT)
				.append(HnApiConstants.ENDPOINT_SUFFIX);
		topStories = Arrays.asList(template.getForObject(builder.toString(), Integer[].class));
		return topStories;
	}

	public static Story getStory(int storyId) {
		StringBuilder builder = new StringBuilder();
		builder.append(HnApiConstants.HACKER_NEWS_API_BASE_URL).append(HnApiConstants.ITEM_END_POINT)
				.append(storyId).append(HnApiConstants.ENDPOINT_SUFFIX);
		return template.getForObject(builder.toString(), Story.class);
	}

	public static Comment getComment(int commentId) {
		StringBuilder builder = new StringBuilder();
		builder.append(HnApiConstants.HACKER_NEWS_API_BASE_URL).append(HnApiConstants.ITEM_END_POINT)
				.append(commentId).append(HnApiConstants.ENDPOINT_SUFFIX);
		return template.getForObject(builder.toString(), Comment.class);
	}

	public static int getSubCommentCount(Comment comment) {
		int count = 0;
		if (comment.getKids() != null) {
			count += comment.getKids().length;
		}
		return count;
	}


	public static int getUserAgeByName(String name) {
		StringBuilder builder = new StringBuilder();
		builder.append(HnApiConstants.HACKER_NEWS_API_BASE_URL).append(HnApiConstants.USER_END_POINT)
				.append(name).append(HnApiConstants.ENDPOINT_SUFFIX);
		User user = template.getForObject(builder.toString(), User.class);
		if (user != null) {
			return Period.between(
					LocalDate.ofInstant(Instant.ofEpochMilli(user.getCreated() * 1000L), ZoneId.systemDefault()),
					LocalDate.now()).getYears();
		}
		return 0;
	}

}
