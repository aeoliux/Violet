import SwiftUI
import Shared

struct HomeLuckyNumberSection: View {
    let luckyNumber: Int
    
    var body: some View {
        Section {
            HStack {
                Spacer()
                
                Text("Next lucky number")
                    .font(.title3)
                
                Spacer()
                
                Circle()
                    .fill(Color.orange)
                    .frame(width: 50, height: 50)
                    .overlay {
                        Text("\(self.luckyNumber)")
                            .foregroundStyle(.white)
                    }
                
                Spacer()
            }
        }
    }
}
