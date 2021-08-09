# gqlk
A concise toolkit for generating GraphQl requests in Kotlin with Moshi and Retrofit

[View Documentation](https://katiedotson.github.io/gqlk/)

## Usage
#### Example request body (see code comments for details):
```kotlin
// Be sure to extend GqlK
data class GetWidgetsRequest(
  // add query paramters as regular properties
  val queryParam: String,
  // override the path value for the root of the query/mutation
  override val path: String = "getWidgets",
  // set type to GqlKRequestType.QUERY or GqlKRequestType.MUTATION
  override val type: GqlKRequestType = GqlKRequestType.QUERY,
  // default constructor must work for response (provide default values when constructing classes)
  override val requestBody: GetWidgetsResponse = GetWidgetsResponse() 
) : GqlK()
```
#### Example response body (see code comments for details):
```kotlin
data class GetWidgetsResponse(
  // name the properties so they match the query body; 
  // be sure to initialize a list with a default instance
  val widgets: List<WidgetResponse> = listOf(WidgetResponse()), 
) {
  data class WidgetResponse(val foo: String = "", val bar: Int = 0)
}
```
#### GraphQl Query generated for the above example:
```graphql
{
  # "getWidgets" is the path property of the request
  getWidgets (queryParam: "parameter-value") { 
    # the name of the property on GetWidgetsResponse
    widgets { 
      # properties of the WidgetResponse class
      foo
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
// create an instance using any parameters and call .toQueryObject()
val getWidgetsRequestObj = GetWidgetsRequest(GetWidgetsRequest("parameter-value")).toQueryObject() 
// get GqlKResponse, then any errors at response.errors or the data at response.data
val response = service.getWidgets(getWidgetsRequest) 
```

### Differing response body and request body
Sometimes a request needs a different request body than its response body. In the example below, the request body will be the `Launchpad` class, but the response will be returned as the `GetLaunchpadResponse`. 

#### Request class example
```kotlin
data class GetLaunchpadRequest(
    val id: String = "vafb_slc_3w",
    override val path: String = "launchpad",
    override val requestBody: Launchpad = Launchpad(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY
) : GqlK() {
    data class GetLaunchpadResponse(
        val launchpad: Launchpad = Launchpad() // RESPONSE
    )
    data class Launchpad(val details: String = "")
}
```
#### Usage in the service class
```kotlin
@POST(GRAPHQL)
// the type param of the response is GetLaunchpadResponse, not the Launchpad class that is the requestBody
suspend fun getLaundpad(@Body getLaunchpadRequest: GqlKRequest): GqlKResponse<GetLaunchpadResponse>
```

## Examples
See [https://github.com/katiedotson/gqlk/tree/main/lib/src/test/kotlin/xyz/katiedotson/gqlk]
