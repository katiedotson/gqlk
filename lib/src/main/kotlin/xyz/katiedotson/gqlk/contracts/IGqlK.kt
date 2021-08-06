package xyz.katiedotson.gqlk.contracts

import xyz.katiedotson.gqlk.GqlKRequest

interface IGqlK {
    val path: String
    val requestBody: Any
    val type: GqlKRequestType
    fun toQueryObject(): GqlKRequest
}