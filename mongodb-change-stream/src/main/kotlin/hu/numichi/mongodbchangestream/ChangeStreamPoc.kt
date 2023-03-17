package hu.numichi.mongodbchangestream

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId


@Document("change-stream-poc")
data class ChangeStreamPoc(
    @field:MongoId
    val id: ObjectId? = null,

    val data: String
)