import Foundation
import RunLogShared
import SwiftUI

class ProfileViewModel: ObservableObject {
    @Published var state: ProfileState = .loading

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
        strava.profile { result, _ in
            if let result = result as? ResultData<AthleteProfile> {
                let update = ProfileState.data(ProfileState.Data(profile: result.data!))
                self.updateState(to: update)
            } else if let error = result as? ResultError {
                if error.recoverable {
                    self.updateState(to: .error)
                } else {
                    self.user.clear()
                }
            }
        }
    }

    private func updateState(to: ProfileState) {
        DispatchQueue.main.async {
            self.state = to
        }
    }
}

enum ProfileState {
    struct Data {
        let profile: AthleteProfile
    }

    case loading
    case data(Data)
    case error
}
