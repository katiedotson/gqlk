package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetCountriesRequest(
    val filter: CountryFilter = CountryFilter(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY,
    override val path: String = "countries",
    override val requestBody: GetCountriesResponse = GetCountriesResponse(),
) : GqlK() {
    companion object {
        fun countryFilter(continentEq: String): CountryFilter {
            return CountryFilter(CountryFilterInput(continentEq))
        }
    }

    data class CountryFilter(val continent: CountryFilterInput = CountryFilterInput())
    data class CountryFilterInput(val eq: String = "")
    data class GetCountriesResponse(
        val countries: List<CountriesResponse> = listOf(CountriesResponse()),
    )

    data class CountriesResponse(
        val native: String = "",
        val capital: String = "",
        val name: String = "",
        val currency: String = "",
        val languages: List<CountriesResponseLanguage> = listOf(CountriesResponseLanguage()),
        val continent: CountriesResponseContinent = CountriesResponseContinent(),
    )

    data class CountriesResponseContinent(val code: String = "", val name: String = "")

    data class CountriesResponseLanguage(val name: String = "")
}
