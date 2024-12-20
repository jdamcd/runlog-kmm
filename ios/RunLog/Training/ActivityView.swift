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
                Button(action: load) {
                    Text(Copy.retry)
                }
            case let .data(activity):
                ActivityDetailsView(
                    activity: activity,
                    openOnWeb: { viewModel.linkUrl(id: id) }
                )
            }
        }.onAppear(perform: load)
    }

    private func load() {
        viewModel.load(id: id)
    }
}

private struct ActivityDetailsView: View {
    @Environment(\.colorScheme) var colorScheme
    var activity: ActivityDetails
    var openOnWeb: () -> URL

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                if let map = activity.mapUrl {
                    let mapUrl = colorScheme == .dark ? map.darkMode : map.default_
                    LazyImage(url: URL(string: mapUrl)) { state in
                        if let image = state.image {
                            image
                                .resizable()
                                .aspectRatio(2.5, contentMode: .fit)
                        }
                    }
                }
                VStack(alignment: .leading) {
                    DescriptionHeader(activity: activity)
                    SummaryStats(activity: activity)
                        .padding(.horizontal)
                    if !activity.splits.isEmpty {
                        SplitsList(splits: activity.splits)
                    }
                    ActivityFooter(openOnWeb: openOnWeb)
                        .padding(.top, 28)
                }
                Spacer()
            }
        }.navigationBarTitle(activity.name, displayMode: .inline)
    }
}

private struct DescriptionHeader: View {
    var activity: ActivityDetails

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 0) {
                if let description = activity.description_ {
                    Text(description)
                        .padding(.bottom, 4)
                }
                Text(activity.start)
                    .font(.footnote)
                    .fontWeight(.light)
            }
            Spacer()
            ActivityIcons(type: activity.type, subtype: activity.subtype)
        }.frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .padding(.horizontal)
            .background(.regularMaterial)
    }
}

private struct SummaryStats: View {
    var activity: ActivityDetails

    var body: some View {
        if activity.type == ActivityType.crossTrain {
            HStack {
                StatBox(
                    title: Copy.activity_stat_elapsed_time,
                    stat: activity.elapsedDuration
                )
            }.frame(maxWidth: .infinity)
        } else {
            RunRideSummaryStats(activity: activity)
        }
    }
}

private struct RunRideSummaryStats: View {
    var activity: ActivityDetails

    var body: some View {
        HStack {
            StatBox(
                title: Copy.activity_stat_distance,
                stat: activity.distance
            )
            StatBox(
                title: Copy.activity_stat_pace,
                stat: activity.pace
            )
            if activity.subtype.isRace() {
                StatBox(
                    title: Copy.activity_stat_elapsed_time,
                    stat: activity.elapsedDuration
                )
            } else {
                StatBox(
                    title: Copy.activity_stat_moving_time,
                    stat: activity.movingDuration
                )
            }
        }.frame(maxWidth: .infinity)
        if let effort = activity.effort,
           let avgHr = activity.averageHeartrate,
           let maxHr = activity.maxHeartrate
        {
            Divider()
            HStack {
                StatBox(
                    title: Copy.activity_stat_effort,
                    stat: "\(effort)"
                )
                StatBox(
                    title: Copy.activity_stat_hr_avg,
                    stat: "\(avgHr)"
                )
                StatBox(
                    title: Copy.activity_stat_hr_max,
                    stat: "\(maxHr)"
                )
            }.frame(maxWidth: .infinity)
        }
        Divider()
        HStack {
            StatBox(
                title: Copy.activity_stat_elevation,
                stat: activity.elevationGain
            )
            StatBox(
                title: Copy.activity_stat_calories,
                stat: "\(activity.calories)"
            )
        }.frame(maxWidth: .infinity)
    }
}

private struct StatBox: View {
    var title: LocalizedStringKey
    var stat: String

    var body: some View {
        VStack(alignment: .center, spacing: 0) {
            Text(title)
                .font(.footnote)
                .fontWeight(.light)
                .foregroundColor(Color.gray)
                .padding(.bottom, 2)
            Text(stat)
        }.frame(maxWidth: .infinity)
            .padding(4)
    }
}

private struct SplitsList: View {
    var splits: [Split]

    var body: some View {
        HStack(spacing: 0) {
            Text(Copy.activity_split_k)
                .font(.footnote)
                .fontWeight(.light)
                .frame(width: 40, alignment: .leading)

            Text(Copy.activity_split_pace)
                .font(.footnote)
                .fontWeight(.light)
                .padding(.leading, 4)
                .frame(width: 76, alignment: .leading)
            Spacer()
            Text(Copy.activity_split_elevation)
                .font(.footnote)
                .fontWeight(.light)
                .frame(width: 68, alignment: .trailing)
            Text(Copy.activity_split_hr)
                .font(.footnote)
                .fontWeight(.light)
                .frame(width: 48, alignment: .trailing)
        }.padding(.vertical, 12)
            .padding(.horizontal)
            .background(.regularMaterial)
            .padding(.bottom, 2)

        ForEach(splits, id: \.number) { split in
            SplitItem(split: split)
        }
        .padding(.horizontal)
    }
}

private struct SplitItem: View {
    var split: Split

    var body: some View {
        HStack(spacing: 0) {
            if split.isPartial {
                Text(split.distance)
                    .italic()
                    .frame(width: 40, alignment: .leading)
            } else {
                Text("\(split.number)")
                    .frame(width: 40, alignment: .leading)
            }
            Text(split.pace)
                .padding(.leading, 4)
                .frame(width: 72, alignment: .leading)
            SplitBar(value: split.visualisation)
            Text(split.elevation)
                .frame(width: 48, alignment: .trailing)
            Text(split.averageHeartrate)
                .frame(width: 48, alignment: .trailing)
        }.padding(.vertical, 2)
    }
}

private struct ActivityFooter: View {
    var openOnWeb: () -> URL

    var body: some View {
        HStack {
            Spacer()
            Link(destination: openOnWeb()) {
                Text(Copy.strava_view)
                    .padding(.horizontal, 12)
                    .padding(.vertical, 8)
                    .background(.regularMaterial)
                    .foregroundColor(Color.asset(.strava))
                    .cornerRadius(6)
            }
            Spacer()
        }
    }
}

struct ActivityView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ActivityDetailsView(activity:
                ActivityDetails(
                    id: 123,
                    name: "Afternoon Run",
                    description: "Easy Peckham Rye loop",
                    type: ActivityType.run,
                    subtype: ActivitySubtype.default_,
                    kudos: 2,
                    distance: "5k",
                    elapsedDuration: "27:05",
                    movingDuration: "24:39",
                    elevationGain: "41m",
                    elevationLow: "19m",
                    elevationHigh: "47.8m",
                    effort: 34,
                    calories: 371,
                    averageHeartrate: 151,
                    maxHeartrate: 167,
                    pace: "4:54 /km",
                    start: "FRIDAY 25 NOV @ 2:23PM",
                    mapUrl: ImageUrl(default: "", darkMode: ""),
                    splits: [Split(
                        number: 1,
                        distance: "1k",
                        isPartial: false,
                        elapsedDuration: "5:03",
                        movingDuration: "5:03",
                        elevation: "14",
                        averageHeartrate: "145",
                        pace: "5:03",
                        paceSeconds: 303,
                        paceZone: 2,
                        visualisation: 1.0
                    ),
                    Split(
                        number: 2,
                        distance: "1k",
                        isPartial: false,
                        elapsedDuration: "5:04",
                        movingDuration: "5:04",
                        elevation: "12",
                        averageHeartrate: "159",
                        pace: "5:04",
                        paceSeconds: 304,
                        paceZone: 2,
                        visualisation: 1.0
                    ),
                    Split(
                        number: 3,
                        distance: "1k",
                        isPartial: false,
                        elapsedDuration: "5:06",
                        movingDuration: "5:06",
                        elevation: "-15",
                        averageHeartrate: "158",
                        pace: "5:06",
                        paceSeconds: 306,
                        paceZone: 1,
                        visualisation: 0.9
                    ),
                    Split(
                        number: 4,
                        distance: "1k",
                        isPartial: false,
                        elapsedDuration: "5:23",
                        movingDuration: "4:35",
                        elevation: "-6",
                        averageHeartrate: "150",
                        pace: "5:22",
                        paceSeconds: 322,
                        paceZone: 2,
                        visualisation: 0.8
                    ),
                    Split(
                        number: 5,
                        distance: "0.5",
                        isPartial: true,
                        elapsedDuration: "6:16",
                        movingDuration: "4:41",
                        elevation: "-5",
                        averageHeartrate: "143",
                        pace: "6:16",
                        paceSeconds: 376,
                        paceZone: 2,
                        visualisation: 0.7
                    )]
                ),
                openOnWeb: { URL(string: "example.com")! })
        }
    }
}
