package application.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import application.dto.TagDTO;

public class TagCache {
  public static List<TagDTO> get(){
	  List<TagDTO> tagDTOs = new ArrayList<>();
	  TagDTO program = new TagDTO();
	  program.setCategoryName("开发语言");
	  program.setTags(Arrays.asList("javascript","php","css","html","java","node.js"));
	  tagDTOs.add(program);
	  
	  TagDTO framework = new TagDTO();
	  framework.setCategoryName("平台框架");
	  framework.setTags(Arrays.asList("spring","spring boot","express","struts","hibernate","mybatis"));
	  tagDTOs.add(framework);
	  
	  return tagDTOs;
  }
  public static String filterInvalid(String tags) {
	  String[] split = StringUtils.split(tags,      ",");
	  List<TagDTO> tagDTOs = get();
	  List<String> tagList = tagDTOs.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
	  String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
	  return invalid;
  }
}
