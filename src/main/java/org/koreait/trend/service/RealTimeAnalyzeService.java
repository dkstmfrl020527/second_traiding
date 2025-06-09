package org.koreait.trend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.global.configs.FileProperties;
import org.koreait.global.configs.PythonProperties;
import org.koreait.trend.entities.NewsTrend;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({PythonProperties.class, FileProperties.class})
public class RealTimeAnalyzeService {

    private final ObjectMapper om;

    public NewsTrend analyze(String siteUrl) {
        // 파이썬 실행 (SQL 저장하지 않음)
        String pythonPath = "./trend/venv/Scripts/python.exe";
        String scriptPath = "./trend/realtime_trend.py";  // 별도 스크립트
        System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    pythonPath, scriptPath, siteUrl
            );
            Process process = builder.start();
            System.out.println("분석중");

            if (process.waitFor() == 0) {
                System.out.println("분석중2");
                String json = process.inputReader().lines()
                        .collect(Collectors.joining());
                System.out.println(json);
                return om.readValue(json, NewsTrend.class);
            }
            else {
                System.out.println("Python 종료 코드: " + process.waitFor());
                System.out.println("=== Python 에러 ===");
                process.errorReader().lines().forEach(System.out::println);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("anj");
        }

        return null;
    }
}
