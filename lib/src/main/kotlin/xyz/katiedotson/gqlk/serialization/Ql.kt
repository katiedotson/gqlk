package xyz.katiedotson.gqlk.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import xyz.katiedotson.gqlk.contracts.IGqlK
import xyz.katiedotson.gqlk.contracts.IQl
import kotlin.reflect.full.memberProperties

class Ql : IQl {

    private var gson: Gson

    init {
        val gsonBuilder = GsonBuilder()
        val serializer = GraphQlCollectionJsonSerializer()
        gsonBuilder.registerTypeAdapter(List::class.java, serializer)
        gson = gsonBuilder.create()
    }

    override fun buildBody(request: IGqlK): String {
        val responseObjKey = request.requestBody::class.memberProperties.first().name

        val responseMap = request.requestBody.serializeToMap()

        val map = responseMap[responseObjKey] as? Map<String, Any>
        return map?.entries?.fold("") { accum, entry ->
            build(entry, accum, "")
        } ?: return buildBodyForSingleObject(responseMap)
    }

    override fun buildParameters(request: IGqlK): String {
        val filtered = request::class.memberProperties
            .filter { mem ->
                !IGqlK::class.members
                    .map { it.name }.contains(mem.name)
            }
        return filtered
            .foldIndexed("") { index, accumulator, member ->
                var comma = ""
                if (index < filtered.count() - 1) comma = ","
                val property = wrapForTypeInQuery(member.getter.call(request))

                "$accumulator ${member.name}: $property $comma"
            }
    }

    private fun buildBodyForSingleObject(responseMap: Map<String, Any>?): String {
        return responseMap?.entries?.fold("") { accum, entry ->
            "$accum ${entry.key}"
        } ?: throw Exception("Unable to build response body.")
    }

    private fun build(
        entry: Map.Entry<String, Any>,
        accumulator: String,
        initialValue: String,
        separator: String = ""
    ) =
        when {
            entry.value.isMap() -> {
                val map = entry.value as Map<String, Any>
                val entriesForMap = buildQlForMap(initialValue, map, separator)
                "$accumulator ${entry.key} $separator { $entriesForMap }"
            }
            entry.value.isList() -> {
                val list = entry.value as List<String>
                val listEntries = list.joinToString { " " }
                "$accumulator  ${entry.key} $separator { $listEntries }"
            }
            // The separator will be : if this is for parameters, so the value will need to be wrapped
            (separator == ":") -> {
                val value = wrapForTypeInQuery(entry.value)
                "$accumulator ${entry.key} : $value"
            }
            else -> "$accumulator ${entry.key}"
        }

    private fun buildQlForMap(initialValue: String, map: Map<String, Any>, separator: String): String {
        return map.entries.fold(initialValue) { accumulator, entry ->
            build(entry, accumulator, initialValue, separator)
        }
    }

    private fun buildQlForObjInQuery(map: Map<String, Any>): String {
        val queryBody = map.entries.fold("") { accum, entry ->
            build(entry, accum, "", ":")
        }
        return "{ $queryBody }"
    }

    private fun wrapForTypeInQuery(property: Any?): String {
        val initializationException = Exception("Properties must be initialized.")
        return when (property) {
            is Int? -> property?.toString() ?: throw initializationException
            is Int -> property.toString()
            is Float? -> property?.toString() ?: throw initializationException
            is Float -> property.toString()
            is Double? -> property?.toString() ?: throw initializationException
            is Double -> property.toString()
            (property is String? && property != null) -> "\"${property}\""
            (property is String? && property == null) -> throw initializationException
            is String -> "\"${property}\""
            is Enum<*>? -> property?.toString() ?: throw initializationException
            is Enum<*> -> property.toString()
            (property is List<*>?) -> throw initializationException
            is List<*> -> buildForListInQuery(property)
            else -> buildQlForObjInQuery(property.serializeToMap())
        }
    }

    private fun buildForListInQuery(property: List<*>): String {
        val listString =  property.fold("") { accum, item ->
            "$accum ${wrapForTypeInQuery(item)}"
        }

        return "[ $listString ]"
    }

    private inline fun <I, reified O> I.convert(): O {
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<O>() {}.type)
    }

    private fun Any.isList(): Boolean {
        return this as? List<String> != null
    }

    private fun Any.isMap(): Boolean {
        return this as? Map<String, Any> != null
    }

    private fun <T> T.serializeToMap(): Map<String, Any> {
        return convert()
    }
}