import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        val client = HttpClient(Apache) {
        }
        client.get<String>("http://127.0.0.1:11111/")
    }
}
