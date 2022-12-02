import RunLogShared
import SwiftUI

struct SplitBar: View {
    var value: Float

    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                Rectangle().frame(
                    width: CGFloat(self.value) * geometry.size.width,
                    height: geometry.size.height
                )
                .foregroundColor(Color.asset(.accent))
                .opacity(Double(value))
            }.cornerRadius(4)
        }
    }
}

struct ActivityIcons: View {
    var type: ActivityType
    var subtype: ActivitySubtype

    var body: some View {
        HStack {
            switch type {
            case .run:
                Image(systemName: "figure.run")
            case .cycle:
                Image(systemName: "figure.outdoor.cycle")
            default:
                Image(systemName: "dumbbell")
            }
            switch subtype {
            case .race:
                Image(systemName: "medal.fill")
                    .foregroundColor(Color.asset(.accent))
            case .workout:
                Image(systemName: "chart.bar.xaxis")
                    .foregroundColor(Color.asset(.accent))
            case .long_:
                Image(systemName: "signpost.right")
                    .foregroundColor(Color.asset(.accent))
            default: EmptyView()
            }
        }
    }
}

struct Components_Previews: PreviewProvider {
    static var previews: some View {
        VStack(alignment: .leading) {
            SplitBar(value: 0.25)
            SplitBar(value: 0.5)
            SplitBar(value: 0.75)
            SplitBar(value: 1.0)
        }
        .frame(height: 200)
        .padding()

        VStack(alignment: .leading, spacing: 20) {
            ActivityIcons(
                type: ActivityType.run,
                subtype: ActivitySubtype.default_
            )
            ActivityIcons(
                type: ActivityType.run,
                subtype: ActivitySubtype.race
            )
            ActivityIcons(
                type: ActivityType.run,
                subtype: ActivitySubtype.long_
            )
            ActivityIcons(
                type: ActivityType.run,
                subtype: ActivitySubtype.workout
            )

            ActivityIcons(
                type: ActivityType.cycle,
                subtype: ActivitySubtype.default_
            )

            ActivityIcons(
                type: ActivityType.crossTrain,
                subtype: ActivitySubtype.default_
            )
        }
        .previewDisplayName("ff")
    }
}
