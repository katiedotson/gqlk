# gqlk
A concise toolkit for generating GraphQl requests in Kotlin with Moshi and Retrofit

## Usage
#### Example request body (see code comments for details):
```kotlin
data class GetWidgetsRequest(
  val queryParam: String, // add query paramters as regular properties
  override val path: String = "getWidgets", // override the path value for the root of the query/mutation
  override val type: GqlKRequestType = GqlKRequestType.QUERY, // or GqlKRequestType.MUTATION
  override val requestBody: GetWidgetsResponse = GetWidgetsResponse() // default constructor must work for response (provide default values when constructing classes)
) : GqlK<GetWidgetsResponse>() // Be sure to extend GqlK<T>, using the requestBody type as the type parameter
```
#### Example response body (see code comments for details):
```kotlin
data class GetWidgetsResponse(
  val widgets: List<WidgetResponse> = listOf(WidgetResponse()), // name the properties so they match the query body; be sure to initialize a list with a default instance
) {
  data class WidgetResponse(val foo: String = "", val bar: Int = 0)
}
```
#### GraphQl Query generated for the above example:
```kotlin
{
  getWidgets (queryParam: "parameter-value") { // "getWidgets" == path property of request
    widgets { // the name of the property on GetWidgetsResponse
      foo // properties of the WidgetResponse class
      bar
    }
  }
}
```

#### Example call using Retrofit and Moshi
##### Retrofit Service
```kotlin
@POST(GRAPHQL)
suspend fun getWidgets(@Body getWidgetsRequest: GqlKRequest): GqlKResponse<GetWidgetsResponse>
```
##### Repository
```kotlin
val getWidgetsRequestObj = GetWidgetsRequest(GetWidgetsRequest("parameter-value")).toQueryObject() // create an instance using any parameters and call .toQueryObject()
val response = service.getWidgets(getWidgetsRequest) // get GqlKResponse, then any errors at response.errors or the data at response.data
```

### Differing response body and request body
```kotlin
data class GetLaunchpadRequest(
    val id: String = "vafb_slc_3w",
    override val path: String = "launchpad",
    override val requestBody: Launchpad = Launchpad(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY
) : GqlK<GetLaunchpadRequest.Launchpad>() {

    data class GetLaunchpadResponse(
        val launchpad: Launchpad = Launchpad() // RESPONSE
    )

    data class Launchpad(val details: String = "")
}
```
