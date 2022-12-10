import Foundation
import RunLogShared

@MainActor
class ActivityViewModel: ObservableObject {
    @Published var state: ActivityState = .loading

    private let strava: Strava

    init(strava: Strava = SharedModule().buildStrava(userState: PersistingUserState())) {
        self.strava = strava
    }

    func load(id: Int64) {
        if !state.isLoaded {
            state = .loading
            getActivityDetails(id: id)
        }
    }

    private func getActivityDetails(id: Int64) {
        Task {
            let result = try await strava.activityDetails(id: id)
            if let data = result as? ResultData<ActivityDetails> {
                state = .data(ActivityState.Data(activity: data.data!))
            } else if result is ResultError {
                state = .error
            }
        }
    }

    func linkUrl(id: Int64) -> URL {
        URL(string: strava.linkUrl(id: id))!
    }

    func setDarkMode(_ isEnabled: Bool) {
        strava.requestDarkModeImages(enabled: isEnabled)
    }
}

enum ActivityState: Equatable {
    struct Data: Equatable {
        let activity: ActivityDetails
    }

    case loading
    case data(Data)
    case error

    var isLoaded: Bool {
        switch self {
        case .data: return true
        default: return false
        }
    }
}
