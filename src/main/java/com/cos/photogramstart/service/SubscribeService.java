package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager em;

    @Transactional
    public void 구독하기(int fromUserId, int toUserId) {
        try {
            subscribeRepository.mSubscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiException("이미 구독을 하였습니다.");
        }
    }

    @Transactional
    public void 구독취소하기(int fromUserId, int toUserId) {
        subscribeRepository.mUnSubscribe(fromUserId, toUserId);
    }

    @Transactional(readOnly = true)
    public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {

        StringBuilder sb = new StringBuilder();
        sb.append("select u.id ");
        sb.append(" , u.username ");
        sb.append(" , u.profileimageurl ");
        sb.append(" , coalesce((select 1 from subscribe where fromuserid = ? and touserid = u.id), 0) subscribeState ");
        sb.append(" , case when ? = u.id then 1 else 0 end equalUserState ");
        sb.append(" from users u inner join subscribe s on u.id = s.touserid ");
        sb.append(" where s.fromuserid = ? ");

        // 1.물을표 principalId
        // 2.물을표 principalId
        // 3.물을표 pageUserId
        // 쿼리 완성
        Query query = em.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, pageUserId);

        // 쿼리 실행 (qlrm 라이브러리 필요 = Dto 에 DB 결과를 매핑하기 위해서)
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDto> subscribeDtoList = result.list(query, SubscribeDto.class);

        return subscribeDtoList;
    }
}
