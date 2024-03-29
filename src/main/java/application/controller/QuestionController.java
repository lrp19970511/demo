package application.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import application.dto.CommentDTO;
import application.dto.QuestionDTO;
import application.enums.CommentTypeEnum;
import application.service.CommentService;
import application.service.QuestionService;

@Controller
public class QuestionController {
	@Autowired
	private QuestionService questionService;

	@Autowired
	private CommentService commentService;
	@GetMapping("/question/{id}")
	public String question(@PathVariable(name = "id") Long id, Model model) {
		QuestionDTO questionDTO = questionService.getById(id);
		List<QuestionDTO> relatedQuestions = questionService.selectReated(questionDTO);
		List<CommentDTO> comments = commentService.listByTargetId(id,CommentTypeEnum.QUESTION);
		// 累加阅读数
		questionService.incView(id);
		model.addAttribute("question", questionDTO);
		model.addAttribute("comments", comments);
		model.addAttribute("relatedQuestions", relatedQuestions);
		return "question";
	}
}
