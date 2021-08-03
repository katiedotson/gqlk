package xyz.katiedotson.gqlk.contracts

import xyz.katiedotson.gqlk.GqlKRequest

interface IGqlK<T> {
    val path: String?
    val requestBody: T
    val type: GqlKRequestType?
    fun toQueryObject(): GqlKRequest
}