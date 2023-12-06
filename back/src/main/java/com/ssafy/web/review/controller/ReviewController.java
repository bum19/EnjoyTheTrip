package com.ssafy.web.review.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.web.member.model.MemberDto;
import com.ssafy.web.review.model.ReviewDto;
import com.ssafy.web.review.model.ReviewListDto;
import com.ssafy.web.review.model.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/review")
@Slf4j
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        super();
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> writeReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        log.info("writeReview ReviewDto - {}", reviewDto);
        HttpSession session = request.getSession(false);
        if(session == null) {
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
        try {
            reviewService.writeReview(reviewDto);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @PostMapping("/{reviewNo}")
    public ResponseEntity<?> updateReview(@PathVariable int reviewNo, @RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        reviewDto.setReviewNo(reviewNo);
        log.info("updateReivew ReviewDto - {}", reviewDto);
        HttpSession session = request.getSession(false);
        //세션이 없거나 세션에있는 작성자와 리뷰 작성자가 다를경우
        if(session == null || !reviewDto.getEmail().equals(((MemberDto)session.getAttribute("userInfo")).getEmail())) {
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
        
        try {
            reviewService.modifyReview(reviewDto);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listReview(@RequestParam Map<String, String> map) {
        log.info("listArticle map - {}", map);
        try {
            ReviewListDto reviewListDto = reviewService.listReview(map);
            return ResponseEntity.ok().body(reviewListDto);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping("/{reviewNo}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable("reviewNo") int reviewNo) throws Exception {
        log.info("getReview - 호출 : " + reviewNo);
        reviewService.updateHit(reviewNo);
        return new ResponseEntity<ReviewDto>(reviewService.getReview(reviewNo), HttpStatus.OK);
    }

    @GetMapping("/delete/{reviewNo}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewNo") int reviewNo, HttpServletRequest request) throws Exception {
        log.info("deleteReview - 호출 : " + reviewNo);
        HttpSession session = request.getSession(false);
        ReviewDto reviewDto = reviewService.getReview(reviewNo);
        //세션이 없거나 세션에있는 작성자와 리뷰 작성자가 다를경우
        if(session == null || !reviewDto.getEmail().equals(((MemberDto)session.getAttribute("userInfo")).getEmail())) {
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
        reviewService.deleteReview(reviewNo);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private ResponseEntity<String> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}