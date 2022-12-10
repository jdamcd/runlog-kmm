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
            if let items = result as? ResultData<NSArray> {
                let itemsArray = items.data as! [ActivityCard]
                state = .data(TrainingState.Data(activities: itemsArray))
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
    struct Data: Equatable {
        let activities: [ActivityCard]
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
