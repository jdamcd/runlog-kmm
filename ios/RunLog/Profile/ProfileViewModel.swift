import Foundation
import RunLogShared
import SwiftUI

@MainActor
class ProfileViewModel: ObservableObject {
    @Published var state: ProfileState = .loading

    private let user: UserState
    private let stravaProfile: StravaProfile

    init(user: UserState = PersistingUserState(),
         stravaProfile: StravaProfile = SharedModule().stravaProfile(user: PersistingUserState()))
    {
        self.user = user
        self.stravaProfile = stravaProfile
    }

    func load() {
        state = .loading
        Task {
            let result = try await stravaProfile.profile()
            if let result = result as? ResultData<AthleteProfile> {
                state = .data(result.data!)
            } else if let error = result as? ResultError {
                state = .error
                if !error.recoverable {
                    user.clear()
                }
            }
        }
    }
}

enum ProfileState: Equatable {
    case loading
    case data(AthleteProfile)
    case error
}
