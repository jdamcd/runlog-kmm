import Foundation
import RunLogShared

class ActivitiesViewModel: ObservableObject {
    @Published var state: ActivitiesState = .loading

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
                let update = ActivitiesState.data(
                    ActivitiesState.Data(activities: itemsArray))
                self.state = update
            } else if let error = result as? ResultError {
                if error.recoverable {
                    self.state = .error
                } else {
                    self.signOut()
                }
            }
        }
    }

    func linkUrl(id: Int64) -> URL {
        URL(string: strava.linkUrl(id: id))!
    }

    private func signOut() {
        user.clear()
    }
}

enum ActivitiesState {
    struct Data {
        let activities: [ActivityCard]
    }

    case loading
    case data(Data)
    case error
}
