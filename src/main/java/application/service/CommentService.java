package application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.dto.CommentDTO;
import application.enums.CommentTypeEnum;
import application.enums.NotificationStatusEnum;
import application.enums.NotificationTypeEnum;
import application.exception.CustomizeErrorCode;
import application.exception.CustomizeException;
import application.mapper.CommentExtMapper;
import application.mapper.CommentMapper;
import application.mapper.NotificationMapper;
import application.mapper.QuestionExtMapper;
import application.mapper.QuestionMapper;
import application.mapper.UserMapper;
import application.model.Comment;
import application.model.CommentExample;
import application.model.Notification;
import application.model.Question;
import application.model.User;
import application.model.UserExample;

@Service
public class CommentService {
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private QuestionExtMapper questionExtMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CommentExtMapper commentExtMapper;
	@Autowired
	private NotificationMapper notificationMapper;
	@Transactional
	public void insert(Comment comment,User commentator) {
		if (comment.getParentId() == null || comment.getParentId() == 0) {
			throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
		}
		if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
			throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
		}
		if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
			//回复评论
			Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
			if(dbComment == null) {
				throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
			}
			//回复问题
			Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
			if(question == null) {
				throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			}
			commentMapper.insert(comment);
			//增加评论数
			Comment parentComment = new Comment();
			parentComment.setId(comment.getParentId());
			parentComment.setCommentCount((long) 1);
			commentExtMapper.incCommentCount(parentComment);
			//创建通知	
			createNotify(comment,dbComment.getCommentator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_COMMENT,question.getId());
		}else {
			//回复问题
			Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
			if(question == null) {
				throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
			}
			comment.setCommentCount((long) 0);
			commentMapper.insert(comment);
			question.setCommentCount(1);
			questionExtMapper.incCommentCount(question);
			//创建通知
			createNotify(comment, question.getCreator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());
		}
	}
    private void createNotify(Comment comment,Long receiver,String notifierName,String outerTitle,NotificationTypeEnum notificationType,Long outerId) {
    	if(receiver == comment.getCommentator()) {
    		return;
    	}
		Notification notification = new Notification();
		notification.setGmtCreate(System.currentTimeMillis());
		notification.setType(notificationType.getType());
		notification.setOuterid(outerId);
		notification.setNotifier(comment.getCommentator());
		notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
		notification.setReceiver(receiver);
		notification.setNotifierName(notifierName);
		notification.setOuterTitle(outerTitle);
		notificationMapper.insert(notification);
	}
	public List<CommentDTO> listByTargetId(Long id,CommentTypeEnum type) {
		// TODO Auto-generated method stub
		CommentExample commentExample = new CommentExample();
		commentExample.createCriteria()
		.andParentIdEqualTo(id)
		.andTypeEqualTo(type.getType());
		commentExample.setOrderByClause("gmt_create desc");
		List<Comment> comments = commentMapper.selectByExample(commentExample);
		if(comments.size() == 0) {
			return new ArrayList<>();
		}
		//获取去重评论人
		Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
		List<Long> userIds = new ArrayList();
		userIds.addAll(commentators);
		//获取评论人并转换为Map
		UserExample userExample = new UserExample();
		userExample.createCriteria()
		.andIdIn(userIds);
		List<User> users = userMapper.selectByExample(userExample);
		Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user ->user.getId(), user -> user));
		//转换comment为commentDOT
		List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
			CommentDTO commentDTO =new CommentDTO();
			BeanUtils.copyProperties(comment, commentDTO);
			commentDTO.setUser(userMap.get(comment.getCommentator()));
			return commentDTO;
		}).collect(Collectors.toList());
		return commentDTOs;
	}

}
