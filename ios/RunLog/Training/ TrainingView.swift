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
                    Button(action: viewModel.load, label: {
                        Text(Copy.retry)
                    })
                case let .data(data):
                    ActivitiesListView(
                        activities: data.activities,
                        generateUrl: { id in
                            viewModel.linkUrl(id: id)
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
                self.viewModel.load()
            })
        }
    }
}

private struct ActivitiesListView: View {
    var activities: [ActivityCard]
    var generateUrl: (Int64) -> URL

    var body: some View {
        List(activities, id: \.id) { activity in
            Link(destination: generateUrl(activity.id)) {
                VStack {
                    HStack {
                        VStack(alignment: .leading) {
                            Text("\(activity.type): \(activity.name)")
                                .font(.headline)
                            if let label = activity.label {
                                Text(label)
                                    .foregroundColor(Color.asset(.strava))
                            }
                            Text(activity.duration)
                            Text(activity.distance)
                            Text(activity.start)
                        }
                        Spacer()
                        Text(Copy.strava_view)
                            .font(.footnote)
                            .foregroundColor(Color.asset(.strava))
                    }
                    if let map = activity.mapUrl {
                        LazyImage(url: URL(string: map))
                            .aspectRatio(2.5, contentMode: ContentMode.fit)
                    }
                }
            }
        }
        .listStyle(PlainListStyle())
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
                    type: "Run",
                    label: "Race",
                    distance: "42.2km",
                    duration: "2:59:59",
                    start: "Sunday 6 Nov @ 9:11am",
                    mapUrl: "example.com"
                ),
                ActivityCard(
                    id: 2,
                    name: "Morning Run",
                    type: "Run",
                    label: nil,
                    distance: "12.3km",
                    duration: "1:02:17",
                    start: "Saturday 12 nov @ 8:37am",
                    mapUrl: nil
                ),
            ],
            generateUrl: { _ in URL(string: "example.com")! }
        )
    }
}
