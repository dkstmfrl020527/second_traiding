package org.koreait.trend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
@EqualsAndHashCode(callSuper = true)
public class DummyTrend extends Trend {

    private static final Random random = new Random();

    // 더미 키워드들 (더미임을 명시)
    private static final List<String> DUMMY_KEYWORDS = Arrays.asList(
            "[더미]대통령", "[더미]경제", "[더미]정치", "[더미]사회", "[더미]문화",
            "[더미]스포츠", "[더미]IT", "[더미]과학", "[더미]교육", "[더미]환경",
            "[더미]건강", "[더미]여행", "[더미]음식", "[더미]패션", "[더미]게임"
    );

    /**
     * 특정 날짜의 더미 데이터 생성
     */
    public static DummyTrend createDummyForDate(String category, LocalDateTime dateTime) {
        DummyTrend dummy = new DummyTrend();

        // 카테고리에 [더미] 표시
        dummy.setCategory("[더미] " + category);
        dummy.setKeywords(generateDummyKeywords());
        dummy.setWordCloud("/uploads/trend/[더미]워드클라우드_" + dateTime.toLocalDate() + ".jpg");
        dummy.setCreatedAt(dateTime);
        dummy.setModifiedAt(dateTime);

        return dummy;
    }

    /**
     * 더미 키워드 JSON 생성 (더미 표시 포함)
     */
    private static String generateDummyKeywords() {
        StringBuilder json = new StringBuilder("{");

        int keywordCount = 8 + random.nextInt(5);  // 8-12개

        for (int i = 0; i < keywordCount; i++) {
            String keyword = DUMMY_KEYWORDS.get(random.nextInt(DUMMY_KEYWORDS.size()));
            int frequency = 10 + random.nextInt(91);

            json.append("\"").append(keyword).append("\":")
                    .append(frequency);

            if (i < keywordCount - 1) {
                json.append(",");
            }
        }

        json.append("}");
        return json.toString();
    }

}