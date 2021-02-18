package ru.otus.work20.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.otus.work20.domain.DbSequence;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final ReactiveMongoOperations reactiveMongoOperations;

    public Mono<Long> getReactiveSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        Mono<DbSequence> counter = reactiveMongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), DbSequence.class);
        return counter.map(dbSequence -> !Objects.isNull(dbSequence) ? dbSequence.getSeq() : 1L);
    }
}