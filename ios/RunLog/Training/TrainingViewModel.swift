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
    }

    func load() {
        getProfileImage()
        if !state.isLoaded {
            state = .loading
            getActivities()
        }
    }

    func refresh() {
        getActivities()
    }

    private func getProfileImage() {
        Task {
            profileImage = try await stravaProfile.userImageUrl()
        }
    }

    private func getActivities() {
        Task {
            let result = try await stravaActivity.activities()
            if let result = result as? ResultData<NSArray> {
                let items = result.value as! [ActivityCard]
                state = .data(items)
            } else if result is ResultError {
                state = .error
            }
        }
    }

    func setDarkMode(_ isEnabled: Bool) {
        stravaActivity.requestDarkModeImages(enabled: isEnabled)
    }
}

enum TrainingState: Equatable {
    case loading
    case data([ActivityCard])
    case error

    var isLoaded: Bool {
        switch self {
        case .data: return true
        default: return false
        }
    }
}
