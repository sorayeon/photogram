package com.cos.photogramstart.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    @Modifying
    @Query(value = "insert into likes(imageid, userid, createdate) values (:imageId, :principalId, now())", nativeQuery = true)
    int mLikes(int imageId, int principalId);

    @Modifying
    @Query(value = "delete from likes where imageid = :imageId and userid = :principalId", nativeQuery = true)
    int mUnLikes(int imageId, int principalId);
}
