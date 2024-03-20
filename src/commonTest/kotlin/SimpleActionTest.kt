import com.github.thoebert.krosbridge.*
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

    @Test
    fun simpleTurtleTest() = runTest {
        val ros = Ros("localhost", port = 8080)
        val action = Action(
            ros,
            "/turtle1/rotate_absolute",
            type = "turtlesim/action/RotateAbsolute",
            goalClz = RotateGoal::class,
            feedbackClz = RotateFeedback::class,
            resultClz = RotateResult::class
        )
        action.sendGoalGeneric(RotateGoal(1f))
    }
}