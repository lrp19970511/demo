package application.interceptor;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import application.mapper.UserMapper;
import application.model.User;
import application.model.UserExample;
import application.service.NotificationService;

@Service
public class SessionInterceptor implements HandlerInterceptor {
	@Autowired
	private UserMapper usermapper;
	@Autowired
    private NotificationService notificationService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length != 0) {
			for (Cookie cookie : cookies) {

				if (cookie.getName().equals("token")) {
					String token = cookie.getValue();
					UserExample userExample = new UserExample();
					userExample.createCriteria().andTokenEqualTo(token);
					List<User> users = usermapper.selectByExample(userExample);
					if (users.size() != 0) {
						request.getSession().setAttribute("user", users.get(0));
						Long unreadCount = notificationService.unreadCount(users.get(0).getId());
						request.getSession().setAttribute("unreadCount", unreadCount);
					}
					break;
				}

			}
		}
		// TODO Auto-generated method stub
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
