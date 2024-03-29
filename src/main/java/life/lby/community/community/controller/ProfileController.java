package life.lby.community.community.controller;

import life.lby.community.community.dto.PageDTO;
import life.lby.community.community.model.User;
import life.lby.community.community.service.NotificationService;
import life.lby.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的话题");
            PageDTO pageDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",pageDTO);
        }else if("replies".equals(action)){
//            PageDTO pageDTO = notificationService.list(user.getId(), page, size);
//            model.addAttribute("section","replies");
//            model.addAttribute("pagination",pageDTO);
//            model.addAttribute("sectionName","最新回复");
        }


        return "profile";
    }
}
