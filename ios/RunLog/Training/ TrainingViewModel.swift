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
        state = .loading
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

    func linkUrl(id: Int64) -> URL {
        URL(string: strava.linkUrl(id: id))!
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
}
