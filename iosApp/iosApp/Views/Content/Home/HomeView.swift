import SwiftUI
import Shared

struct HomeView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack {
            List {
                HomeLuckyNumberSection(luckyNumber: self.viewModel.luckyNumber)
                HomeTimetableSection(timetable: self.viewModel.timetable, timetableDate: self.viewModel.timetableDate ?? Date())
                HomeGradesSection(grades: self.viewModel.latestGrades)
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Hi, \(self.viewModel.aboutMe?.firstName ?? "")")
        }
    }
}
