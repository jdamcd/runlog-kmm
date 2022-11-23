import Foundation
import RunLogShared

class ActivityViewModel: ObservableObject {
    private let strava: Strava

    init(strava: Strava = SharedModule().buildStrava(userState: UserState())) {
        self.strava = strava
    }

    func load(id: Int64) {
        strava.activityDetails(id: id) { _, _ in
        }
    }

    func linkUrl(id: Int64) -> URL {
        URL(string: strava.linkUrl(id: id))!
    }
}
