package org.koreait.trend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.configs.FileProperties;
import org.koreait.global.configs.PythonProperties;
import org.koreait.trend.entities.NewsTrend;
import org.koreait.trend.entities.Trend;
import org.koreait.trend.repositories.TrendRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Lazy
@RequiredArgsConstructor
@Service
@EnableConfigurationProperties({PythonProperties.class, FileProperties.class})
public class NewsSaveService {
    private final PythonProperties properties;
    private final FileProperties fileProperties;

    private final WebApplicationContext ctx;
    private final TrendRepository repository;
    private final HttpServletRequest request;
    private final ObjectMapper om;

    public void DataSave(NewsTrend item,String siteUrl) {
        if (item == null) return;

        String wordCloud = fileProperties.getPath() + "/trend/" +item.getImage();

        try {
            String keywords = om.writeValueAsString(item.getKeywords());
            Trend data = new Trend();
            data.setCategory(siteUrl);
            data.setWordCloud(wordCloud);
            data.setKeywords(keywords);
            repository.save(data);
            System.out.println(siteUrl);

            System.out.println("트렌드 데이터 저장 완료: " + wordCloud);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
