package org.koreait.trend.service;

import lombok.RequiredArgsConstructor;
import org.koreait.global.search.CommonSearch;
import org.koreait.trend.entities.Trend;
import org.koreait.trend.exceptions.TrendNotFoundException;
import org.koreait.trend.repositories.TrendRepository;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableJdbcRepositories(basePackages = "org.koreait")
public class TrendInfoService {

    private final TrendRepository repository;

    /**
     * 최근 트렌드 1개 조회
     *
     * @param category
     * @return
     */
    public Trend getLatest(String category) {
        Trend item = repository.getLatest(category).orElseThrow(TrendNotFoundException::new);

        return item;
    }


    /**
     * 특정 날자의 트렌드 데이터 1개
     * @param date
     * @return
     */
    public Trend get(String category, LocalDate date) {

        return null;
    }

    /**
     * 특정 날짜 범위의 트렌트 데이터 조회
     *
     * @return
     */
    public List<Trend> getList(String category, CommonSearch search) {
        System.out.println("Repository: " + repository);
        return repository.findByCategoryAndDateRange(
                category,
                search.getSDate(),
                search.getEDate()
        );
    }



}
