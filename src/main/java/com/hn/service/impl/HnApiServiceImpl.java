package com.hn.service.impl;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hn.common.HnApiConstants;
import com.hn.common.HnApiUtils;
import com.hn.model.Comment;
import com.hn.model.Story;
import com.hn.model.TopComment;
import com.hn.service.HnApiService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class HnApiServiceImpl implements HnApiService {

	private static Set<Story> pastStorySet = new HashSet<>();

	@Cacheable(value = "top_story_cache")
	public List<Story> getTopStories() {
		return getTopStoriesList();
	}

	@Scheduled(fixedRate = 900000)
	@CacheEvict(value = "top_story_cache", allEntries = true)
	public void clearCacheData() {
		log.info("Clearing out cache data after every 15 min");
	}

	public Set<Story> getPastTopStories() {
		return pastStorySet;
	}

	public SortedSet<TopComment> getCommentsById(int storyId) {
		SortedSet<TopComment> topCommentSet = new TreeSet<>(Comparator.comparing(TopComment::getTotalComments)
				.reversed().thenComparing(TopComment::getTotalComments));
		Story story = HnApiUtils.getStory(storyId);
		if (story != null) {
			int[] comments = story.getKids();
			if (comments != null) {
				for (int commentId : comments) {
					Comment comment = HnApiUtils.getComment(commentId);
					if (comment != null) {
						//adding 1 to consider main comment also.
						int subCommentCount = 1 + HnApiUtils.getSubCommentCount(comment);
						TopComment topComment = new TopComment(comment.getText(), comment.getBy(),
								HnApiUtils.getUserAgeByName(comment.getBy()), subCommentCount);
						topCommentSet.add(topComment);
					}
				}
			}
		}
		return topCommentSet;
	}

	private List<Story> getTopStoriesList() {

		SortedSet<Story> topStoriesSet = new TreeSet<>(
				Comparator.comparing(Story::getScore).reversed().thenComparing(Story::getScore));

		List<Integer> topStories = HnApiUtils.getTopStoryIds();
		addStoriesToSet(topStoriesSet, topStories);
		List<Story> topTenStories = topStoriesSet.stream().limit(10).collect(Collectors.toList());

		addToPastStorySet(topTenStories);

		return topTenStories;
	}


	private void addStoriesToSet(SortedSet<Story> topStoriesSet, List<Integer> subList) {
		for (int storyId : subList) {
			Story story = HnApiUtils.getStory(storyId);
			if (story.getType().equals(HnApiConstants.ITEM_TYPE_STORY)) {
				topStoriesSet.add(story);
			}
		}
	}


	private void addToPastStorySet(List<Story> topTenStories) {
		pastStorySet.clear();
		topTenStories.stream().forEach(pastStorySet::add);
	}

}