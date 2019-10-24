package application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import application.dto.NotificationDTO;
import application.dto.PaginationDTO;
import application.enums.NotificationTypeEnum;
import application.model.Notification;
import application.model.User;
import application.service.NotificationService;

@Controller
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
	@GetMapping("/notification/{id}")
	public String profile(@PathVariable(name = "id") Long id,HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return "redirect:/";
		}
		NotificationDTO notificationDTO = notificationService.read(id,user);
		if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
				|| NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
			return "redirect:/question/" + notificationDTO.getOuterid();
		}else {
			return "redirect:/";
		}
	}
}
