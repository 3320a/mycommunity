package life.lby.community.community.mapper;

import life.lby.community.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("insert into notification(notifier,receiver,outer_id,type,gmt_create,status) values(#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status})")
    void insert(Notification notification);

    @Select("select count(1) from notification where id = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from notification where id=#{userId} limit #{offset} , #{size}")
    List<Notification> List(@Param(value = "userId") Integer userId,@Param(value = "offset")Integer offset, @Param(value = "size") Integer size);
}
