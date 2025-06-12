package org.koreait.admin.trend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.koreait.admin.global.controllers.CommonController;
import lombok.RequiredArgsConstructor;
import org.koreait.global.search.CommonSearch;
import org.koreait.trend.entities.DummyTrend;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String etc(@ModelAttribute TrendSearch search, Model model, ObjectMapper objectMapper) {
        commonProcess("etc", model);

        // ⭐ 사용자가 URL을 입력했다면 실시간 분석
        if (search.getSiteUrl() != null && !search.getSiteUrl().trim().isEmpty()) {
            try {
                System.out.println("실시간 분석 시작: " + search.getSiteUrl());

                NewsTrend result = realTimeService.analyze(search.getSiteUrl());
                System.out.println("result: " + result);

                if (result != null) {
                    String keywordsJson = objectMapper.writeValueAsString(result.getKeywords());
                    model.addAttribute("keywordsJson", keywordsJson);
                    model.addAttribute("analysisResult", result);
                    model.addAttribute("analyzedUrl", search.getSiteUrl());
                    model.addAttribute("success", true);
                    System.out.println("분석 성공: " + result.getImage());
                    newsSaveService.DataSave(result, search.getSiteUrl());

                    // 🆕 일주일 데이터 생성 및 전달
                    List<Trend> weeklyTrends = new ArrayList<>();
                    List<String> dates = new ArrayList<>(); // String으로 변경
                    List<Map<String, Integer>> keywordsByDate = new ArrayList<>();

                    for (int i = 0; i < 7; i++) {
                        System.out.println("처리 중인 일자: " + i);
                        LocalDate targetDate = LocalDate.now().minusDays(i);

                        // 날짜 포맷팅 (MM-dd 형식)
                        String dateStr = targetDate.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd"));
                        dates.add(dateStr);

                        // 해당 날짜의 실제 데이터 조회
                        Trend trend = null;
                        try {
                            trend = infoService.get(search.getSiteUrl(), targetDate);
                        } catch (Exception e) {
                            System.out.println("데이터 조회 실패: " + e.getMessage());
                        }

                        if (trend == null) {
                            // 실제 데이터가 없으면 더미 데이터 생성
                            LocalDateTime targetDateTime = targetDate.atTime(12, 0);
                            trend = DummyTrend.createDummyForDate("[더미] " + search.getSiteUrl(), targetDateTime);
                            System.out.println("더미 데이터 생성: " + targetDate);
                        } else {
                            System.out.println("실제 데이터 발견: " + targetDate);
                        }

                        weeklyTrends.add(trend);

                        // 키워드 파싱
                        try {
                            if (trend.getKeywords() != null && !trend.getKeywords().trim().isEmpty()) {
                                Map<String, Integer> keywords = objectMapper.readValue(trend.getKeywords(), Map.class);
                                keywordsByDate.add(keywords);
                            } else {
                                keywordsByDate.add(new HashMap<>());
                            }
                        } catch (Exception e) {
                            System.err.println("키워드 파싱 오류: " + e.getMessage());
                            keywordsByDate.add(new HashMap<>());
                        }
                    }

                    // 🆕 JSON 문자열로 변환하여 전달
                    try {
                        String weeklyDatesJson = objectMapper.writeValueAsString(dates);
                        String weeklyKeywordsJson = objectMapper.writeValueAsString(keywordsByDate);

                        model.addAttribute("weeklyTrends", weeklyTrends);
                        model.addAttribute("weeklyDatesJson", weeklyDatesJson);
                        model.addAttribute("weeklyKeywordsJson", weeklyKeywordsJson);

                        // 🆕 통계 정보 추가
                        long realDataCount = weeklyTrends.stream()
                                .filter(trend -> !trend.getCategory().startsWith("[더미]"))
                                .count();

                        Map<String, Object> weeklyStats = new HashMap<>();
                        weeklyStats.put("realDataCount", realDataCount);
                        weeklyStats.put("dummyDataCount", 7 - realDataCount);
                        model.addAttribute("weeklyStats", weeklyStats);

                        System.out.println("✅ 일주일 데이터 전달 완료");
                        System.out.println("📅 날짜 JSON: " + weeklyDatesJson);
                        System.out.println("📊 키워드 개수: " + keywordsByDate.size());

                    } catch (Exception e) {
                        System.err.println("JSON 변환 오류: " + e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    model.addAttribute("error", "분석에 실패했습니다. 사이트 주소를 확인해주세요.");
                }

            } catch (Exception e) {
                System.err.println("분석 에러: " + e.getMessage());
                model.addAttribute("error", "분석 중 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("🔍 사이트 URL이 입력되지 않음");
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