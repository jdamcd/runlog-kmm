import SwiftUI

struct ActivityView: View {
    @ObservedObject var viewModel = ActivityViewModel()
    var id: Int64

    var body: some View {
        Link(destination: viewModel.linkUrl(id: id)) {
            Text(Copy.strava_view)
                .foregroundColor(Color.asset(.strava))
        }.onAppear(perform: {
            self.viewModel.load(id: id)
        })
    }
}

struct ActivityView_Previews: PreviewProvider {
    static var previews: some View {
        ActivityView(id: 123)
    }
}
