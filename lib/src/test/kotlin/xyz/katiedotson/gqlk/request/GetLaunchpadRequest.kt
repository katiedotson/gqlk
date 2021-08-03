package xyz.katiedotson.gqlk.request

import xyz.katiedotson.gqlk.GqlK
import xyz.katiedotson.gqlk.contracts.GqlKRequestType

data class GetLaunchpadRequest(
    val id: String = "vafb_slc_3w",
    override val path: String = "launchpad",
    override val requestBody: Launchpad = Launchpad(),
    override val type: GqlKRequestType = GqlKRequestType.QUERY
) : GqlK<GetLaunchpadRequest.Launchpad>() {

    data class GetLaunchpadResponse(
        val launchpad: Launchpad = Launchpad()
    )

    data class Launchpad(val details: String = "")
}


