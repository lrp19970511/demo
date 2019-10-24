package application.dto;

import application.model.User;
import lombok.Data;

@Data
public class CommentDTO {
   private Long id;
   private Long parentId;
   private Integer type;
   private Long commentator;
   private Long gmtCreate;
   private Long gmtModified;
   private Long commentCount;
   private String content;
   private Long likeCount;
   private User user;
}
