import NukeUI
import RunLogShared
import SwiftUI

struct TrainingView: View {
    @ObservedObject var viewModel = TrainingViewModel()

    var body: some View {
        NavigationView {
            ZStack {
                switch viewModel.state {
                case .loading:
                    ProgressView()
                case .error:
                    Button(action: viewModel.load) {
                        Text(Copy.retry)
                    }
                case let .data(data):
                    ActivitiesListView(
                        activities: data.activities,
                        refresh: {
                            viewModel.refresh()
                        }
                    )
                }
            }
            .navigationBarTitle(Copy.activities_title)
            .navigationBarItems(trailing:
                NavigationLink(destination: ProfileView()) {
                    Image(systemName: "person.circle.fill")
                        .renderingMode(.template)
                        .foregroundColor(.black)
                }
            )
            .onAppear(perform: {
                viewModel.load()
            })
        }.accentColor(Color.asset(.strava))
    }
}

private struct ActivitiesListView: View {
    var activities: [ActivityCard]
    var refresh: () -> Void

    var body: some View {
        List(activities, id: \.id) { activity in
            ActivityItemView(activity: activity)
        }
        .listStyle(PlainListStyle())
        .refreshable {
            refresh()
        }
    }
}

private struct ActivityItemView: View {
    var activity: ActivityCard

    var body: some View {
        VStack {
            NavigationLink(destination: ActivityView(id: activity.id)) {
                HStack {
                    VStack(alignment: .leading) {
                        Text(activity.name)
                            .font(.headline)
                        HStack(alignment: .firstTextBaseline) {
                            Text(activity.distance)
                                .font(.largeTitle)
                            Text("·  \(activity.duration)")
                            Text("·  \(activity.pace)")
                        }
                        Text(activity.start)
                            .font(.footnote)
                            .fontWeight(.light)
                    }
                    Spacer()
                    ActivityIconsView(activity: activity)
                }
            }
            if let map = activity.mapUrl {
                LazyImage(url: URL(string: map))
                    .aspectRatio(2.5, contentMode: ContentMode.fit)
                    .cornerRadius(6)
            }
        }
    }
}

private struct ActivityIconsView: View {
    var activity: ActivityCard

    var body: some View {
        HStack {
            switch activity.type {
            case .run:
                Image(systemName: "figure.run")
            case .cycle:
                Image(systemName: "figure.outdoor.cycle")
            default:
                Image(systemName: "dumbbell")
            }
            switch activity.subtype {
            case .race:
                Image(systemName: "medal.fill")
                    .foregroundColor(Color.asset(.strava))
            case .workout:
                Image(systemName: "1.lane")
                    .foregroundColor(Color.asset(.strava))
            case .long_:
                Image(systemName: "signpost.right")
                    .foregroundColor(Color.asset(.strava))
            default: EmptyView()
            }
        }
    }
}

struct ActivitiesView_Previews: PreviewProvider {
    static var previews: some View {
        TrainingView(viewModel: TrainingViewModel())

        ActivitiesListView(
            activities: [
                ActivityCard(
                    id: 1,
                    name: "NYC Marathon",
                    type: ActivityType.run,
                    subtype: ActivitySubtype.race,
                    distance: "42.2k",
                    duration: "2:59:59",
                    pace: "4:16 /km",
                    start: "SUNDAY 6 NOV @ 9:11AM",
                    mapUrl: "example.com"
                ),
                ActivityCard(
                    id: 2,
                    name: "Morning Run",
                    type: ActivityType.run,
                    subtype: ActivitySubtype.default_,
                    distance: "12.3k",
                    duration: "1:02:17",
                    pace: "5:04 /km",
                    start: "SATURDAY 12 NOV @ 8:37AM",
                    mapUrl: nil
                ),
            ],
            refresh: {}
        )
    }
}
