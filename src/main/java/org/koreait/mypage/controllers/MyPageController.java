package org.koreait.mypage.controllers;

import org.koreait.global.libs.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final Utils utils;

    public String index(){
        return utils.tpl("mypage/index");
    }


}
