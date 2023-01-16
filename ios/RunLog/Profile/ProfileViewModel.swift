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
            await loadData()
            if await refresh() == RefreshState.success {
                await loadData()
            } else if state == .loading {
                state = .error
            }
        }
    }

    private func loadData() async {
        do {
            let result = try await stravaProfile.profile()
            if let result = result as? ResultData<AthleteProfile> {
                state = .data(result.value!)
            } else if result is ResultError {
                state = .error
            }
        } catch {
            state = .error
        }
    }

    private func refresh() async -> RefreshState {
        do {
            return try await stravaProfile.refresh()
        } catch {
            return RefreshState.error
        }
    }
}

enum ProfileState: Equatable {
    case loading
    case data(AthleteProfile)
    case error
}
