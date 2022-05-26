package com.cos.photogramstart.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select * from image where userid in (select touserid from subscribe where fromuserid = :principalId) order by id desc", nativeQuery = true)
    Page<Image> mStory(int principalId, Pageable pageable);

    @Query(value = "select i.* from image i inner join (select imageid, count(imageid) likeCount from likes group by imageid order by likeCount desc) l on i.id = l.imageid order by likeCount desc", nativeQuery = true)
    List<Image> mPopular();
}
