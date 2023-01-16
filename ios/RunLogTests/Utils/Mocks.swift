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
    var refreshState = RefreshState.loading

    private var callCount = 0
    var profile: [Result<AthleteProfile>] = []

    func refresh() async throws -> RefreshState {
        refreshState
    }

    func profile() async throws -> Result<AthleteProfile> {
        callCount += 1
        return profile[callCount - 1]
    }

    func profileFlow() -> Kotlinx_coroutines_coreFlow {
        NSObject() as! Kotlinx_coroutines_coreFlow
    }
}

class UserManagerMock: UserManager {
    var loggedIn = true

    func isLoggedIn() -> Bool {
        loggedIn
    }

    func logOut() {
        loggedIn = false
    }
}
