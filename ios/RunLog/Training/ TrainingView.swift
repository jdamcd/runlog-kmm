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
                        viewModel: viewModel,
                        activities: data.activities
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
    var viewModel: TrainingViewModel
    var activities: [ActivityCard]

    var body: some View {
        List(activities, id: \.id) { activity in
            Link(destination: viewModel.linkUrl(id: activity.id)) {
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
    }
}
