import SwiftUI
import Shared

struct HomeTimetableSection: View {
    let timetable: [Timetable]
    let timetableDate: Date
    
    var body: some View {
        Section("Timetable for \(homeDateFormatter.string(from: self.timetableDate))") {
            ForEach(self.timetable, id: \.id) { entry in
                HStack {
                    Text("\(entry.time)")
                        .frame(width: 50)
                    Text(entry.subject)
                        .multilineTextAlignment(.leading)
                        .lineLimit(1)
                        .fontWeight(.semibold)
                    
                    Text(entry.classroom)
                        .lineLimit(1)
                    
                    Spacer()
                    Text("\(entry.lessonNo)")
                }
                .foregroundStyle(entry.isCanceled ? SwiftUI.Color.red : SwiftUI.Color.primary)
            }
        }
    }
}

let homeDateFormatter = {
    let f = DateFormatter()
    
    f.dateFormat = "EEEE, dd MMMM"
    return f
}()
