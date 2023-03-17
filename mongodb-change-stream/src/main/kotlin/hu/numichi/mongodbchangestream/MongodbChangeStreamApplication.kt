package hu.numichi.mongodbchangestream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ChangeStreamEvent
import org.springframework.data.mongodb.core.ReactiveChangeStreamOperation.ReactiveChangeStream
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import reactor.core.publisher.Flux

@SpringBootApplication
@EnableReactiveMongoRepositories
class MongodbChangeStreamApplication {

    @Bean
    fun mongoDBChangeStream(template: ReactiveMongoTemplate): Flux<ChangeStreamEvent<ChangeStreamPoc>> {
        return template.changeStream(ChangeStreamPoc::class.java).listen()
    }
}

fun main(args: Array<String>) {
    runApplication<MongodbChangeStreamApplication>(*args)
}
