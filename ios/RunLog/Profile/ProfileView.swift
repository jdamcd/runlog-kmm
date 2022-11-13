import NukeUI
import RunLogShared
import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var userAuth: UserAuth
    @ObservedObject var viewModel = ProfileViewModel()

    var body: some View {
        ZStack {
            switch viewModel.state {
            case .loading:
                ProgressView()
            case .error:
                Button(action: viewModel.load, label: {
                    Text(Copy.retry)
                })
            case let .data(data):
                ProfileDetailsView(profile: data.profile)
            }
        }
        .navigationBarTitle(Copy.profile_title, displayMode: .inline)
        .navigationBarItems(trailing:
            Button {
                self.userAuth.signOut()
            } label: {
                Image(systemName: "rectangle.portrait.and.arrow.right")
                    .renderingMode(.template)
                    .foregroundColor(.black)
            }
        )
        .onAppear(perform: {
            self.viewModel.load()
        })
    }
}

private struct ProfileDetailsView: View {
    var profile: AthleteProfile

    var body: some View {
        VStack {
            LazyImage(url: URL(string: profile.imageUrl))
                .frame(width: 100, height: 100)
                .clipShape(Circle())
            Text(profile.name)
                .font(.headline)
            Text(profile.yearRunDistance)
        }
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ProfileView()
        }

        ProfileDetailsView(profile:
            AthleteProfile(
                id: 1,
                username: "jdamcd",
                name: "Jamie McDonald",
                imageUrl: "example.com",
                yearRunDistance: "1,234km",
                allTimeRunDistance: "12,345km"
            ))
    }
}
