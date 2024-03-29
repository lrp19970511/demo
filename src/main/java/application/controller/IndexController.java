package application.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import application.cache.HotTagCache;
import application.dto.PaginationDTO;
import application.service.QuestionService;

@Controller
public class IndexController {
	@Autowired
	private QuestionService questionService;

	@Autowired
	private HotTagCache hotTagCache;
	@GetMapping("/")
	public String index(HttpServletRequest request, Model model,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "tag", required = false) String tag) {
		
		PaginationDTO pagination = questionService.list(page, size,search,tag);
		List<String> tags = hotTagCache.getHots();
		model.addAttribute("pagination", pagination);
		model.addAttribute("search", search);
		model.addAttribute("tag", tag);
		model.addAttribute("tags", tags);
		return "index";
	}
}