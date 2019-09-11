package life.lby.community.community.mapper;

import life.lby.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,comment_count,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{tag})")
    void create(Question question);

    @Select("select * from question order by gmt_create desc limit #{offset} , #{size}")
    List<Question> questionList(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} order by gmt_create desc limit #{offset} , #{size}")
    List<Question> List(@Param(value = "userId") Integer userId,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id=#{id}")
    void update(Question question);
    @Update("update question set view_count=#{viewCount} where id=#{id}")
    void updateViewCount(Question question);
    @Update("update question set comment_count=#{commentCount} where id=#{id}")
    void updateCommentCount(Question question);
}
