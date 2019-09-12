package life.lby.community.community.mapper;

import life.lby.community.community.model.Comment;
import life.lby.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(parent_id,type,commentator,gmt_create,gmt_modified,content) values(#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);
    @Select("select * from comment where id = #{parentId}")
    Comment getByParentId(@Param(value = "parentId") Integer parentId);
    @Select("select * from comment where parent_id = #{id} and type = #{type} order by gmt_create desc")
    List<Comment> getListByParentId(@Param(value = "id") Integer id , @Param(value = "type") Integer type);
    @Update("update comment set comment_count=#{commentCount} where id=#{id}")
    void updateCommentCount(Comment comment);
}
