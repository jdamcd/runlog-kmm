import SwiftUI

struct LaunchView: View {
    @EnvironmentObject var userAuth: UserAuth

    var body: some View {
        if userAuth.loggedIn {
            ActivitiesView()
        } else {
            LoginView()
        }
    }
}
