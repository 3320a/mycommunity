package life.lby.community.community.service;

import life.lby.community.community.dto.NotificationDTO;
import life.lby.community.community.dto.PageDTO;
import life.lby.community.community.dto.QuestionDTO;
import life.lby.community.community.mapper.NotificationMapper;
import life.lby.community.community.mapper.UserMapper;
import life.lby.community.community.model.Notification;
import life.lby.community.community.model.Question;
import life.lby.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    public PageDTO list(Integer userId, Integer page, Integer size) {

        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();

        Integer totalPage;

        Integer totalCount = notificationMapper.countByUserId(userId);

        if (totalCount%size == 0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }

        if(page<1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        pageDTO.setPagination(totalPage,page);

        Integer offset = size*(page-1);
        List<Notification> notifications = notificationMapper.List(userId,offset,size);
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            User user = userMapper.findById(notification.getNotifier());
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setNotifier(user);
            notificationDTOS.add(notificationDTO);
        }
        pageDTO.setData(notificationDTOS);
        return pageDTO;
    }
}
