package life.lby.community.community.service;

import life.lby.community.community.dto.CommentDTO;
import life.lby.community.community.enums.CommentTypsEnums;
import life.lby.community.community.enums.NotificationStatusEnums;
import life.lby.community.community.enums.NotificationTypeEnums;
import life.lby.community.community.exception.CustomizeErrorCode;
import life.lby.community.community.exception.CustomizeException;
import life.lby.community.community.mapper.CommentMapper;
import life.lby.community.community.mapper.NotificationMapper;
import life.lby.community.community.mapper.QuestionMapper;
import life.lby.community.community.mapper.UserMapper;
import life.lby.community.community.model.Comment;
import life.lby.community.community.model.Notification;
import life.lby.community.community.model.Question;
import life.lby.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if(comment.getType() == null || !CommentTypsEnums.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if(comment.getType() == CommentTypsEnums.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.getByParentId(comment.getParentId());
            if(dbcomment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            Comment newComment = new Comment();
            newComment.setId(dbcomment.getId());
            newComment.setCommentCount(dbcomment.getCommentCount()+1);
            commentMapper.updateCommentCount(newComment);
            //创建通知
            createNotification(comment, dbcomment.getCommentator(), NotificationTypeEnums.REPLY_COMMENT);
        }else {
            //回复话题
            Question question = questionMapper.getById(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.updateCommentCount(question);

            createNotification(comment,question.getCreator(), NotificationTypeEnums.REPLY_QUESTION);
        }
    }

    private void createNotification(Comment comment, Integer receiver, NotificationTypeEnums notificationType) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnums.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Integer id, CommentTypsEnums type) {
        List<Comment> comments = commentMapper.getListByParentId(id,type.getType());
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            User user = userMapper.findById(comment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }
}
