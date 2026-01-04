import Shared
import SwiftUI

struct TimetableView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack(path: self.$viewModel.pathState) {
            List {
                ForEach(self.viewModel.timetable, id: \.date) { timetable in
                    NavigationLink(value: timetable) {
                        Label(timetableDateFormat.string(from: timetable.date), systemImage: "\(Calendar.current.component(.weekday, from: timetable.date) - 1).calendar")
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Timetable")
            .navigationDestination(for: ViewModel.NavKey.self) { timetable in
                TimetableDayView(timetable: timetable, refresh: self.viewModel.refresh)
                    .navigationTitle(timetableDateFormat.string(from: timetable.date))
            }
        }
    }
}

let weekDayFormat = {
    let f = DateFormatter()
    f.dateFormat = "EEEE"
    
    return f
}()

let timetableDateFormat = {
    let f = DateFormatter()
    f.dateFormat = "EEEE, dd MMMM"
    return f
}()
