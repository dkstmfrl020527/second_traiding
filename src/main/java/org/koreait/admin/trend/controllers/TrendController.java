package org.koreait.admin.trend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.koreait.admin.global.controllers.CommonController;
import lombok.RequiredArgsConstructor;
import org.koreait.trend.entities.NewsTrend;
import org.koreait.trend.entities.Trend;
import org.koreait.trend.service.NewsSaveService;
import org.koreait.trend.service.RealTimeAnalyzeService;
import org.koreait.trend.service.TrendInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/trend")
public class TrendController extends CommonController {

    private final TrendInfoService infoService;
    private final RealTimeAnalyzeService realTimeService;
    private ObjectMapper objectMapper;
    private final NewsSaveService newsSaveService;

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "trend";
    }

    @GetMapping({"", "/news"}) // /admin/trend, /admin/trend/news
    public String news(Model model) {
        commonProcess("news", model);

        Trend item = infoService.getLatest("https://news.naver.com");
        model.addAttribute("item", item);
        System.out.println(item);

        return "admin/trend/news";
    }

    @GetMapping("/etc")
    public String etc(@ModelAttribute TrendSearch search, Model model,ObjectMapper objectMapper) {
        commonProcess("etc", model);

        // ⭐ 사용자가 URL을 입력했다면 실시간 분석
        if (search.getSiteUrl() != null && !search.getSiteUrl().trim().isEmpty()) {
            try {
                System.out.println("실시간 분석 시작: " + search.getSiteUrl());

                NewsTrend result = realTimeService.analyze(search.getSiteUrl());

                if (result != null) {
                    String keywordsJson = objectMapper.writeValueAsString(result.getKeywords());
                    model.addAttribute("keywordsJson", keywordsJson);
                    model.addAttribute("analysisResult", result);
                    model.addAttribute("analyzedUrl", search.getSiteUrl());
                    model.addAttribute("success", true);
                    System.out.println("분석 성공: " + result.getImage());
                    newsSaveService.DataSave(result,search.getSiteUrl());
                } else {
                    model.addAttribute("error", "분석에 실패했습니다. 사이트 주소를 확인해주세요.");
                }

            } catch (Exception e) {
                System.err.println("분석 에러: " + e.getMessage());
                model.addAttribute("error", "분석 중 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return "admin/trend/etc";
    }

    /**
     * 공통 처리
     *
     * @param code  : 서브메뉴 코드
     * @param model
     */
    private void commonProcess(String code, Model model) {
        code = StringUtils.hasText(code) ? code : "news";

        String pageTitle = "";
        List<String> addScript = new ArrayList<>();

        if (code.equals("news")) {
            addScript.add("trend/news");
            pageTitle = "오늘의 뉴스 트렌드";
        } else if (code.equals("etc")) {
            addScript.add("trend/etc");  // JavaScript 파일 추가
            pageTitle = "실시간 사이트 분석";  // 페이지 제목 설정
        }

        model.addAttribute("subCode", code);
        model.addAttribute("addScript", addScript);
        model.addAttribute("pageTitle", pageTitle);
    }
}