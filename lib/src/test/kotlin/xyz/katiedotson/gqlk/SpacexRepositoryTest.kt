package xyz.katiedotson.gqlk

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.katiedotson.gqlk.request.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpacexRepositoryTest {

    private lateinit var service: ApiService

    @BeforeAll
    fun setUp() {
        service = ApiServiceFactory.buildApiService("https://api.spacex.land/")
    }

    @Test
    fun `Request with single parameter retruns object with non-primitive properties and a list`() = runBlocking {

        val capsuleId = "C105"
        val obj = GetCapsuleRequest(capsuleId).toQueryObject()
        val response = service.getCapsuleById(obj).data
            ?: throw Exception("Get Capsule Request test failed. Response.data is null. Check the query.")

        assert(capsuleId == response.capsule.id)
        assert(response.capsule.dragon.active)
        assert(0 == response.capsule.dragon.crew_capacity)
        assert(1 == response.capsule.landings)
        assert("unknown" == response.capsule.status)
        assert("Dragon 1.1" == response.capsule.type)
        assert(14 == response.capsule.missions[0].flight)
        assert("CRS-3" == response.capsule.missions[0].name)
    }

    @Test
    fun `Errors handled`() = runBlocking {
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
    fun `Complex query`() = runBlocking {
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

    @Test
    fun `Query class is different than response class`() = runBlocking {
        val obj = GetLaunchpadRequest().toQueryObject()
        val response = service.getLaunchpad(obj)

        assert(response.data != null)
        val expectedDetails =
            "SpaceX original west coast launch pad for Falcon 1. " +
                    "Performed a static fire but was never used for a " +
                    "launch and abandoned due to scheduling conflicts."
        assert(response.data!!.launchpad.details == expectedDetails)
    }

    @Test
    fun `Mutation with list insert and delete with where clause`() = runBlocking {
        val userId = "f71c633d-dd98-4841-90a4-ab25304c0762"
        val frankS = InsertUsersRequest.toGqlUser(userId, "Frank S.", "To the moon")
        val list = listOf(frankS)
        val obj = InsertUsersRequest(list).toQueryObject()
        val response = service.insertUsers(obj)
        assert(response.errors == null)

        if (response.errors == null) {
            val deleteObj = DeleteUserRequest(DeleteUserRequest.toWhereClause(userId)).toQueryObject()
            val deleteResponse = service.deleteUser(deleteObj)
            assert(deleteResponse.errors == null)
        }
    }
}