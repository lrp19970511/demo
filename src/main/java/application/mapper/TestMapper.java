package application.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import application.model.Question;

@Mapper
public interface TestMapper {

	@Select("select * from question limit #{offset},#{size}")
	List<Question> listByUserId(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);
}
