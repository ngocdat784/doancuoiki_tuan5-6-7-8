package com.example.bai5_6.Service;

import com.example.bai5_6.Model.Review;
import java.util.List;

public interface ReviewService {

    // Lấy danh sách review theo product
    List<Review> getReviewsByProduct(Integer productId);

    // Tính trung bình số sao
    double getAverageRating(Integer productId);

    // Lưu hoặc cập nhật review
    void saveReview(Integer productId, int rating, String comment, String username);

    // Lấy review của user cho 1 product
    Review getUserReview(Integer productId, String username);
}