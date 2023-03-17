package hu.numichi.mongodbchangestream

import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChangeStreamPocRepository : CoroutineCrudRepository<ChangeStreamPoc, ObjectId>