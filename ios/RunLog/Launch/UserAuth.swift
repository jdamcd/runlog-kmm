import Foundation
import RunLogShared

class UserAuth: ObservableObject {
    @Published var loggedIn: Bool

    private let userState: UserState

    init(userState: UserState = PersistingUserState()) {
        self.userState = userState
        loggedIn = userState.isLoggedIn()
    }

    func onLoginSuccess() {
        loggedIn = true
    }

    func signOut() {
        userState.clear()
        loggedIn = false
    }
}
