package application.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import application.dto.FileDTO;
import application.provider.UCloudProvider;

@Controller
public class FileController {
	
	@Autowired
	private UCloudProvider uCloudProvider;
	
	@RequestMapping("/file/upload")
	@ResponseBody
	public FileDTO upload(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("editormd-image-file");
		try {
			String fileName = uCloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
			FileDTO fileDTO = new FileDTO();
			fileDTO.setSuccess(1);
			fileDTO.setUrl(fileName);
			return fileDTO;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		FileDTO fileDTO = new FileDTO();
		fileDTO.setSuccess(1);
		fileDTO.setUrl("/static/images/jianlai.png");
		return fileDTO;
	}
}
