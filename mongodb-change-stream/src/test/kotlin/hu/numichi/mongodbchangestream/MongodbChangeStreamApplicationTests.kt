package hu.numichi.mongodbchangestream

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ChangeStreamEvent
import reactor.core.publisher.Flux
import java.time.Duration

@SpringBootTest
@ExperimentalCoroutinesApi
class MongodbChangeStreamApplicationTests : TestContainers() {

    @Autowired
    lateinit var  changeStreamEvent: Flux<ChangeStreamEvent<ChangeStreamPoc>>

    @Autowired
    lateinit var repository: ChangeStreamPocRepository

    @Test
    fun contextLoads() {
        var event: ChangeStreamEvent<ChangeStreamPoc>? = null
        changeStreamEvent.subscribe { event = it }

        runTest {
            val record = ChangeStreamPoc(data = "banana")

            repository.save(record)

            Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted {
                assertNull(record.id)
                assertNotNull(event?.body?.id)
                assertEquals(record.data, event?.body?.data)
            }
        }
    }

}
