import Foundation
import RunLogShared

class TrainingViewModel: ObservableObject {
    @Published var state: TrainingState = .loading

    private let user: UserState
    private let strava: Strava

    init(user: UserState = UserState(),
         strava: Strava = SharedModule().buildStrava(userState: UserState()))
    {
        self.user = user
        self.strava = strava
    }

    func load() {
        if !state.isLoaded {
            updateState(to: .loading)
            getActivities()
        }
    }

    func refresh() {
        getActivities()
    }

    private func getActivities() {
        strava.activities { result, _ in
            if let items = result as? ResultData<NSArray> {
                let itemsArray = items.data as! [ActivityCard]
                let update = TrainingState.data(
                    TrainingState.Data(activities: itemsArray))
                self.updateState(to: update)
            } else if let error = result as? ResultError {
                if error.recoverable {
                    self.updateState(to: .error)
                } else {
                    self.signOut()
                }
            }
        }
    }

    private func updateState(to: TrainingState) {
        DispatchQueue.main.async {
            self.state = to
        }
    }

    private func signOut() {
        user.clear()
    }
}

enum TrainingState {
    struct Data {
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
