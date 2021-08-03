package xyz.katiedotson.gqlk.serialization

import xyz.katiedotson.gqlk.contracts.IGqlK
import xyz.katiedotson.gqlk.contracts.IGraphQlFactory
import xyz.katiedotson.gqlk.contracts.IQl

class GraphQlFactory<T>(private val ql: IQl<T>) : IGraphQlFactory<T> {

    /**
     * Given a [IGqlK], returns a [String] representation of a GraphQl mutation for the class.
     */
    override fun toMutation(request: IGqlK<T>): String {
        val params = ql.buildParameters(request)
        val responseBody = ql.buildBody(request)
        // don't inline this so this is easy to debug
        val mutation = "mutation { ${request.path}($params) { $responseBody } }"
        return mutation
    }

    /**
     * Given a [IGqlK], returns a [String] representation of a GraphQl query for the class.
     */
    override fun toQuery(request: IGqlK<T>): String {
        val params = ql.buildParameters(request)
        val responseBody = ql.buildBody(request)
        // don't inline this so this is easy to debug
        val query = "{ ${request.path}($params) { $responseBody } }"
        return query
    }

}

