import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("Main")
fun main() {
    // this option enables stacktrace recovery:
    // -Dkotlinx.coroutines.debug=on

    // this required kotlinx.coroutines.debug to be on the classpath
    // according to the docs, but when running in IntelliJ that seems to be
    // the case automatically
    runBlocking {
        try {
            callThrowableSuspendFun()
        } catch (ex: Exception) {
            logger.error("Error while execute", ex)
        }
    }
}

suspend fun changeContextAndThrow(): String {
    logger.info("Call function changeContextAndThrow")
    withContext(Dispatchers.IO) {
        throw IllegalArgumentException("Throw from changeContextAndThrow")
    }
}

suspend fun callThrowableSuspendFun(): String {
    logger.info("Call function callThrowableSuspendFun")
    val b = true
    if (b)
        return changeContextAndThrow()
        // this complete method (callThrowableSuspendFun) will not
        // be part of the stack trace because the Kotlin compiler
        // inlines trailing suspend functions according to
        // https://github.com/Kotlin/kotlinx.coroutines/issues/74#issuecomment-603077880
        // workaround: make the suspend fun non-trailing by doing something afterwards,
        // even an empty also {} works...
//                .also {  }
    else
        return changeContextAndThrow()
}