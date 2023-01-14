import Foundation
import RunLogShared

class ActivityMock: StravaActivity {
    var activities: Result<NSArray> = ResultError(error: KotlinThrowable())
    var activityDetails: Result<ActivityDetails> = ResultError(error: KotlinThrowable())
    var loginResult: LoginResult = .Success()

    var darkModeImages = false

    func activities() async throws -> Result<NSArray> {
        activities
    }

    func activityDetails(id _: Int64) async throws -> Result<ActivityDetails> {
        activityDetails
    }

    func linkUrl(id: Int64) -> String {
        "testLinkUrl:\(id)"
    }

    func requestDarkModeImages(enabled: Bool) {
        darkModeImages = enabled
    }
}

class ProfileMock: StravaProfile {
    var profile: Result<AthleteProfile> = ResultError(error: KotlinThrowable())

    func refresh() async throws -> RefreshState {
        RefreshState.loading
    }

    func profile() async throws -> Result<AthleteProfile> {
        profile
    }

    func profileFlow() -> Kotlinx_coroutines_coreFlow {
        NSObject() as! Kotlinx_coroutines_coreFlow
    }
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
