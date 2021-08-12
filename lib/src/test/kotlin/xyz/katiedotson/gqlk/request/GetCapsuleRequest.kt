package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetCapsuleRequest(
    val id: String,
    override val path: String = "capsule",
    override val type: GqlKRequestType = GqlKRequestType.QUERY,
    override val requestBody: GetCapsuleResponse = GetCapsuleResponse(),
) : GqlK() {
    data class GetCapsuleResponse(
        var capsule: CapsuleResponse = CapsuleResponse(),
    ) {

        data class CapsuleResponse(
            var dragon: ResponseDragon = ResponseDragon(),
            var id: String = "",
            var landings: Int = 0,
            var missions: List<CapsuleMission> = listOf(CapsuleMission()),
            var status: String = "",
            var type: String = "",
        )

        data class ResponseDragon(
            var active: Boolean = false,
            var crew_capacity: Int = 0,
            var description: String = "",
            var diameter: Distance = Distance(),
            var dry_mass_kg: Int = 0,
            var dry_mass_lb: Int = 0,
            var first_flight: String = "",
            var id: String = "",
            var name: String = "",
            var wikipedia: String = "",
        )

        data class Distance(var feet: Float = 0f, var meters: Float = 0f)

        data class CapsuleMission(var flight: Int = 0, var name: String = "")
    }
}
