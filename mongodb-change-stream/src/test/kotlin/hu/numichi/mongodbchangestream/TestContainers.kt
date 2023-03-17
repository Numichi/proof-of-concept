package hu.numichi.mongodbchangestream

import org.junit.ClassRule
import org.junit.rules.TestRule
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import java.io.File

abstract class TestContainers {

    class Containers(file: File) : DockerComposeContainer<Containers>(file)

    companion object {
        @JvmStatic
        private val containers = Containers(File("./docker/docker-compose.yml"))
            .withLocalCompose(false)
            .waitingFor("mongodb1", WaitAllStrategy())
            .waitingFor("mongodb2", WaitAllStrategy())
            .waitingFor("mongodb3", WaitAllStrategy())

        @field:ClassRule
        @field:JvmField
        val start = initMongoDBReplicaSet()

        @JvmStatic
        fun initMongoDBReplicaSet(): TestRule {
            // Start containers
            containers.start()

            // Replica Set initiate
            containers.getContainerByServiceName("mongodb1").get()
                .execInContainer("/bin/bash", "-c", """
                    mongosh --eval 'printjson(rs.initiate({
                        "_id": "dbrs",
                        "version": 1,
                        "members": [
                            {"_id": 1, "host": "mongodb1:27017", "priority": 3}, 
                            {"_id": 2, "host": "mongodb2:27017", "priority": 2}, 
                            {"_id": 3, "host": "mongodb3:27017", "priority": 1}]
                    }, {force: true}))'
                """.trimIndent().replace("\n", ""))

            // Waiting for MASTER status
            containers.getContainerByServiceName("mongodb1").get()
                .execInContainer("/bin/bash", "-c", """
                    until mongosh --eval 'printjson(rs.isMaster())' | grep ismaster | grep true > /dev/null 2>&1;do sleep 1;done
                """.trimIndent())

            return containers
        }
    }
}