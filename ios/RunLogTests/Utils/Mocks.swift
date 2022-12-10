import Foundation
import RunLogShared

class StravaMock: Strava {
    var activities: Result<NSArray> = ResultError(error: KotlinThrowable(), recoverable: false)
    var activityDetails: Result<ActivityDetails> = ResultError(error: KotlinThrowable(), recoverable: false)
    var loginResult: LoginResult = .Success()
    var profile: Result<AthleteProfile> = ResultError(error: KotlinThrowable(), recoverable: false)

    var darkModeImages = false

    func linkUrl(id: Int64) -> String {
        "testLinkUrl:\(id)"
    }

    func activities() async throws -> Result<NSArray> {
        activities
    }

    func activityDetails(id _: Int64) async throws -> Result<ActivityDetails> {
        activityDetails
    }

    func authenticate(code _: String) async throws -> LoginResult {
        loginResult
    }

    func profile() async throws -> Result<AthleteProfile> {
        profile
    }

    func requestDarkModeImages(enabled: Bool) {
        darkModeImages = enabled
    }

    var authScheme = "testAuthScheme"

    var loginUrl = "testLoginUrl"
}

class UserStateMock: UserState {
    var loggedIn = true

    func isLoggedIn() -> Bool {
        loggedIn
    }

    func clear() {
        loggedIn = false
    }

    var accessToken = "testAccessToken"

    var refreshToken = "testRefreshToken"
}
