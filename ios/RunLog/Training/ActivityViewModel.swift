import Foundation
import RunLogShared

class ActivityViewModel: ObservableObject {
    @Published var state: ActivityState = .loading

    private let strava: Strava

    init(strava: Strava = SharedModule().buildStrava(userState: UserState())) {
        self.strava = strava
    }

    func load(id: Int64) {
        if !state.isLoaded {
            updateState(to: .loading)
            getActivityDetails(id: id)
        }
    }

    private func getActivityDetails(id: Int64) {
        strava.activityDetails(id: id) { result, _ in
            if let data = result as? ResultData<ActivityDetails> {
                let update = ActivityState.data(
                    ActivityState.Data(activity: data.data!))
                self.updateState(to: update)
            } else if result is ResultError {
                self.updateState(to: .error)
            }
        }
    }

    func linkUrl(id: Int64) -> URL {
        URL(string: strava.linkUrl(id: id))!
    }

    private func updateState(to: ActivityState) {
        DispatchQueue.main.async {
            self.state = to
        }
    }
}

enum ActivityState {
    struct Data {
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
