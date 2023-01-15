import Foundation
import RunLogShared
import SwiftUI

@MainActor
class ProfileViewModel: ObservableObject {
    @Published var state: ProfileState = .loading

    private let stravaProfile: StravaProfile

    init(stravaProfile: StravaProfile = IosDI().stravaProfile()) {
        self.stravaProfile = stravaProfile
    }

    func load() {
        state = .loading
        Task {
            let result = try await stravaProfile.profile()
            if let result = result as? ResultData<AthleteProfile> {
                state = .data(result.value!)
            } else if result is ResultError {
                state = .error
            }
        }
    }
}

enum ProfileState: Equatable {
    case loading
    case data(AthleteProfile)
    case error
}
