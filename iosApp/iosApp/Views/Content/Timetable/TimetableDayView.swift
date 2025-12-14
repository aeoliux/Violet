import Shared
import SwiftUI

struct TimetableDayView: View {
    let timetable: TimetableView.ViewModel.NavKey
    let refresh: () async -> Void
    
    var body: some View {
        List {
            ForEach(self.timetable.timetable, id: \.time) { timetable in
                ForEach(timetable.entries, id: \.id) { entry in
                    HStack {
                        VStack(alignment: .leading, spacing: 10) {
                            if entry.isCanceled {
                                Text("Canceled!")
                                    .font(.caption2)
                            }
                            
                            Text("\(entry.subject)")
                                .font(.headline)
                            
                            Text("\(entry.time)-\(entry.timeTo), \(entry.classroom)")
                                .font(.caption)
                            
                            Text("\(entry.teacher)")
                                .font(.caption)
                        }
                        
                        Spacer()
                        
                        Text("\(entry.lessonNo)")
                            .font(.title)
                            .fontWeight(.semibold)
                    }
                    .foregroundStyle(entry.isCanceled ? SwiftUI.Color.red : SwiftUI.Color.primary)
                }
            }
        }
        .refreshable { await self.refresh() }
    }
}
