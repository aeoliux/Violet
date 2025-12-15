import Shared
import SwiftUI

extension GradesPerSubjectView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var grades = [[GradeInfo]]()
        var gradesTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        var subject: String
        init(_ subject: String) {
            self.subject = subject
            
            super.init()
            
            self.gradesTask = Task {
                for await grades in self.repos.gradesRepository.getGradesFlow(subject: self.subject) {
                    self.grades = grades.compactMap { semester in
                        semester.map { GradeInfo(color: $0.color.toColor() ?? Color.primary, grade: $0) }
                    }
                }
            }
        }
        
        deinit {
            self.gradesTask?.cancel()
        }
        
        func refresh() async {
            await self.task {
                try await self.repos.gradesRepository.refresh()
            }
        }
        
        struct GradeInfo: Hashable, Equatable {
            let color: SwiftUI.Color
            let grade: Grade_
        }
    }
}
