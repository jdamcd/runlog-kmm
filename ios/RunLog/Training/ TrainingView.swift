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
                            Text(activity.name)
                                .font(.headline)
                            HStack(alignment: .firstTextBaseline) {
                                Text(activity.distance)
                                    .font(.largeTitle)
                                Text("·")
                                Text(activity.duration)
                            }
                            Text(activity.start)
                                .font(.footnote)
                                .fontWeight(.light)
                        }
                        Spacer()
                        VStack(alignment: .trailing) {
                            HStack {
                                switch activity.type {
                                case .run:
                                    Image(systemName: "figure.run")
                                case .cycle:
                                    Image(systemName: "figure.outdoor.cycle")
                                default:
                                    Image(systemName: "dumbbell")
                                }
                                if activity.isRace {
                                    Image(systemName: "medal.fill")
                                        .foregroundColor(Color.asset(.strava))
                                }
                            }.padding(.bottom)

                            Text(Copy.strava_view)
                                .font(.footnote)
                                .foregroundColor(Color.asset(.strava))
                                .padding(.top)
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
                    type: ActivityType.run,
                    isRace: true,
                    distance: "42.2km",
                    duration: "2:59:59",
                    start: "SUNDAY 6 NOV @ 9:11AM",
                    mapUrl: "example.com"
                ),
                ActivityCard(
                    id: 2,
                    name: "Morning Run",
                    type: ActivityType.run,
                    isRace: false,
                    distance: "12.3km",
                    duration: "1:02:17",
                    start: "SATURDAY 12 NOV @ 8:37AM",
                    mapUrl: nil
                ),
            ],
            generateUrl: { _ in URL(string: "example.com")! }
        )
    }
}
