package xyz.katiedotson.gqlk.contracts

interface IQl<T> {
    fun  buildBody(request: IGqlK<T>) : String
    fun  buildParameters(request: IGqlK<T>): String
}