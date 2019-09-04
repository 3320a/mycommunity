package life.lby.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFristPage;
    private Boolean showNext;
    private Boolean showEndPage;

    private Integer page;
    private Integer totalPage = 0;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
        this.page = page;
        pages.add(page);

        for(int i=1;i<=3;i++){
            if (page-i>0){
                pages.add(0,page-i);
            }
            if (page+i<=totalPage){
                pages.add(page+i);
            }
        }

        if (page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }

        if(page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }

        if(!pages.contains(1)){
            showFristPage = true;
        }else{
            showFristPage = false;
        }

        if(!pages.contains(totalPage)){
            showEndPage = true;
        }else{
            showEndPage = false;
        }
    }
}
