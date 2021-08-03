package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetCoreRequest(
    val id: String = "",
    override val path: String = "core",
    override val type: GqlKRequestType = GqlKRequestType.QUERY,
    override val requestBody: GetCoreResponse = GetCoreResponse(),
) : GqlK<GetCoreRequest.GetCoreResponse>() {
    data class GetCoreResponse(val id: NonexistentProperty = NonexistentProperty())
    data class NonexistentProperty(val nonexistent: String = "")
}