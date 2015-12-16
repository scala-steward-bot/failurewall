package failurewall.test

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Try

object TestHelper {
  def await[A](future: Future[A]): Try[A] = Try(Await.result(future, 50.seconds))

  def awaitAssert(condition: => Boolean, until: FiniteDuration = 50.millis): Unit = {
    val trial = (1 to 10).foldLeft(condition) {
      case (true, _) => true
      case (false, _) =>
        TimeUnit.NANOSECONDS.sleep(until.toNanos / 10)
        condition
    }
    if (!trial) assert(condition)
  }

  def assertWhile(condition: => Boolean, until: FiniteDuration = 50.millis): Unit = {
    val trial = (1 to 10).foldLeft(condition) {
      case (true, _) =>
        TimeUnit.NANOSECONDS.sleep(until.toNanos / 10)
        condition
      case (false, _) => false
    }
    assert(trial)
  }
}
