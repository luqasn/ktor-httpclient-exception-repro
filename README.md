# ktor-httpclient-exception-repro

Contains repro cases for exception thrown in HttpClients not being recovered correctly
to include the call site where `runBlocking{}` is called.

Ktor version *1.3.0-beta-1*

Kotlin version *1.3.50*

Running:
```
./gradlew testCIO
./gradlew testApache
```

```kotlin
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
```

## Expected

We see an exception that contains file and line references
to `Apache.kt` and `CIO.kt`, respectively.

## Actual result
We see
```
❯ ./gradlew testCIO

> Task :testCIO FAILED
Exception in thread "main" java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)
        at java.base/sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:779)
        at io.ktor.network.sockets.SocketImpl.connect$ktor_network(SocketImpl.kt:34)
        at io.ktor.network.sockets.SocketImpl$connect$1.invokeSuspend(SocketImpl.kt)
        at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
        at kotlinx.coroutines.DispatchedTask.run(Dispatched.kt:241)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
        at java.base/java.lang.Thread.run(Thread.java:834)

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':testCIO'.
> Process 'command '/Library/Java/JavaVirtualMachines/openjdk-11.0.2.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 1s
2 actionable tasks: 1 executed, 1 up-to-date
```
and
```
❯ ./gradlew testApache

> Task :testApache FAILED
Exception in thread "main" java.net.ConnectException: Connection refused
        at java.base/sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)
        at java.base/sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:779)
        at org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor.processEvent(DefaultConnectingIOReactor.java:171)
        at org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor.processEvents(DefaultConnectingIOReactor.java:145)
        at org.apache.http.impl.nio.reactor.AbstractMultiworkerIOReactor.execute(AbstractMultiworkerIOReactor.java:348)
        at org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager.execute(PoolingNHttpClientConnectionManager.java:221)
        at org.apache.http.impl.nio.client.CloseableHttpAsyncClientBase$1.run(CloseableHttpAsyncClientBase.java:64)
        at java.base/java.lang.Thread.run(Thread.java:834)

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':testApache'.
> Process 'command '/Library/Java/JavaVirtualMachines/openjdk-11.0.2.jdk/Contents/Home/bin/java'' finished with non-zero exit value 1

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 2s
2 actionable tasks: 1 executed, 1 up-to-date
```
