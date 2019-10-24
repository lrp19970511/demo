package application.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import application.dto.CommentCreateDTO;
import application.dto.CommentDTO;
import application.dto.ResultDTO;
import application.enums.CommentTypeEnum;
import application.exception.CustomizeErrorCode;
import application.mapper.CommentMapper;
import application.model.Comment;
import application.model.User;
import application.service.CommentService;

@Controller
public class CommentController {
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private CommentService commentService;

	@ResponseBody
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public Object post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
		}
		if(commentCreateDTO== null || StringUtils.isBlank(commentCreateDTO.getContent())) {
			return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
		}
		Comment comment = new Comment();
		comment.setParentId(commentCreateDTO.getParentId());
		comment.setContent(commentCreateDTO.getContent());
		comment.setType(commentCreateDTO.getType());
		comment.setGmtModified(System.currentTimeMillis());
		comment.setGmtCreate(System.currentTimeMillis());
		comment.setCommentator(user.getId());
		comment.setLikeCount(0L);
		commentService.insert(comment,user);
		return ResultDTO.okOf();
	}
	@ResponseBody
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
	public ResultDTO<List> comments(@PathVariable(name="id") Long id) {
		List<CommentDTO> commentDTOs = commentService.listByTargetId(id,CommentTypeEnum.COMMENT);
		return ResultDTO.okOf(commentDTOs);
	}
}
