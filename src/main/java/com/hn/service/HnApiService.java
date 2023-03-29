package com.hn.service;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.hn.model.Story;
import com.hn.model.TopComment;


public interface HnApiService {

	List<Story> getTopStories();

	SortedSet<TopComment> getCommentsById(int storyId);

	Set<Story> getPastTopStories();

	void clearCacheData();
}
