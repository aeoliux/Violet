import Shared
import SwiftUI

struct AttendanceView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(self.viewModel.dates, id: \.self) { date in
                if let entries = self.viewModel.attendance[date] {
                    Section(dateFormat.string(from: date)) {
                        ForEach(entries, id: \.id) { entry in
                            AttendanceComponent(entry: entry)
                        }
                    }
                }
            }
        }
        .navigationTitle("Attendance")
        .refreshable { await self.viewModel.refresh() }
        .sheet(isPresented: self.$viewModel.showSummary) {
            AttendanceSummaryView()
        }
        .toolbar {
            Button {
                self.viewModel.showSummary = true
            } label: {
                Image(systemName: "percent")
            }
        }
    }
}

let dateFormat = {
    let f = DateFormatter()
    f.dateFormat = "dd MMMM YYYY"
    
    return f
}()
