package xyz.katiedotson.gqlk

class GraphQlError(
    val message: String,
    val locations: List<ErrorLocation>,
    val extensions: Extensions
) {
    class ErrorLocation(val line: String, val column: String)
    class Extensions(val code: String)
}