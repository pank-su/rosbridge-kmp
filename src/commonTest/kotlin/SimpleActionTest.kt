import com.github.thoebert.krosbridge.Ros
import com.github.thoebert.krosbridge.action.ActionFeedback
import com.github.thoebert.krosbridge.action.ActionGoal
import com.github.thoebert.krosbridge.action.ActionResult
import com.github.thoebert.krosbridge.messages.action_tutorials_interfaces.action.Fibonacci
import com.github.thoebert.krosbridge.messages.action_tutorials_interfaces.action.FibonacciResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.test.Test

@Serializable
data class RotateGoal(val theta: Float) : ActionGoal()

@Serializable
data class RotateFeedback(val delta: Float) : ActionFeedback()

@Serializable
data class RotateResult(val delta: Float) : ActionResult()


class SimpleActionTest {

    suspend fun fibAction(fibonacciAction: Fibonacci, id: String, goal: Int) {
        println(id)
        fibonacciAction.sendFeedback(listOf(1), id)
        delay(500)
        val fibs = mutableListOf(1, 1)
        fibonacciAction.sendFeedback(listOf(1, 1), id)
        delay(500)

        for (i in 1..<goal) {
            fibs.add(fibs[i - 1] + fibs[i])
            fibonacciAction.sendFeedback(fibs, id)
            delay(500)

        }
        fibonacciAction.sendResult(FibonacciResult(fibs), true, id)
    }


    @Test
    fun simpleTest() = runTest {
        val ros = Ros("localhost", port = 8080)
        ros.connect()
        val action = Fibonacci(ros, "/fibonacci")
        action.sendGoal(true, 12).collect{
            println(it)
        }
//        val action = Fibonacci(ros, "fibonacci_kotlin")
//        action.advertiseAction { fibonacciGoal, s ->
//            CoroutineScope(Dispatchers.Default).launch {
//                fibAction(action, s!!, fibonacciGoal!!.order)
//            }
//            println(fibonacciGoal)
//        }
//        while (true){
//
//        }

    }

}