package life.lby.community.community.service;

import life.lby.community.community.dto.PageDTO;
import life.lby.community.community.dto.QuestionDTO;
import life.lby.community.community.exception.CustomizeErrorCode;
import life.lby.community.community.exception.CustomizeException;
import life.lby.community.community.mapper.QuestionMapper;
import life.lby.community.community.mapper.UserMapper;
import life.lby.community.community.model.Question;
import life.lby.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {

        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();

        Integer totalPage;

        Integer totalCount = questionMapper.count();

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
        List<Question> questions = questionMapper.questionList(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public PageDTO list(Integer userId, Integer page, Integer size) {
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();

        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId);

        if (totalCount%size == 0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }

        if(page<1){
            page = 1;
        }
        if(totalPage >0 && page > totalPage){
            page = totalPage;
        }

        pageDTO.setPagination(totalPage,page);

        Integer offset = size*(page-1);
        List<Question> questions = questionMapper.List(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.create(question);
        }else{
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }

    public void incView(Integer id) {
        Question question = questionMapper.getById(id);
        question.setViewCount(question.getViewCount()+1);
        questionMapper.updateViewCount(question);
    }


    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        List<Question> list = questionMapper.selectByTagRegexp(questionDTO.getId(), regexpTag);
        List<QuestionDTO> relatedQuestionList = new ArrayList<>();

        for (Question question : list) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(question,dto);
            dto.setUser(user);
            relatedQuestionList.add(dto);
        }
        return relatedQuestionList;
    }
}
