package com.coveo.blitz.server

package object system {

  type Token = String
  type GameId = String

  def notFound(msg: String) = akka.actor.Status.Failure(NotFoundException(msg))

  def inputPromise(to: akka.actor.ActorRef)(implicit ctx: scala.concurrent.ExecutionContext) = {
    val p = scala.concurrent.Promise[PlayerInput]
    p.future onSuccess {
      case x => to ! x
    }
    p
  }
}
