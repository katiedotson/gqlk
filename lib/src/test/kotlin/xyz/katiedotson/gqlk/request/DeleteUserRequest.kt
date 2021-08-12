package xyz.katiedotson.gqlk.request

import com.squareup.moshi.JsonClass
import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

@JsonClass(generateAdapter = true)
data class DeleteUserRequest(
    val where: WhereClause = WhereClause(),
    override val path: String = "delete_users",
    override val requestBody: DeleteUserResponse = DeleteUserResponse(),
    override val type: GqlKRequestType = GqlKRequestType.MUTATION
) : GqlK() {

    companion object {
        fun toWhereClause(id: String): WhereClause {
            return WhereClause(WhereClause.Expression(id))
        }
    }

    data class WhereClause(val id: Expression = Expression()) {
        data class Expression(val _eq: String = "")
    }

    @JsonClass(generateAdapter = true)
    data class DeleteUserResponse(val affected_rows: Int = 0)
}