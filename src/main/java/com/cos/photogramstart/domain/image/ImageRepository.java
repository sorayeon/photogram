package com.cos.photogramstart.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select * from image where userid in (select touserid from subscribe where fromuserid = :principalId)", nativeQuery = true)
    List<Image> mStory(int principalId);
}
