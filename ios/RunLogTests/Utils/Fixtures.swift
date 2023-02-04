import Foundation
import RunLogShared

extension ActivityCard {
    static func with(id: Int64 = 0) -> ActivityCard {
        ActivityCard(id: id,
                     name: "",
                     type: ActivityType.run,
                     subtype: ActivitySubtype.default_,
                     distance: "10k",
                     duration: "40:00",
                     pace: "4:00 /k",
                     start: "",
                     mapUrl: nil)
    }
}

extension ActivityDetails {
    static func with(id: Int64 = 0) -> ActivityDetails {
        ActivityDetails(id: id,
                        name: "",
                        description: nil,
                        type: ActivityType.run,
                        subtype: ActivitySubtype.race,
                        kudos: 0,
                        distance: "10k",
                        elapsedDuration: "40:00",
                        movingDuration: "40:00",
                        elevationGain: "",
                        elevationLow: nil,
                        elevationHigh: nil,
                        effort: nil,
                        calories: 500,
                        averageHeartrate: nil,
                        maxHeartrate: nil,
                        pace: "4:00 /k",
                        start: "",
                        mapUrl: nil,
                        splits: [])
    }
}

extension AthleteProfile {
    static func with(id: Int64 = 0) -> AthleteProfile {
        AthleteProfile(id: id,
                       username: "",
                       name: "",
                       imageUrl: nil,
                       recentRuns: AthleteStats(distance: "", pace: ""),
                       yearRuns: AthleteStats(distance: "", pace: ""),
                       allRuns: AthleteStats(distance: "", pace: ""))
    }
}
