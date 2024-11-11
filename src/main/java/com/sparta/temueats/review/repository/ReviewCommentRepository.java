package com.sparta.temueats.review.repository;

import com.sparta.temueats.review.entity.P_reviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewCommentRepository extends JpaRepository<P_reviewComment, UUID> {
}
