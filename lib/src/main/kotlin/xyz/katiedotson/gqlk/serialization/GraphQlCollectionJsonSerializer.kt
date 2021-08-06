package xyz.katiedotson.gqlk.serialization

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException
import java.lang.reflect.Type
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

/**
 * Serialize [Collection]s as if they were Objects.
 */
class GraphQlCollectionJsonSerializer : JsonSerializer<List<Any>> {

    /**
     *
     */
    override fun serialize(
        src: List<Any>?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?,
    ): JsonElement {

        val obj: Any?

        if (src == null) {
            throw NullPointerException("Lists must be initialized.")
        }
        try {
            obj = src[0]
            val memberProperties = obj::class.memberProperties
            return serialize(memberProperties, obj)
        } catch (e: IndexOutOfBoundsException) {
            throw NullPointerException("Lists must be initialized with one instance of their type.")
        } catch (e: NullPointerException) {
            throw NullPointerException("Lists must be initialized with one instance of their type. The type must also be initialized.")
        }
    }

    /**
     * Instead of serializing the list as an array, treat it as an object.
     */
    private fun serialize(memberProperties: Collection<KProperty1<out Any, *>>, obj: Any?): JsonElement {
        val jsonObject = JsonObject()
        memberProperties.forEach { objecT ->
            when {
                objecT.isBasic() -> jsonObject.addProperty(objecT.name, "")
                objecT.isList() -> jsonObject.add(objecT.name, createElement(objecT.getter.call(obj) as List<*>))
                else -> {
                    val call = objecT.getter.call(obj)!!
                    jsonObject.add(objecT.name, createElement(call))
                }
            }
        }
        return jsonObject
    }

    /**
     * Create a Json Element for an object.
     */
    private fun createElement(call: Any): JsonElement {
        val properties = call::class.memberProperties
        return serialize(properties, call)
    }

    /**
     * Create a Json Element for a list.
     */
    private fun createElement(list: List<*>): JsonElement {
        try {
            val item = list[0] ?: throw Exception("Lists must be initialized with at least one instance of their type.")
            return serialize(item::class.memberProperties, item)
        } catch (e: IndexOutOfBoundsException) {
            throw Exception("Lists must be initialized with an object.")
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Is [Int], [Float], [Double], [String], or [Enum]. Check for both primitive and wrapper classes.
     */
    private fun KProperty1<out Any, *>.isBasic(): Boolean {
        return primitives.contains(this.returnType.javaType)
    }

    /**
     * Check if a property is a subclass of [Collection]
     */
    private fun <T, V> KProperty1<T, V>.isList(): Boolean {
        try {
            return this.returnType.jvmErasure.isSubclassOf(Collection::class)
        } catch (e: NullPointerException) {
            throw Exception("Problem when checking if ${this.javaClass.name} was a list.")
        }
    }

    companion object {
        val primitives = listOf<Class<*>>(
            Int::class.java,
            Integer::class.javaObjectType,
            String::class.java,
            String::class.javaObjectType,
            Enum::class.java,
            Enum::class.javaObjectType,
            Float::class.java,
            Float::class.javaObjectType,
            Double::class.java,
            Double::class.javaObjectType
        )
    }
}
