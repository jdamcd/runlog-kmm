import RunLogShared
import SwiftUI

@main
struct RunLogApp: App {
    init() {
        SharedModuleKt.doInitKoin()
        userAuth = UserAuth()
    }

    private var userAuth: UserAuth

    var body: some Scene {
        WindowGroup {
            LaunchView().environmentObject(userAuth)
        }
    }
}
