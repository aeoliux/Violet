import Shared
import SwiftUI

extension HomeView {
    @Observable
    class ViewModel: RefreshableViewModel {
        let repos = RepositoryHelper()
        
        var navPath = NavigationPath()
        
        var aboutMe: AboutMe?
        var luckyNumber: Int = 0
        var timetable = [Timetable]()
        var timetableDate: Date?
        var latestGrades: [(Grade_, SwiftUI.Color)] = []
        
        var aboutMeTask: Task<(), Never>?
        var luckyNumberTask: Task<(), Never>?
        var timetableTask: Task<(), Never>?
        var gradesTask: Task<(), Never>?
        
        override init() {
            super.init()
            
            self.aboutMeTask = Task {
                for await aboutMe in self.repos.aboutMeRepository.getAboutMeFlow() {
                    self.aboutMe = aboutMe
                }
            }
            
            self.luckyNumberTask = Task {
                for await luckyNumber in self.repos.luckyNumberRepository.getLuckyNumberFlow() {
                    self.luckyNumber = luckyNumber.intValue
                }
            }
            
            self.timetableTask = Task {
                for await timetable in self.repos.timetableRepository.getCurrentTimetable() {
                    guard
                        let date = timetable?.first?.toNSDate(),
                        let timetable = timetable?.second as? [Timetable]
                    else { continue }
                    
                    self.timetable = timetable
                    self.timetableDate = date
                }
            }
            
            self.gradesTask = Task {
                for await grades in self.repos.gradesRepository.getLatestGrades(amount: 5) {
                    self.latestGrades = grades.map { ($0, $0.color.toColor() ?? SwiftUI.Color.accentColor) }
                }
            }
        }
        
        deinit {
            self.aboutMeTask?.cancel()
            self.luckyNumberTask?.cancel()
            self.timetableTask?.cancel()
            self.gradesTask?.cancel()
        }
        
        func refresh() async {
            await self.task {
                try await self.repos.fullRefresh()
            }
        }
    }
}
