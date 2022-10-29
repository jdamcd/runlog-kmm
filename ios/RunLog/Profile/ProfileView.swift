import Foundation
import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var userAuth: UserAuth

    var body: some View {
        ZStack {
            // TODO:
            ProgressView()
        }
        .navigationBarTitle(Copy.profile_title, displayMode: .inline)
        .navigationBarItems(trailing:
            Button {
                userAuth.signOut()
            } label: {
                Image(systemName: "rectangle.portrait.and.arrow.right")
                    .renderingMode(.template)
                    .foregroundColor(.black)
            }
        )
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ProfileView()
        }
    }
}
