import Shared
import SwiftUI

struct TimetableView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack(path: self.$viewModel.pathState) {
            List {
                ForEach(self.viewModel.timetable, id: \.date) { timetable in
                    NavigationLink(value: timetable) {
                        Label(weekDayFormat.string(from: timetable.date), systemImage: "\(Calendar.current.component(.weekday, from: timetable.date)).calendar")
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Timetable")
            .navigationDestination(for: ViewModel.NavKey.self) { timetable in
                TimetableDayView(timetable: timetable, refresh: self.viewModel.refresh)
                    .navigationTitle("Timetable - \(weekDayFormat.string(from: timetable.date))")
            }
        }
    }
}

let weekDayFormat = {
    let f = DateFormatter()
    f.dateFormat = "EEEE"
    
    return f
}()
