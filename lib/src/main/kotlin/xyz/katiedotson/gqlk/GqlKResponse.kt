package xyz.katiedotson.gqlk

open class GqlKResponse<T>(
    val data: T?,
    val errors: List<GraphQlError>?,
)