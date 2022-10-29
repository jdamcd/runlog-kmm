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
                ProfileDetailsView(
                    viewModel: viewModel,
                    profile: data.profile
                )
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
    var viewModel: ProfileViewModel
    var profile: AthleteProfile

    var body: some View {
        VStack {
            AsyncImage(url: URL(string: profile.imageUrl)) { image in
                image
                    .resizable()
                    .transition(.opacity)
            } placeholder: {
                Color.white
            }
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
    }
}
