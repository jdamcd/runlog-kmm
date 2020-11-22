import SwiftUI
import RunLogShared

@main
struct RunLogApp: App {
    
    private var userAuth = UserAuth()
    
    var body: some Scene {
        WindowGroup {
            LaunchView().environmentObject(userAuth)
        }
    }
}
