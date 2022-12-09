import Foundation
import RunLogShared
import SwiftUI

@MainActor
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
        Task {
            let result = try await strava.profile()
            if let result = result as? ResultData<AthleteProfile> {
                state = .data(ProfileState.Data(profile: result.data!))
            } else if let error = result as? ResultError {
                if error.recoverable {
                    state = .error
                } else {
                    user.clear()
                }
            }
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
