package application.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import application.cache.HotTagCache;
import application.mapper.QuestionMapper;
import application.model.Question;
import application.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HotTagTasks {
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private HotTagCache hotTagCache;
	@Scheduled(fixedRate = 1000 *60 *60 * 3)
	//@Scheduled(cron = " 0 0 1 * * * ")
	public void hotTagSchedule() {
		int offset = 0;
		 int limit = 20;
		  log.info("hotTagSchedule start {}", new Date());
		  List<Question> list = new ArrayList<>();
		  Map<String, Integer> priorities = new HashMap<>();
		  while(offset == 0 || list.size() == limit) {
			  list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset,limit));
			  for(Question question : list) {
				  String[] tags = StringUtils.split(question.getTag(),",");
				  if(tags != null) {
				  for(String tag : tags) {
					  Integer priority = priorities.get(tag);
					  if(priority != null) {
						  priorities.put(tag,priority + 5 + question.getCommentCount());
					  }else {
						  priorities.put(tag,5 + question.getCommentCount());
					  }
				  }
				  }
			  }
			  offset += limit;
		  }
		  hotTagCache.updateTags(priorities);
		  log.info("hotTagSchedule stop {}",new Date());
	}
}
