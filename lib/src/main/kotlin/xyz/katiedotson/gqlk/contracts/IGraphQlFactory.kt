package xyz.katiedotson.gqlk.contracts

interface IGraphQlFactory {
    fun toMutation(request: IGqlK): String
    fun toQuery(request: IGqlK): String
}
