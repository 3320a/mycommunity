package life.lby.community.community.service;

import life.lby.community.community.enums.CommentTypsEnums;
import life.lby.community.community.mapper.CommentMapper;
import life.lby.community.community.mapper.QuestionMapper;
import life.lby.community.community.model.Comment;
import life.lby.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){

        }

        if(comment.getType() == null || !CommentTypsEnums.isExist(comment.getType())){

        }

        if(comment.getType() == CommentTypsEnums.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.getByParentId(comment.getParentId());
            if(dbcomment==null){

            }
            commentMapper.insert(comment);
        }else {
            //回复话题
            Question question = questionMapper.getById(comment.getParentId());
            if (question == null){

            }
            commentMapper.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.updateCommentCount(question);
        }
    }
}
