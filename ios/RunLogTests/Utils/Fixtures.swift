import Foundation
import RunLogShared

extension ActivityCard {
    static func with(id: Int64 = 0) -> ActivityCard {
        ActivityCard(id: id,
                     name: "Name",
                     type: ActivityType.run,
                     subtype: ActivitySubtype.default_,
                     distance: "10k",
                     duration: "40:00",
                     pace: "4:00 /k",
                     start: "Time",
                     mapUrl: nil)
    }
}
