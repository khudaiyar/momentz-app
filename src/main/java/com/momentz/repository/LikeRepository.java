package com.momentz.repository;

import com.momentz.model.Like;
import com.momentz.model.Post;
import com.momentz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);

    Boolean existsByPostAndUser(Post post, User user);

    Long countByPost(Post post);

    void deleteByPostAndUser(Post post, User user);
}