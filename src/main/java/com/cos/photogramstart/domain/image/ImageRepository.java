package com.cos.photogramstart.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select * from image where userid in (select touserid from subscribe where fromuserid = :principalId) order by id desc", nativeQuery = true)
    Page<Image> mStory(int principalId, Pageable pageable);
}
