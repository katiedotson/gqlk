package xyz.katiedotson.gqlk

import xyz.katiedotson.gqlk.request.GetCapsuleRequest
import xyz.katiedotson.gqlk.request.GetCoreRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.katiedotson.gqlk.request.GetLaunchpadRequest
import xyz.katiedotson.gqlk.request.GetPastLaunchesRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpacexRepositoryTest {

    private lateinit var service: ApiService

    @BeforeAll
    fun setUp() {
        service = ApiServiceFactory.buildApiService("https://api.spacex.land/")
    }

    /**
     * Test a request will a single parameter that returns an object with non-primitive properties and a list.
     */
    @Test
    fun when_querySent_then_expectedResponseReturned() = runBlocking {

        val capsuleId = "C105"
        val obj = GetCapsuleRequest(capsuleId).toQueryObject()
        val response = service.getCapsuleById(obj)
        val domainObj = response.data?.toDomain()
            ?: throw Exception("Get Capsule Request test failed. Response.data is null. Check the query.")

        assert(capsuleId == domainObj.id)
        assert(domainObj.dragon.active)
        assert(0 == domainObj.dragon.crewCapacity)
        assert(1 == domainObj.landings)
        assert("unknown" == domainObj.status)
        assert("Dragon 1.1" == domainObj.type)
        assert(14 == domainObj.missions[0].flight)
        assert("CRS-3" == domainObj.missions[0].name)
    }

    /**
     * Test that when an error is received, the response body is still returned and can be used.
     */
    @Test
    fun when_errorReceived_then_errorHandled() = runBlocking {
        val coreId = "e"
        val expectedMessage = "Cannot query field \"nonexistent\" on type \"Core\"."
        val locationsLine = "1"
        val locationsColumn = "22"
        val extensionsCode = "GRAPHQL_VALIDATION_FAILED"

        val obj = GetCoreRequest(coreId).toQueryObject()
        val response = service.getCoreById(obj)
        val error = response.errors!![0]

        assert(error.message == expectedMessage)
        assert(error.locations[0].line == locationsLine)
        assert(error.locations[0].column == locationsColumn)
        assert(error.extensions.code == extensionsCode)
    }

    @Test
    fun when_complexQuerySubmitted_then_correctResponseReceived() = runBlocking {
        val expectedMissionName = "Starlink-15 (v1.0)"
        val expectedLaunchDate = "2020-10-24T11:31:00-04:00"
        val expectedLaunchSiteName = "Cape Canaveral Air Force Station Space Launch Complex 40"
        val expectedLinkArticle = null
        val expectedLinkVideo = "https://youtu.be/J442-ti-Dhg"
        val expectedRocketName = "Falcon 9"
        val expectedFirstStageCoresSize = 1
        val expectedFirstStageCoreFlight = 7
        val expectedFirstStageCoreCoreReuseCount = 6
        val expectedFirstStageCoreCoreStatus = "unknown"
        val expectedSecondStageBlock = 5
        val expectedSecondStagePayloadsSize = 1
        val expectedSecondStagePayloadType = "Satellite"
        val expectedSecondStagePayloadMassKg = 15400
        val expectedSecondStagePayloadMassLbs = 33951.2f
        val expectedShipsSize = 4
        val expectedShipName = "GO Ms Tree"
        val expectedShipHomePort = "Port Canaveral"
        val expectedShipImage = "https://i.imgur.com/MtEgYbY.jpg"

        val obj = GetPastLaunchesRequest().toQueryObject()
        var response = service.getPastLaunches(obj).data

        assert(response != null)
        response = response!!
        assert(response.launchesPast.size == 10)
        val launch = response.launchesPast[0]
        assert(launch.mission_name == expectedMissionName)
        assert(launch.launch_date_local == expectedLaunchDate)
        assert(launch.launch_site.site_name_long == expectedLaunchSiteName)
        assert(launch.links.article_link == expectedLinkArticle)
        assert(launch.links.video_link == expectedLinkVideo)
        assert(launch.rocket.rocket_name == expectedRocketName)
        assert(launch.rocket.first_stage.cores.size == expectedFirstStageCoresSize)
        assert(launch.rocket.first_stage.cores[0].flight == expectedFirstStageCoreFlight)
        assert(launch.rocket.first_stage.cores[0].core.reuse_count == expectedFirstStageCoreCoreReuseCount)
        assert(launch.rocket.first_stage.cores[0].core.status == expectedFirstStageCoreCoreStatus)
        assert(launch.rocket.second_stage.block == expectedSecondStageBlock)
        assert(launch.rocket.second_stage.payloads.size == expectedSecondStagePayloadsSize)
        assert(launch.rocket.second_stage.payloads[0].payload_type == expectedSecondStagePayloadType)
        assert(launch.rocket.second_stage.payloads[0].payload_mass_kg == expectedSecondStagePayloadMassKg)
        assert(launch.rocket.second_stage.payloads[0].payload_mass_lbs == expectedSecondStagePayloadMassLbs)
        assert(launch.ships.size == expectedShipsSize)
        assert(launch.ships[0].home_port == expectedShipHomePort)
        assert(launch.ships[0].image == expectedShipImage)
        assert(launch.ships[0].name == expectedShipName)
    }


    /**
     * Request body is of type
     */
    @Test
    fun when_queryClassIsDifferentFromResponseClass_then_correctResponseReceived() = runBlocking {
        val obj = GetLaunchpadRequest().toQueryObject()
        val response = service.getLaunchpad(obj)

        assert(response.data != null)
        val expectedDetails =
            "SpaceX original west coast launch pad for Falcon 1. " +
                    "Performed a static fire but was never used for a " +
                    "launch and abandoned due to scheduling conflicts."
        assert(response.data!!.launchpad.details == expectedDetails)
    }
}