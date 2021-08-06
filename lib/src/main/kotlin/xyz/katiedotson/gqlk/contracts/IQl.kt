package xyz.katiedotson.gqlk.contracts

interface IQl {
    fun  buildBody(request: IGqlK) : String
    fun  buildParameters(request: IGqlK): String
}