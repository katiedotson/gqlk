package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class InsertUsersRequest(
    val objects: List<SpaceXUser> = listOf(SpaceXUser()),
    override val path: String = "insert_users",
    override val requestBody: InsertUsersResponse = InsertUsersResponse(),
    override val type: GqlKRequestType = GqlKRequestType.MUTATION
) : GqlK() {
    companion object {
        fun toGqlUser(id: String, name: String, rocket: String): SpaceXUser {
            return SpaceXUser(id, name, rocket)
        }
    }
    data class SpaceXUser(val id: String = "", val name: String = "", val rocket: String = "")
    data class InsertUsersResponse(val affected_rows: Int = 0)
}