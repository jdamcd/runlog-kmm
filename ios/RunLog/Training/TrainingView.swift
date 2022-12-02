import NukeUI
import RunLogShared
import SwiftUI

struct TrainingView: View {
    @ObservedObject var viewModel = TrainingViewModel()
    @Environment(\.colorScheme) var colorScheme

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
                    ActivitiesList(
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
                        .foregroundColor(Color.asset(.primary))
                }
            )
            .onAppear {
                viewModel.setDarkMode(isEnabled: colorScheme == .dark)
                viewModel.load()
            }
        }.accentColor(Color.asset(.primary))
    }
}

private struct ActivitiesList: View {
    var activities: [ActivityCard]
    var refresh: () -> Void

    var body: some View {
        List(activities, id: \.id) { activity in
            ActivityItem(activity: activity)
        }
        .listStyle(PlainListStyle())
        .refreshable {
            refresh()
        }
    }
}

private struct ActivityItem: View {
    var activity: ActivityCard

    var body: some View {
        VStack {
            NavigationLink(destination: ActivityView(id: activity.id)) {
                HStack {
                    ActivitySummary(activity: activity)
                    Spacer()
                    ActivityIcons(
                        type: activity.type,
                        subtype: activity.subtype
                    )
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

private struct ActivitySummary: View {
    var activity: ActivityCard

    var body: some View {
        if activity.type == ActivityType.crossTrain {
            MiniSummary(activity: activity)
        } else {
            FullSummary(activity: activity)
        }
    }
}

private struct MiniSummary: View {
    var activity: ActivityCard

    var body: some View {
        VStack(alignment: .leading) {
            Text(activity.name)
                .font(.headline)
            Text(activity.duration)
                .font(.largeTitle)
            Text(activity.start)
                .font(.footnote)
                .fontWeight(.light)
        }
    }
}

private struct FullSummary: View {
    var activity: ActivityCard

    var body: some View {
        VStack(alignment: .leading) {
            Text(activity.name)
                .font(.headline)
            HStack(alignment: .firstTextBaseline) {
                Text(activity.distance)
                    .font(.largeTitle)
                Text("·")
                Text(activity.duration)
                Text("·")
                Text(activity.pace)
            }
            Text(activity.start)
                .font(.footnote)
                .fontWeight(.light)
        }
    }
}

struct ActivitiesView_Previews: PreviewProvider {
    static var previews: some View {
        ActivitiesList(
            activities: [
                ActivityCard(
                    id: 1,
                    name: "NYC Marathon",
                    type: ActivityType.run,
                    subtype: ActivitySubtype.workout,
                    distance: "42.2k",
                    duration: "2:59:59",
                    pace: "4:16 /km",
                    start: "SUNDAY 6 NOV @ 9:11AM",
                    mapUrl: "example.com"
                ),
                ActivityCard(
                    id: 2,
                    name: "Yoga",
                    type: ActivityType.crossTrain,
                    subtype: ActivitySubtype.default_,
                    distance: "0k",
                    duration: "30:00",
                    pace: "0:00 /km",
                    start: "SATURDAY 12 NOV @ 8:37AM",
                    mapUrl: nil
                )
            ],
            refresh: {}
        )
        TrainingView(viewModel: TrainingViewModel())
    }
}
