package jpabook.jpashop.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*jpashop 연결 gitHub name*/
@Slf4j
@Controller
public class HomeController {
/*최근에 검색했던 리소스 컨트롤+E*/
    @RequestMapping("/")
    public String home(){
        log.info("home Controller");
        return "home";
    }
}
