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

struct Components_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            SplitBar(value: 0.25)
            SplitBar(value: 0.5)
            SplitBar(value: 0.75)
            SplitBar(value: 1.0)
        }
        .frame(height: 200)
        .padding()
    }
}
