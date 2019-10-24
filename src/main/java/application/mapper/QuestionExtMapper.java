package application.mapper;

import java.util.List;

import application.dto.QuestionQueryDTO;
import application.model.Question;

public interface QuestionExtMapper {
	int incView(Question record);

	int incCommentCount(Question record);

	List<Question> selectRelated(Question question);

	Integer countBySearch(QuestionQueryDTO questionQueryDTO);

	List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
	
}