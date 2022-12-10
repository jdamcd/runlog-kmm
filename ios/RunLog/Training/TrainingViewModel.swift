import Foundation
import RunLogShared

@MainActor
class TrainingViewModel: ObservableObject {
    @Published var state: TrainingState = .loading

    private let user: UserState
    private let strava: Strava

    init(user: UserState = PersistingUserState(),
         strava: Strava = SharedModule().buildStrava(userState: PersistingUserState()))
    {
        self.user = user
        self.strava = strava
    }

    func load() {
        if !state.isLoaded {
            state = .loading
            getActivities()
        }
    }

    func refresh() {
        getActivities()
    }

    private func getActivities() {
        Task {
            let result = try await strava.activities()
            if let result = result as? ResultData<NSArray> {
                let items = result.data as! [ActivityCard]
                state = .data(items)
            } else if let error = result as? ResultError {
                state = .error
                if !error.recoverable {
                    signOut()
                }
            }
        }
    }

    func setDarkMode(_ isEnabled: Bool) {
        strava.requestDarkModeImages(enabled: isEnabled)
    }

    private func signOut() {
        user.clear()
    }
}

enum TrainingState: Equatable {
    case loading
    case data([ActivityCard])
    case error

    var isLoaded: Bool {
        switch self {
        case .data: return true
        default: return false
        }
    }
}
