import SwiftUI
import Shared

struct HomeView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack(path: self.$viewModel.navPath) {
            List {
                HomeLuckyNumberSection(luckyNumber: self.viewModel.luckyNumber)
                HomeTimetableSection(timetable: self.viewModel.timetable, timetableDate: self.viewModel.timetableDate ?? Date())
                HomeGradesSection(grades: self.viewModel.latestGrades) { self.viewModel.navPath.append($0) }
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Hi, \(self.viewModel.aboutMe?.firstName ?? "")")
            .navigationDestination(for: Grade_.self) { grade in
                GradeView(grade)
            }
        }
    }
}
