import SwiftUI
import RunLogShared

struct ActivitiesView: View {
    
    @EnvironmentObject var userAuth: UserAuth
    @ObservedObject var viewModel = ActivitiesViewModel()
    
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
                    case .data(let data):
                        ActivitiesListView(
                            viewModel: viewModel,
                            activities: data.activities)
                }
            }
            .navigationBarTitle(Copy.activities_title)
            .navigationBarItems(trailing:
                Button(Copy.sign_out) {
                    userAuth.signOut()
                }
            )
            .onAppear(perform: {
                self.viewModel.load()
            })
        }
    }
}

private struct ActivitiesListView : View {
    var viewModel: ActivitiesViewModel
    var activities: Array<ActivityCard>
    
    var body: some View {
        List(activities, id: \.id) { activity in
            Link(destination: viewModel.linkUrl(id: activity.id)) {
                HStack {
                    VStack(alignment: .leading) {
                        Text("\(activity.type): \(activity.name)")
                            .font(.headline)
                        if let label = activity.label {
                            Text(label)
                                .foregroundColor(Color.asset(.strava))
                        }
                        Text(activity.time)
                        Text(activity.distance)
                    }
                    Spacer()
                    Text(Copy.strava_view)
                        .font(.footnote)
                        .foregroundColor(Color.asset(.strava))
                }
            }
        }
        .listStyle(PlainListStyle())
    }
}

struct ActivitiesView_Previews: PreviewProvider {
    static var previews: some View {
        ActivitiesView(viewModel: ActivitiesViewModel())
    }
}
