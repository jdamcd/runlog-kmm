import Foundation
import RunLogShared

class UserAuth: ObservableObject {
    @Published var loggedIn: Bool

    private let userManager: UserManager

    init(userManager: UserManager = IosDI().userManager()) {
        self.userManager = userManager
        loggedIn = userManager.isLoggedIn()
    }

    func onLoginSuccess() {
        loggedIn = true
    }

    func logOut() {
        userManager.logOut()
        loggedIn = false
    }
}
