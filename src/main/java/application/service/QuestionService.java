package application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dto.PaginationDTO;
import application.dto.QuestionDTO;
import application.dto.QuestionQueryDTO;
import application.exception.CustomizeErrorCode;
import application.exception.CustomizeException;
import application.mapper.QuestionExtMapper;
import application.mapper.QuestionMapper;
import application.mapper.UserMapper;
import application.model.Question;
import application.model.QuestionExample;
import application.model.User;

@Service
public class QuestionService {
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private QuestionExtMapper questionExtMapper;
	@Autowired
	private UserMapper userMapper;

	public PaginationDTO list(Integer page, Integer size , String search,String tag) {
		if(StringUtils.isNotBlank(search)) {
			String[] tags = StringUtils.split(search,",");
			search = Arrays.stream(tags).collect(Collectors.joining("|"));
		}
		
		
		PaginationDTO paginationDTO = new PaginationDTO();
		Integer totalPage;
		QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
		questionQueryDTO.setSearch(search);
		questionQueryDTO.setTag(tag);
		Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
		if (totalCount % size == 0) {
			totalPage = totalCount / size;
		} else {
			totalPage = totalCount / size + 1;
		}

		if (page < 1) {
			page = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}
		paginationDTO.setPagination(totalPage, page);
		Integer offset = page < 1 ? 0 : size * (page - 1);
		QuestionExample questionExample = new QuestionExample();
		questionExample.setOrderByClause("gmt_create desc");
		questionQueryDTO.setPage(offset);
		questionQueryDTO.setSize(size);
		List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
		List<QuestionDTO> questionDTOList = new ArrayList<>();

		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(question, questionDTO);
			questionDTO.setUser(user);
			questionDTOList.add(questionDTO);
		}
		paginationDTO.setData(questionDTOList);

		// TODO Auto-generated method stub
		return paginationDTO;
	}

	public PaginationDTO list(Long userId, Integer page, Integer size) {
		// TODO Auto-generated method stub
		PaginationDTO paginationDTO = new PaginationDTO();
		Integer totalPage;
		QuestionExample questionExample = new QuestionExample();
		questionExample.createCriteria().andCreatorEqualTo(userId);
		Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
		if (totalCount % size == 0) {
			totalPage = totalCount / size;
		} else {
			totalPage = totalCount / size + 1;
		}

		if (page < 1) {
			page = 1;
		}
		if (page > totalPage) {
			page = totalPage;
		}

		paginationDTO.setPagination(totalPage, page);
		Integer offset = size * (page - 1);
		QuestionExample example = new QuestionExample();
		example.setOrderByClause("gmt_create desc");
		example.createCriteria().andCreatorEqualTo(userId);
		List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),
				new RowBounds(offset, size));
		List<QuestionDTO> questionDTOList = new ArrayList<>();

		for (Question question : questions) {
			User user = userMapper.selectByPrimaryKey(question.getCreator());
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(question, questionDTO);
			questionDTO.setUser(user);
			questionDTOList.add(questionDTO);
		}
		paginationDTO.setData(questionDTOList);

		// TODO Auto-generated method stub
		return paginationDTO;
	}

	public QuestionDTO getById(Long id) {
		Question question = questionMapper.selectByPrimaryKey(id);
		if (question == null) {
			throw new CustomizeException("你找的问题不在了，要不要换个试试？");
		}
		QuestionDTO questionDTO = new QuestionDTO();
		BeanUtils.copyProperties(question, questionDTO);
		// TODO Auto-generated method stub
		User user = userMapper.selectByPrimaryKey(question.getCreator());
		questionDTO.setUser(user);
		return questionDTO;
	}

	public void createOrUpdate(Question question) {
		// TODO Auto-generated method stub
		if (question.getId() == null) {
			//创建
			question.setGmtCreate(System.currentTimeMillis());
			question.setGmtModified(question.getGmtCreate());
			question.setViewCount(0);
			question.setLikeCount(0);
			question.setCommentCount(0);
			questionMapper.insert(question);
		} else {
			//更新
			Question updateQuestion = new Question();
			updateQuestion.setGmtModified(System.currentTimeMillis());
			updateQuestion.setTitle(question.getTitle());
			updateQuestion.setDescription(question.getDescription());
			updateQuestion.setTag(question.getTag());
			QuestionExample example = new QuestionExample();
			example.createCriteria().andIdEqualTo(question.getId());
			int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
			if (updated != 1) {
				throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			}
		}
	}

	public void incView(Long id) {
		Question question = new Question();
		question.setId(id);
		question.setViewCount(1);
		questionExtMapper.incView(question);
	}
	public List<QuestionDTO> selectReated(QuestionDTO queryDTO){
		if(StringUtils.isBlank(queryDTO.getTag())) {
			return new ArrayList<>();
		}
		String[] tags = StringUtils.split(queryDTO.getTag(),",");
		String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
		Question question = new Question();
		question.setId(queryDTO.getId());
		question.setTag(regexpTag);
		List<Question> questions = questionExtMapper.selectRelated(question);
		List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
			QuestionDTO questionDTO = new QuestionDTO();
			BeanUtils.copyProperties(q, questionDTO);
			return questionDTO;
		}).collect(Collectors.toList());
		return questionDTOS;
	}

}
