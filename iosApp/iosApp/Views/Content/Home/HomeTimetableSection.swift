import SwiftUI
import Shared

struct HomeTimetableSection: View {
    let timetable: [(Date, [Timetable])]
    let timetableDate: Date
    
    var body: some View {
        Section("Timetable for \(homeDateFormatter.string(from: self.timetableDate))") {
            ForEach(self.timetable, id: \.0) { (time, entries) in
                ForEach(entries, id: \.id) { entry in
                    HStack {
                        Text("\(entry.time)")
                            .frame(width: 50)
                        Text(entry.subject)
                            .multilineTextAlignment(.leading)
                            .lineLimit(1)
                            .frame(maxWidth: 200, alignment: .leading)
                            .fontWeight(.semibold)
                        
                        Text(entry.classroom)
                        
                        Spacer()
                        Text("\(entry.lessonNo)")
                    }
                    .foregroundStyle(entry.isCanceled ? SwiftUI.Color.red : SwiftUI.Color.primary)
                }
            }
        }
    }
}

let homeDateFormatter = {
    let f = DateFormatter()
    
    f.dateFormat = "EEEE, dd MMMM"
    return f
}()
