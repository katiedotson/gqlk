package xyz.katiedotson.gqlk

import xyz.katiedotson.gqlk.request.GetCountriesRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.katiedotson.gqlk.request.GetLanguageRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CountriesRepositoryTest {
    private lateinit var service: ApiService

    @BeforeAll
    fun setUp() {
        service = ApiServiceFactory.buildApiService("https://countries.trevorblades.com")
    }

    @Test
    fun `Request with an object in the query and whose response is a list`() = runBlocking {
        val continent = "EU"
        val obj = GetCountriesRequest(GetCountriesRequest.countryFilter(continent)).toQueryObject()
        val response = service.getCountries(obj)
        val france = response.data?.countries?.find { it.name == "France" }
            ?: throw Exception("France not found. Either response.data or response.data.countries is null, or France is no longer a country. :/")

        assert(france.capital == "Paris")
        assert(france.native == "France")
        assert(france.currency == "EUR")
        assert(france.languages.first().name == "French")
        assert(france.continent.code == continent)
        assert(france.continent.name == "Europe")
    }

    @Test
    fun `Simple query with object response`() = runBlocking {
        val languageCode = "en"
        val expectedName = "English"
        val obj = GetLanguageRequest(languageCode).toQueryObject()
        val response = service.getLanguage(obj)
        val languageName = response.data?.language?.name ?: throw Exception("Language data was not returned properly. Check the request.")

        assert(languageName == expectedName)
    }
}
