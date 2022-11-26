import SwiftUI

enum AssetColor: String {
    case primary
    case dark
    case accent
    case container
    case strava
}

extension Color {
    static func asset(_ name: AssetColor) -> Color {
        Color(name.rawValue)
    }
}
