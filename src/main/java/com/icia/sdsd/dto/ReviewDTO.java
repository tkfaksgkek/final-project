package com.icia.sdsd.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("review")
public class ReviewDTO {

    private String srNum;           // 리뷰 일련번호
    private String srCount;         // 리뷰 갯수
    private String srCode;          // 숙소 일련번호
    private String srRoom;          // 숙소 방이름
    private String srId;            // 아이디
    private String srNickName;      // 작성자 이름(닉네임)
    private String srReviewTitle;   // 리뷰 제목
    private String srReview;        // 리뷰 내용
    private String srDate;          // 리뷰 날짜
    private int srGrade;            // 별점

    public static ReviewDTO toDTO(ReviewEntity entity){
        ReviewDTO review = new ReviewDTO();

        review.setSrNum(entity.getSrNum());
        review.setSrCount(entity.getSrCount());
        review.setSrCode(entity.getSrCode());
        review.setSrRoom(entity.getSrRoom());
        review.setSrId(entity.getSrId());
        review.setSrNickName(entity.getSrNickName());
        review.setSrReviewTitle(entity.getSrReviewTitle());
        review.setSrReview(entity.getSrReview());
        review.setSrDate(entity.getSrDate());
        review.setSrGrade(entity.getSrGrade());

        return review;
    }

}
