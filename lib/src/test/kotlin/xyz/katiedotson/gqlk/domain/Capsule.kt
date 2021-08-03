package xyz.katiedotson.gqlk.domain

data class Capsule(
    val dragon: Dragon,
    val id: String,
    val landings: Int,
    val status: String,
    val type: String,
    val missions: List<Mission>,
)

data class Dragon(val active: Boolean, val crewCapacity: Int)

data class Mission(val flight: Int, val name: String)
