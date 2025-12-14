import Shared
import SwiftUI

extension GradesView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var grades = [NavKey]()
        var gradesTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.gradesTask = Task {
                for await grades in self.repos.gradesRepository.getGradesFlow() {
                    self.grades = grades.compactMap { (subject, grades) in
                        guard
                            let subject = subject as? String,
                            let grades = grades as? [Grade_]
                        else { return nil }
                        
                        let gradesMapped = grades.map {
                            NavKey.GradeInfo(grade: $0, color: $0.color.toColor() ?? SwiftUI.Color.accentColor)
                        }
                        
                        return NavKey(subject: subject, grades: gradesMapped)
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
    }
    
    struct NavKey: Hashable, Equatable {
        let subject: String
        let grades: [GradeInfo]
        
        struct GradeInfo: Hashable, Equatable {
            let grade: Grade_
            let color: SwiftUI.Color
        }
    }
}
