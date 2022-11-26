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
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                if let map = activity.mapUrl {
                    LazyImage(url: URL(string: map))
                        .aspectRatio(2.5, contentMode: ContentMode.fit)
                }
                VStack(alignment: .leading) {
                    SummaryText(
                        description: activity.description_,
                        time: activity.start
                    )
                    SummaryStats(activity: activity)
                        .padding(.horizontal)
                    if let splits = activity.splits {
                        SplitsList(splits: splits)
                    }
                    HStack {
                        Spacer()
                        Link(destination: openOnWeb()) {
                            Text(Copy.strava_view)
                                .foregroundColor(Color.asset(.strava))
                        }
                        Spacer()
                    }.padding(.top)
                }
                Spacer()
            }
        }.navigationBarTitle(activity.name, displayMode: .inline)
    }
}

private struct SummaryText: View {
    var description: String?
    var time: String

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 0) {
                if let description {
                    Text(description)
                        .padding(.bottom, 4)
                }
                Text(time)
                    .font(.footnote)
                    .fontWeight(.light)
            }
            Spacer()
        }.frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .padding(.horizontal)
            .background(Color.asset(.container))
    }
}

private struct SummaryStats: View {
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
            StatBox(
                title: Copy.activity_stat_moving_time,
                stat: activity.movingDuration
            )
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
                .frame(width: 28, alignment: .leading)

            Text(Copy.activity_split_pace)
                .font(.footnote)
                .fontWeight(.light)
                .padding(.leading, 4)
                .frame(width: 68, alignment: .leading)
            Spacer()
            Text(Copy.activity_split_elevation)
                .font(.footnote)
                .fontWeight(.light)
                .frame(width: 68, alignment: .trailing)
            if splits[0].averageHeartrate != nil {
                Text(Copy.activity_split_hr)
                    .font(.footnote)
                    .fontWeight(.light)
                    .frame(width: 48, alignment: .trailing)
            }
        }.padding(.vertical, 12)
            .padding(.horizontal)
            .background(Color.asset(.container))

        ForEach(splits, id: \.split) { split in
            SplitItem(split: split)
        }
        .padding(.horizontal)
    }
}

private struct SplitItem: View {
    var split: Split

    var body: some View {
        HStack(spacing: 0) {
            Text("\(split.split)")
                .frame(width: 28, alignment: .leading)
            Text(split.pace)
                .padding(.leading, 4)
                .frame(width: 68, alignment: .leading)
            Spacer()
            Text("\(split.elevation)")
                .frame(width: 68, alignment: .trailing)
            if let avgHr = split.averageHeartrate {
                Text("\(avgHr)")
                    .frame(width: 48, alignment: .trailing)
            }
        }.padding(.vertical, 2)
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
                    kudos: 2,
                    isRace: false,
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
                    mapUrl: "",
                    splits: [
                        Split(
                            split: 1,
                            distance: "1k",
                            elapsedDuration: "5:03",
                            movingDuration: "5:03",
                            elevation: 14,
                            averageHeartrate: 145,
                            pace: "5:03",
                            paceSeconds: 303,
                            paceZone: 2
                        ),
                        Split(
                            split: 2,
                            distance: "1k",
                            elapsedDuration: "5:04",
                            movingDuration: "5:04",
                            elevation: 12,
                            averageHeartrate: 159,
                            pace: "5:04",
                            paceSeconds: 304,
                            paceZone: 2
                        ),
                        Split(
                            split: 3,
                            distance: "1k",
                            elapsedDuration: "5:06",
                            movingDuration: "5:06",
                            elevation: -15,
                            averageHeartrate: 158,
                            pace: "5:06",
                            paceSeconds: 306,
                            paceZone: 1
                        ),
                        Split(
                            split: 4,
                            distance: "1k",
                            elapsedDuration: "5:23",
                            movingDuration: "4:35",
                            elevation: -6,
                            averageHeartrate: 150,
                            pace: "5:22",
                            paceSeconds: 322,
                            paceZone: 2
                        ),
                        Split(
                            split: 5,
                            distance: "1k",
                            elapsedDuration: "6:16",
                            movingDuration: "4:41",
                            elevation: -5,
                            averageHeartrate: 143,
                            pace: "6:16",
                            paceSeconds: 376,
                            paceZone: 2
                        ),
                        Split(
                            split: 6,
                            distance: "0k",
                            elapsedDuration: "0:13",
                            movingDuration: "0:10",
                            elevation: -1,
                            averageHeartrate: 160,
                            pace: "7:13",
                            paceSeconds: 433,
                            paceZone: 1
                        ),
                    ]
                ),
                openOnWeb: { URL(string: "example.com")! })
        }
    }
}
