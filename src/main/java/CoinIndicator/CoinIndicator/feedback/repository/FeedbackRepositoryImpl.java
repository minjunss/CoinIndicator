package CoinIndicator.CoinIndicator.feedback.repository;

import CoinIndicator.CoinIndicator.feedback.dto.FeedbackResponse;
import CoinIndicator.CoinIndicator.feedback.dto.QFeedbackResponse;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static CoinIndicator.CoinIndicator.feedback.entitiy.QFeedback.feedback;

@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FeedbackResponse> findAllFeedback(Pageable pageable) {

        List<FeedbackResponse> content = jpaQueryFactory
                .select(new QFeedbackResponse(feedback))
                .from(feedback)
                .orderBy(feedback.createdTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(feedback.count())
                .from(feedback);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
