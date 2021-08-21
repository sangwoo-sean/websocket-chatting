package main.command;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;

@Controller
public class TestController {

    private static ModelAndView FORM_VIEW;

    public void init(String view) {
        FORM_VIEW = new ModelAndView(view);
    }

    @GetMapping("/")
    public ModelAndView processGet() {
        init("test");
        FORM_VIEW.addObject("user_name", randomString(6)); // 세션으로 아이디 넣어주면 됨
        return FORM_VIEW;
    }

    private String randomString(int size) {
        StringBuffer result = new StringBuffer();

        Random random = new Random();
        for (int i=0; i<size; i++) {
            result.append(String.valueOf((char) ((int) random.nextInt(26)+97)));
        }
        return result.toString();
    }

}
