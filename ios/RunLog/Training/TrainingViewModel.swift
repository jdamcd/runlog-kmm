import Foundation
import RunLogShared

@MainActor
class TrainingViewModel: ObservableObject {
    @Published var profileImage: String? = nil
    @Published var state: TrainingState = .loading

    private let stravaActivity: StravaActivity
    private let stravaProfile: StravaProfile

    init(stravaActivity: StravaActivity = IosDI().stravaActivity(),
         stravaProfile: StravaProfile = IosDI().stravaProfile())
    {
        self.stravaActivity = stravaActivity
        self.stravaProfile = stravaProfile
        getProfileImage()
    }

    func refresh() {
        if state != .loading {
            Task {
                if await refresh() == RefreshState.success {
                    await loadData()
                }
            }
        }
    }

    func load() {
        if !state.isLoaded {
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
    }

    private func loadData() async {
        do {
            let result = try await stravaActivity.activities()
            if let result = result as? ResultData<NSArray> {
                state = .data(result.value as! [ActivityCard])
            } else if result is ResultError {
                state = .error
            }
        } catch {
            state = .error
        }
    }

    private func refresh() async -> RefreshState {
        do {
            return try await stravaActivity.refresh()
        } catch {
            return RefreshState.error
        }
    }

    private func getProfileImage() {
        Task {
            profileImage = try await stravaProfile.userImageUrl()
        }
    }
}

enum TrainingState: Equatable {
    case loading
    case data([ActivityCard])
    case error

    var isLoaded: Bool {
        switch self {
        case .data: true
        default: false
        }
    }
}
