package com.amolli.oyeongshop.ver2.board.controller;


import com.amolli.oyeongshop.ver2.board.dto.ReviewDTO;
import com.amolli.oyeongshop.ver2.board.dto.ReviewResponseDTO;
import com.amolli.oyeongshop.ver2.board.model.Review;
import com.amolli.oyeongshop.ver2.board.service.ReviewService;
import com.amolli.oyeongshop.ver2.product.model.Product;
import com.amolli.oyeongshop.ver2.product.service.ProductService;
import com.amolli.oyeongshop.ver2.s3.AwsS3Service;
import com.amolli.oyeongshop.ver2.security.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final AwsS3Service awsS3Service;
    private final ReviewService reviewService;
    private final ProductService productService;

    @GetMapping("/lists")
    public String qnaList( ) {
        return "/board/qna-product-list";
    }

    @GetMapping("/write")
    public String qnaWrite() { return "/board/qna-product-write"; }

    @GetMapping("/pwd")
    public String pwdCheck() { return "/board/pwd-check"; }

    @GetMapping("/modify")
    public String qnaModify() { return "/board/qna-product-modify"; }


    // 리뷰 게시판!
    // GET 리뷰 페이지 조회(상품ID에 따른 리뷰들)
    @GetMapping("/review-list")    //product-detail 하단에 위치
    public String findAllReview(Long prodId, Model model) {

        List<Review> reviews = reviewService.findByProdId(prodId);

        List<ReviewResponseDTO> reviewdto = reviews.stream().map(ReviewResponseDTO::from).collect(Collectors.toList());

        model.addAttribute("reviewdto", reviewdto);

        return "board/review-list";
    }

    // GET MY리뷰 페이지 조회(유저ID에 따른 리뷰들)
    @GetMapping("/my-review-list")
    public String findMyReview(@AuthenticationPrincipal PrincipalDetails userDetails, Model model) {

        String userId = userDetails.getUser().getUserId();
        System.out.println("CONTROLLER~!~!~!~~!"+userId);

        List<Review> reviews = reviewService.findByUserId(userId);
        System.out.println("review"+reviews.toString());

        List<ReviewResponseDTO> reviewdto = reviews.stream().map(ReviewResponseDTO::from).collect(Collectors.toList());

        model.addAttribute("reviewdto", reviewdto);

        return "board/my-review-list";
    }

    // GET 리뷰 작성 페이지 조회(해당 페이지의 상품 id 가져와서)
    @GetMapping("/review-write/{prodId}")
    public String reviewWrite(@AuthenticationPrincipal PrincipalDetails details, @PathVariable Long prodId , Model model) {

        Product product = productService.findById(prodId);
//        String prodName = product.getProdName();

        String userId = details.getUser().getUserId();

        model.addAttribute("userId", userId);
        model.addAttribute("product", product);

        return "/board/review-write";
    }

    // POST 리뷰 작성 (INSERT)
    @PostMapping(value = "/review-write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam(value = "image1", required = false) List<MultipartFile> files, ReviewDTO reviewDTO
                             , @RequestParam("prodId") Long prodId, @AuthenticationPrincipal PrincipalDetails details) {

        List<String> imagepath = null;

        // 멀티파트파일->S3에 업로드 하고 imageUrls 리스트로 받아옴
        if(!ObjectUtils.isEmpty(files) && !files.get(0).getOriginalFilename().equals("")){
            imagepath = awsS3Service.uploadS3(files);
        }

        // imageUrls를 받아서 DB에 업로드(tbl_review, tbl_review_img 동시에)..
        reviewService.uploadDB(imagepath, reviewDTO, prodId, details);

        return "redirect:/product/detail/" + prodId;
    }

    // POST 리뷰 삭제 (DELETE)
    @PostMapping("/my-review-delete")
    public String deleteReview(@RequestParam("deleteId") Long reviewId) {

        reviewService.deleteMyReview(reviewId);

        return "redirect:/board/my-review-list";
    }
}
