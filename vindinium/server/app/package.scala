package com.coveo.blitz

package object server {

  implicit final class debugKCombinatorAny[A](a: A) {
    def pp: A = { println(s"*DEBUG* $a"); a }
  }

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit final class debugKCombinatorFutureAny[A](fua: Future[A]) {
    def thenPp: Future[A] = {
      fua onComplete { case result ⇒ result.pp }
      fua
    }
  }

  def notFoundPage = play.api.mvc.Results.NotFound(views.html.notFound())
  def unauthorized = play.api.mvc.Results.Unauthorized("Unauthorized!")

  private[server] def charInt(c: Char): Option[Int] = try {
    Some(java.lang.Integer.parseInt(c.toString))
  }
  catch {
    case _: Exception => None
  }
}
