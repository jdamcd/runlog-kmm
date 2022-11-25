import NukeUI
import RunLogShared
import SwiftUI

struct ActivityView: View {
    @ObservedObject var viewModel = ActivityViewModel()
    var id: Int64

    var body: some View {
        ZStack {
            switch viewModel.state {
            case .loading:
                ProgressView()
            case .error:
                Button(action: { viewModel.load(id: id) }) {
                    Text(Copy.retry)
                }
            case let .data(data):
                ActivityDetailsView(
                    activity: data.activity,
                    openOnWeb: { viewModel.linkUrl(id: id) }
                )
            }
        }.onAppear(perform: { viewModel.load(id: id) })
    }
}

private struct ActivityDetailsView: View {
    var activity: ActivityDetails
    var openOnWeb: () -> URL

    var body: some View {
        VStack(alignment: .leading) {
            if let map = activity.mapUrl {
                LazyImage(url: URL(string: map))
                    .aspectRatio(2.5, contentMode: ContentMode.fit)
            }
            VStack(alignment: .leading) {
                if let description = activity.description_ {
                    Text(description)
                }
                HStack {
                    Text(activity.distance)
                    Text("·  \(activity.movingDuration)")
                    Text("·  \(activity.pace)")
                }
                Text(activity.start)
                    .font(.footnote)
                    .fontWeight(.light)

                Spacer()
                HStack {
                    Spacer()
                    Link(destination: openOnWeb()) {
                        Text(Copy.strava_view)
                            .foregroundColor(Color.asset(.strava))
                    }
                    Spacer()
                }
            }.padding()
            Spacer()
        }.navigationBarTitle(activity.name, displayMode: .inline)
    }
}

struct ActivityView_Previews: PreviewProvider {
    static var previews: some View {
        ActivityView(id: 123)
    }
}
