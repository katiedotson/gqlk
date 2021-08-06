package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType
import xyz.katiedotson.gqlk.domain.Capsule
import xyz.katiedotson.gqlk.domain.Dragon
import xyz.katiedotson.gqlk.domain.Mission

data class GetCapsuleRequest(
    val id: String,
    override val path: String = "capsule",
    override val type: GqlKRequestType = GqlKRequestType.QUERY,
    override val requestBody: GetCapsuleResponse = GetCapsuleResponse(),
) : GqlK() {
    data class GetCapsuleResponse(
        var capsule: CapsuleResponse = CapsuleResponse(),
    ) {
        fun toDomain(): Capsule {
            return Capsule(
                dragon = capsule.dragon.toDomain(),
                capsule.id,
                capsule.landings,
                capsule.status,
                capsule.type,
                CapsuleMission.toDomain(capsule.missions)
            )
        }

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
        ) {
            fun toDomain(): Dragon {
                return Dragon(active, crew_capacity)
            }
        }

        data class Distance(var feet: Float = 0f, var meters: Float = 0f)

        data class CapsuleMission(var flight: Int = 0, var name: String = "") {
            companion object {
                fun toDomain(missions: List<CapsuleMission>): List<Mission> {
                    return missions.map {
                        Mission(it.flight, it.name)
                    }
                }
            }
        }
    }
}
