package com.icia.sdsd.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "SDREVIEW")
public class ReviewEntity {

    @Id
    private String srNum;           // 리뷰 일련번호

    @Column
    private String srCount;         // 리뷰 갯수

    @Column
    private String srCode;          // 숙소 일련번호

    @Column
    private String srRoom;          // 숙소 방이름

    @Column
    private String srId;            // 아이디

    @Column
    private String srNickName;      // 작성자 이름(닉네임)

    @Column
    private String srReviewTitle;   // 리뷰 제목

    @Column
    private String srReview;        // 리뷰 내용

    @Column
    private String srDate;          // 리뷰 날짜

    @Column
    private int srGrade;            // 별점



    public static ReviewEntity toEntity(ReviewDTO review){
        ReviewEntity entity = new ReviewEntity();

        entity.setSrNum(review.getSrNum());
        entity.setSrCount(review.getSrCount());
        entity.setSrCode(review.getSrCode());
        entity.setSrRoom(review.getSrRoom());
        entity.setSrId(review.getSrId());
        entity.setSrNickName(review.getSrNickName());
        entity.setSrReviewTitle(review.getSrReviewTitle());
        entity.setSrReview(review.getSrReview());
        entity.setSrDate(review.getSrDate());
        entity.setSrGrade(review.getSrGrade());

        return entity;
    }

}
