package com.jdamcd.runlog.shared.api

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class StravaApiTest {

    @Test
    fun createsLoginUrlWithParameters() {
        StravaApi.loginUrl(clientId = "123") shouldBe "https://strava.com/oauth/mobile/authorize?client_id=123&redirect_uri=runlog-auth%3A%2F%2Fjdamcd.com&response_type=code&approval_prompt=auto&scope=activity%3Aread_all"
    }

    @Test
    fun createsActivityLinkUrl() {
        StravaApi.linkUrl(id = 123L) shouldBe "https://strava.com/activities/123"
    }
}
