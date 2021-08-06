package xyz.katiedotson.gqlk

import xyz.katiedotson.gqlk.contracts.*
import xyz.katiedotson.gqlk.serialization.GraphQlFactory
import xyz.katiedotson.gqlk.serialization.Ql

abstract class GqlK : IGqlK {
    abstract override val path: String
    abstract override val requestBody: Any
    abstract override val type: GqlKRequestType
    override fun toQueryObject(): GqlKRequest {
        val ql : IQl = Ql()
        val graphQlFactory : IGraphQlFactory = GraphQlFactory(ql)
        return when (type) {
            (GqlKRequestType.MUTATION) -> {
                val mutation = graphQlFactory.toMutation(this)
                GqlKRequest(mutation)
            }
            (GqlKRequestType.QUERY) -> {
                val query = graphQlFactory.toQuery(this)
                GqlKRequest(query)
            }
            else -> throw Exception("Query type not permitted.")
        }
    }
}