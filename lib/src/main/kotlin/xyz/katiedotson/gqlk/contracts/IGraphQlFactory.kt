package xyz.katiedotson.gqlk.contracts

interface IGraphQlFactory<T> {
    fun toMutation(request: IGqlK<T>): String
    fun toQuery(request: IGqlK<T>): String
}
