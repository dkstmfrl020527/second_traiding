package org.koreait.trend.entities;

import org.koreait.global.entities.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("TREND")
public class Trend extends BaseEntity {
    @Id
    private Long seq;
    private String category;

    @Column("wordCloud")
    private String wordCloud;

    private String keywords;
}
