import com.allantl.jira4s.v2.{JiraMultiTenantClient, JiraSingleTenantClient}
import com.softwaremill.sttp.HttpURLConnectionBackend

object Server1 extends App {

  implicit val sttpBackend = HttpURLConnectionBackend()

  val singleTenantClient = JiraSingleTenantClient()
  val multiTenantClient = JiraMultiTenantClient()

}
