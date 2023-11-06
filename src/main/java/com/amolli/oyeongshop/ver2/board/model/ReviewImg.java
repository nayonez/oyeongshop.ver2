package com.amolli.oyeongshop.ver2.board.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "tbl_review_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewImageId;

    // 파일 이름
    private String reviewUserFileName;

    // s3에서 사용할 url
    private String reviewServerFileName;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}
