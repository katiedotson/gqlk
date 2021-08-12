package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetLanguageRequest(
    val code: String,
    override val path: String = "language",
    override val requestBody: Any = LanguageResponse(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY
) : GqlK() {
    data class LanguageResponse(val language: Language = Language()) {
        data class Language(val name: String = "")
    }
}