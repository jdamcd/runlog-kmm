import NukeUI
import RunLogShared
import SwiftUI

struct ProfileView: View {
    @EnvironmentObject var userAuth: UserAuth
    @ObservedObject var viewModel = ProfileViewModel.sharedInstance

    var body: some View {
        ZStack {
            switch viewModel.state {
            case .loading:
                ProgressView()
            case .error:
                Button(action: load) {
                    Text(Copy.retry)
                }
            case let .data(profile):
                ProfileDetailsView(profile: profile)
            }
        }
        .navigationBarTitle(Copy.profile_title, displayMode: .inline)
        .navigationBarItems(trailing:
            Button {
                userAuth.logOut()
            } label: {
                Image(systemName: "rectangle.portrait.and.arrow.right")
                    .foregroundColor(Color.asset(.primary))
            }
        )
        .onAppear(perform: load)
    }

    private func load() {
        viewModel.load()
    }
}

private struct ProfileDetailsView: View {
    var profile: AthleteProfile

    var body: some View {
        VStack {
            if let imageUrl = profile.imageUrl {
                LazyImage(url: URL(string: imageUrl)) { state in
                    if let image = state.image {
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color.primary, lineWidth: 1))
                            .padding(.vertical)
                    }
                }
            }
            Text(profile.name)
                .font(.headline)
            Text(profile.username)

            GeometryReader { geometry in
                HStack {
                    Spacer()
                    ProfileStat(
                        title: Copy.profile_stat_30d,
                        primary: profile.recentRuns.distance,
                        secondary: profile.recentRuns.pace
                    )
                    .frame(width: geometry.size.width * 0.25)
                    Divider()
                        .frame(height: 90)
                    ProfileStat(
                        title: Copy.profile_stat_year_to_date,
                        primary: profile.yearRuns.distance,
                        secondary: profile.yearRuns.pace
                    )
                    .frame(width: geometry.size.width * 0.25)
                    Divider()
                        .frame(height: 90)
                    ProfileStat(
                        title: Copy.profile_stat_all_time,
                        primary: profile.allRuns.distance,
                        secondary: profile.allRuns.pace
                    )
                    .frame(width: geometry.size.width * 0.25)
                    Spacer()
                }.padding(.top)
            }
            Spacer()
        }
    }
}

private struct ProfileStat: View {
    var title: LocalizedStringKey
    var primary: String
    var secondary: String

    var body: some View {
        VStack {
            Text(title)
                .font(.headline)
                .foregroundColor(.accentColor)
                .padding(.bottom, 1)
            Text(primary)
                .font(.headline)
            Text(secondary)
                .fontWeight(.light)
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
                recentRuns: AthleteStats(
                    distance: "123.4k",
                    pace: "4:50 /km"
                ),
                yearRuns: AthleteStats(
                    distance: "1,234k",
                    pace: "5:01 /km"
                ),
                allRuns: AthleteStats(
                    distance: "12,345k",
                    pace: "5:25 /km"
                )
            ))
    }
}
