package ru.otus.work25.events;

import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.acls.domain.MongoAcl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.work25.domain.Book;
import ru.otus.work25.repositories.CommentRepository;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@RequiredArgsConstructor
public class BookCascadeEventsListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = Long.parseLong(source.get("_id").toString());
        commentRepository.deleteAllByBookId(id);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Book> event) {
        super.onAfterSave(event);
        val source = event.getSource();
        val id = source.getId();

        Criteria where = Criteria.where("instanceId").is(id).and("className").is(Book.class.getCanonicalName());
        boolean isAclExists = mongoTemplate.exists(query(where), MongoAcl.class);

        if(!isAclExists) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("ACL");
            UUID corrId = UUID.randomUUID();
            mongoCollection.insertOne(Document.parse("{'_id' : '"+corrId.toString()+"', '_class' : 'org.springframework.security.acls.domain.MongoAcl', " +
                    "'className' : '" + Book.class.getCanonicalName() + "', 'instanceId' : " + id + ", 'owner' : {'name': '" + userDetails.getUsername() + "', 'isPrincipal': true}, 'inheritPermissions' : true, " +
                    "'permissions' : [" +
                    "{'_id' : 'dbf4dcb0-70f4-48a5-92b0-d4c782af7498', 'sid' : {'name': 'admin', 'isPrincipal': true}, 'permission' : 1, 'granting' : true, 'auditFailure' : false, 'auditSuccess' : false}, " +
                    "{'_id' : 'dbf4dcb0-70f4-48a5-92b0-d4c782af7411', 'sid' : {'name': 'editor', 'isPrincipal': true}, 'permission' : 2, 'granting' : true, 'auditFailure' : false, 'auditSuccess' : false}, "+
                    "{'_id' : 'dbf4dcb0-70f4-48a5-92b0-d4c782af7490', 'sid' : {'name': 'ROLE_USER', 'isPrincipal': false}, 'permission' : 1, 'granting' : true, 'auditFailure' : false, 'auditSuccess' : false}, " +
                    "{'_id' : 'dbf4dcb0-70f4-48a5-92b0-d4c782af7123', 'sid' : {'name': 'ROLE_ANON', 'isPrincipal': false}, 'permission' : 1, 'granting' : true, 'auditFailure' : false, 'auditSuccess' : false}" +
                    "]}"));
        }
      }
}

