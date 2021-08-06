package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetPastLaunchesRequest(
    val limit: Int = 10,
    override val path: String = "launchesPast",
    override val requestBody: GetPastLaunchesResponse = GetPastLaunchesResponse(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY
) : GqlK() {
    data class GetPastLaunchesResponse(
        val launchesPast: List<PastLaunch> = listOf(PastLaunch())
    )

    data class PastLaunch(
        val mission_name: String = "",
        val launch_date_local: String = "",
        val launch_site: LaunchSite = LaunchSite(),
        val links: Links = Links(),
        val rocket: Rocket = Rocket(),
        val ships: List<Ship> = listOf(Ship())
    )

    data class FirstStage(
        val cores: List<Core> = listOf(Core())
    )

    data class LaunchSite(
        val site_name_long: String = ""
    )

    data class Links(
        val article_link: String? = "",
        val video_link: String = ""
    )

    data class Rocket(
        val rocket_name: String = "",
        val first_stage: FirstStage = FirstStage(),
        val second_stage: SecondStage = SecondStage()
    )

    data class SecondStage(
        val block: Int = 0,
        val payloads: List<Payload> = listOf(Payload())
    )

    data class Payload(
        val payload_type: String = "",
        val payload_mass_kg: Int? = 0,
        val payload_mass_lbs: Float? = 0f
    )

    data class Ship(
        val name: String = "",
        val home_port: String = "",
        val image: String = ""
    )

    data class Core(
        val flight: Int = 0,
        val core: InnerCore = InnerCore()
    )

    data class InnerCore(
        val reuse_count: Int = 0,
        val status: String? = ""
    )

}
