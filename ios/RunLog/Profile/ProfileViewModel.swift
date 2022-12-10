import Foundation
import RunLogShared
import SwiftUI

@MainActor
class ProfileViewModel: ObservableObject {
    @Published var state: ProfileState = .loading

    private let user: UserState
    private let strava: Strava

    init(user: UserState = PersistingUserState(),
         strava: Strava = SharedModule().buildStrava(userState: PersistingUserState()))
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
                state = .error
                if !error.recoverable {
                    user.clear()
                }
            }
        }
    }
}

enum ProfileState : Equatable {
    struct Data : Equatable {
        let profile: AthleteProfile
    }

    case loading
    case data(Data)
    case error
}
