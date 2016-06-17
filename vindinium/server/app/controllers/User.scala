package controllers

import akka.pattern.ask
import com.coveo.blitz.server._
import com.coveo.blitz.server.system.Replay
import com.coveo.blitz.server.user.{User => U}
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.libs.EventSource
import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object User extends Controller {

  val API_KEY = play.api.Play.configuration.getString("vindinium.api-key").getOrElse("")

  private val form = Form(single(
    "name" -> text
      .verifying("Name is too short", _.size >= 3)
      .verifying("Name is too long", _.size <= 50)
      .verifying("Name already taken", name => Await.result(U freeName name, 1 second))
  ))

  def register(apiKey: Option[String]) = Action.async { implicit req =>
    apiKey match {
      case Some(API_KEY) => {
        form.bindFromRequest.fold(
          err => Future successful BadRequest,
          name => U make name map { user =>
            Ok(views.html.user.postRegister(user))
          }
        )
      } case _ => Future successful unauthorized
    }
  }

  private implicit val timeout = akka.util.Timeout(1.second)

  def nowPlaying(id: String) = Action.async {
    system.NowPlaying.actor ? system.NowPlaying.GetEnumeratorFor(id) mapTo
      manifest[Enumerator[List[String]]] map { enumerator =>
        val toJsonArray = Enumeratee.map[List[String]] { ids =>
          Json stringify JsArray(ids map JsString.apply)
        }
        Ok.chunked(enumerator &> toJsonArray &> EventSource()).as("text/event-stream")
      }
  }

  def show(id: String) = Action.async { req =>
    U find id flatMap {
      case None => Future successful notFoundPage
      case Some(user) => Replay.recentByUserName(user.name, 100) map { replays =>
        Ok(views.html.user.show(user, replays, None))
      }
    }
  }

  def list = Action.async { req =>
    val topNb = 100
    U top topNb map { users =>
      Ok(views.html.user.top(users, topNb))
    }
  }
}
