package com.example.bai5_6.Service;

import com.example.bai5_6.Model.Product;
import com.example.bai5_6.Model.Review;
import com.example.bai5_6.Repository.ProductRepository;
import com.example.bai5_6.Repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Review> getReviewsByProduct(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public double getAverageRating(Integer productId) {
        Double avg = reviewRepository.getAverageRating(productId);
        return avg != null ? avg : 0;
    }

    @Override
    public void saveReview(Integer productId, int rating, String comment, String username) {

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        // ✅ kiểm tra user đã review chưa
        Review existing = reviewRepository.findByProductIdAndUsername(productId, username);

        if (existing != null) {
            // update
            existing.setRating(rating);
            existing.setComment(comment);
            reviewRepository.save(existing);
        } else {
            // create mới
            Review review = new Review();
            review.setProduct(product);
            review.setRating(rating);
            review.setComment(comment);
            review.setUsername(username);

            reviewRepository.save(review);
        }
    }

    @Override
    public Review getUserReview(Integer productId, String username) {
        return reviewRepository.findByProductIdAndUsername(productId, username);
    }
}